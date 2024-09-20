package io.spbx.util.code.gen;

import io.spbx.util.code.gen.CompiledTemplate.Block;
import io.spbx.util.code.gen.CompiledTemplate.CompiledDirective;
import io.spbx.util.code.gen.CompiledTemplate.DirectiveBlock;
import io.spbx.util.code.gen.CompiledTemplate.Node;
import io.spbx.util.collect.ListBuilder;
import io.spbx.util.func.Predicates;
import io.spbx.util.func.ScopeFunctions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Stream;

import static io.spbx.util.collect.BasicIterables.newMutableList;
import static io.spbx.util.collect.BasicStreams.streamOf;
import static java.util.Objects.requireNonNull;

class TemplateCompiler {
    static @NotNull CompiledTemplate compile(@NotNull Stream<String> lines, @NotNull Marking marking) {
        List<Node> nodes = lines
            .flatMap(line -> {
                DirectivePosition pos = marking.extract(line);
                if (pos == null) {
                    return streamOf(line).map(Node::literalOf);
                }
                return streamOf(
                    Node.literalOf(line.substring(0, pos.start()))
                        .nullifyIf(Predicates.and(Node::isEmpty, !pos.directive().isTreatedInline())),
                    Node.directiveOf(pos.directive()),
                    Node.literalOf(line.substring(pos.end()))
                        .nullifyIf(Node::isEmpty)
                ).filter(Objects::nonNull);
            }).toList();

        var stack = new Stack<Entry>() {
            void pushOpen(@NotNull Directive directive) {
                assert !directive.isTreatedInline();
                Entry entry = new Entry(directive, ListBuilder.builder(), newMutableList());
                this.push(entry);
            }
            void appendToTop(@NotNull Directive directive) {
                assert directive.isTreatedInline();
                CompiledDirective compiledDirective = CompiledDirective.from(directive);
                DirectiveBlock block = DirectiveBlock.of(compiledDirective, List.of());
                this.peek().append(block);
            }
            void appendToTop(@NotNull String literal) {
                this.peek().literals().add(literal);
            }
            void popAndClose() {
                Entry top = this.pop();
                assert top.isOpen() : "Stack top can't be closed: " + top;
                CompiledDirective compiledDirective = CompiledDirective.from(top.open());
                DirectiveBlock block = DirectiveBlock.of(compiledDirective, top.close());
                this.peek().append(block);
            }
        };

        stack.push(Entry.empty());
        for (Node node : nodes) {
            if (node.isLiteral()) {                                                 // literal
                stack.appendToTop(node.literal());
            } else {
                Directive directive = node.directive();
                Entry peek = stack.peek();
                if (directive.isTreatedInline()) {                                  // inline block
                    stack.appendToTop(directive);
                } else if (peek.isOpen() && peek.canBeReplacedBy(directive)) {      // if -> else
                    assert !directive.modifier().isEnd() : "Unexpected directive: " + directive;
                    stack.popAndClose();
                    stack.pushOpen(peek.updateForReplacement(directive));
                } else if (peek.isClosed() || !peek.canBeClosedBy(directive)) {     // nested
                    assert !directive.modifier().isEnd() : "Unexpected directive: " + directive;
                    stack.pushOpen(directive);
                } else {                                                            // if -> end
                    assert directive.modifier().isEnd() : "Unexpected directive: " + directive;
                    stack.popAndClose();
                }
            }
        }

        if (stack.peek().isOpen() && stack.peek().isEot()) {
            stack.popAndClose();
        }

        assert stack.size() == 1 : "Directive must be closed: " + stack.peek().open();
        assert stack.peek().isClosed() : "Directive must be closed: " + stack.peek().open();

        return CompiledTemplate.of(stack.peek().close());
    }

    private record Entry(@Nullable Directive open, @NotNull ListBuilder<Block> blocks, @NotNull List<String> literals) {
        public static @NotNull Entry empty() {
            return new Entry(null, ListBuilder.builder(), newMutableList());
        }

        public @NotNull Directive open() {
            return requireNonNull(open);
        }

        public boolean isOpen() {
            return open != null;
        }

        public boolean isClosed() {
            return open == null;
        }

        public boolean canBeClosedBy(@NotNull Directive directive) {
            return open().canBeClosedBy(directive);
        }

        public boolean canBeReplacedBy(@NotNull Directive directive) {
            return open().is(Directive.Predefined.IF) && directive.is(Directive.Predefined.ELSE);
        }

        public @NotNull Directive updateForReplacement(@NotNull Directive directive) {
            assert open().is(Directive.Predefined.IF);
            assert directive.is(Directive.Predefined.ELSE);
            return directive.withAttrs(open().attrs());
        }

        public @NotNull List<Block> close() {
            return finalizeLiterals().toGuavaImmutableList();
        }

        public void append(@NotNull DirectiveBlock block) {
            finalizeLiterals().add(block);
        }

        private @NotNull ListBuilder<Block> finalizeLiterals() {
            return ScopeFunctions.alsoRun(blocks, () -> {
                if (!literals.isEmpty()) {
                    blocks.add(CompiledTemplate.LiteralBlock.of(literals));
                    literals.clear();
                }
            });
        }

        public boolean isEot() {
            return open().is(Directive.Predefined.EOD_OF_TEMPLATE);
        }
    }
}
