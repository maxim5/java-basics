package io.spbx.util.io;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.spbx.util.testing.TestingBasics.toPath;

@Tag("fast")
public class BasicFilesTest {
    @Test
    public void getFileExtension_simple() {
        assertThat(BasicFiles.getFileExtension("")).isEqualTo("");
        assertThat(BasicFiles.getFileExtension("foo")).isEqualTo("");
        assertThat(BasicFiles.getFileExtension("foo.txt")).isEqualTo("txt");
        assertThat(BasicFiles.getFileExtension("foo.java")).isEqualTo("java");
        assertThat(BasicFiles.getFileExtension("build.gradle.kts")).isEqualTo("gradle.kts");
        assertThat(BasicFiles.getFileExtension(".git")).isEqualTo("git");
        assertThat(BasicFiles.getFileExtension(".gitignore")).isEqualTo("gitignore");
    }

    @Test
    public void getFileExtensionWithDot_simple() {
        assertThat(BasicFiles.getFileExtensionWithDot("")).isEqualTo("");
        assertThat(BasicFiles.getFileExtensionWithDot("foo")).isEqualTo("");
        assertThat(BasicFiles.getFileExtensionWithDot("foo.txt")).isEqualTo(".txt");
        assertThat(BasicFiles.getFileExtensionWithDot("foo.java")).isEqualTo(".java");
        assertThat(BasicFiles.getFileExtensionWithDot("build.gradle.kts")).isEqualTo(".gradle.kts");
        assertThat(BasicFiles.getFileExtensionWithDot(".git")).isEqualTo(".git");
        assertThat(BasicFiles.getFileExtensionWithDot(".gitignore")).isEqualTo(".gitignore");
    }

    @Test
    public void cutOffFileExtension_simple() {
        assertThat(BasicFiles.cutOffFileExtension("")).isEqualTo("");
        assertThat(BasicFiles.cutOffFileExtension("foo")).isEqualTo("foo");
        assertThat(BasicFiles.cutOffFileExtension("foo.txt")).isEqualTo("foo");
        assertThat(BasicFiles.cutOffFileExtension("foo.java")).isEqualTo("foo");
        assertThat(BasicFiles.cutOffFileExtension("build.gradle.kts")).isEqualTo("build");
        assertThat(BasicFiles.cutOffFileExtension(".git")).isEqualTo("");
        assertThat(BasicFiles.cutOffFileExtension(".gitignore")).isEqualTo("");
    }

    @Test
    public void forceUnixSlashes_simple() {
        assertThat(BasicFiles.forceUnixSlashes("/")).isEqualTo("/");
        assertThat(BasicFiles.forceUnixSlashes("\\")).isEqualTo("/");
        assertThat(BasicFiles.forceUnixSlashes("foo/bar")).isEqualTo("foo/bar");
        assertThat(BasicFiles.forceUnixSlashes("foo\\bar")).isEqualTo("foo/bar");
        assertThat(BasicFiles.forceUnixSlashes("/foo/bar")).isEqualTo("/foo/bar");
        assertThat(BasicFiles.forceUnixSlashes("\\foo\\bar")).isEqualTo("/foo/bar");
        assertThat(BasicFiles.forceUnixSlashes("\\foo\\bar/baz")).isEqualTo("/foo/bar/baz");
    }

    @Test
    public void commonPath_absolute() {
        assertThat(BasicFiles.commonPath(toPath("/"), toPath("/a"))).isEqualTo(toPath("/"));
        assertThat(BasicFiles.commonPath(toPath("/a"), toPath("/"))).isEqualTo(toPath("/"));
        assertThat(BasicFiles.commonPath(toPath("/"), toPath("/a/b/c"))).isEqualTo(toPath("/"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/c"), toPath("/"))).isEqualTo(toPath("/"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/c/"), toPath("/a"))).isEqualTo(toPath("/a"));
        assertThat(BasicFiles.commonPath(toPath("/a"), toPath("/a/b/c/"))).isEqualTo(toPath("/a"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/c/"), toPath("/a/b"))).isEqualTo(toPath("/a/b"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/"), toPath("/a/b/c"))).isEqualTo(toPath("/a/b"));

        assertThat(BasicFiles.commonPath(toPath("/"), toPath("/"))).isEqualTo(toPath("/"));
        assertThat(BasicFiles.commonPath(toPath("/a/"), toPath("/a"))).isEqualTo(toPath("/a"));
        assertThat(BasicFiles.commonPath(toPath("/a/b"), toPath("/a/b"))).isEqualTo(toPath("/a/b"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/c"), toPath("/a/b/c/"))).isEqualTo(toPath("/a/b/c"));

        assertThat(BasicFiles.commonPath(toPath("/a/b/c/d/e"), toPath("/a/b/c/g/h"))).isEqualTo(toPath("/a/b/c"));
        assertThat(BasicFiles.commonPath(toPath("/a/b/c/d"), toPath("/a/d/c/g/h"))).isEqualTo(toPath("/a"));
        assertThat(BasicFiles.commonPath(toPath("/a/bc/d"), toPath("/a/b/c/d"))).isEqualTo(toPath("/a"));
        assertThat(BasicFiles.commonPath(toPath("/ab/c/d"), toPath("/a/b/c/d"))).isEqualTo(toPath("/"));
    }

    @Test
    public void commonPath_relative() {
        assertThat(BasicFiles.commonPath(toPath(""), toPath("a/b"))).isEqualTo(toPath(""));
        assertThat(BasicFiles.commonPath(toPath("a/b"), toPath(""))).isEqualTo(toPath(""));
        assertThat(BasicFiles.commonPath(toPath("a/b/c"), toPath(""))).isEqualTo(toPath(""));
        assertThat(BasicFiles.commonPath(toPath("a/b/c/"), toPath("a"))).isEqualTo(toPath("a"));
        assertThat(BasicFiles.commonPath(toPath("a"), toPath("a/b/c/"))).isEqualTo(toPath("a"));
        assertThat(BasicFiles.commonPath(toPath("a/b/c/"), toPath("a/b"))).isEqualTo(toPath("a/b"));
        assertThat(BasicFiles.commonPath(toPath("a/b/"), toPath("a/b/c"))).isEqualTo(toPath("a/b"));

        assertThat(BasicFiles.commonPath(toPath(""), toPath(""))).isEqualTo(toPath(""));
        assertThat(BasicFiles.commonPath(toPath("a/"), toPath("a"))).isEqualTo(toPath("a"));
        assertThat(BasicFiles.commonPath(toPath("a/b"), toPath("a/b"))).isEqualTo(toPath("a/b"));
        assertThat(BasicFiles.commonPath(toPath("a/b/c"), toPath("a/b/c/"))).isEqualTo(toPath("a/b/c"));

        assertThat(BasicFiles.commonPath(toPath("a/b/c/d/e"), toPath("a/b/c/g/h"))).isEqualTo(toPath("a/b/c"));
        assertThat(BasicFiles.commonPath(toPath("a/b/c/d"), toPath("a/d/c/g/h"))).isEqualTo(toPath("a"));
        assertThat(BasicFiles.commonPath(toPath("a/bc/d"), toPath("a/b/c/d"))).isEqualTo(toPath("a"));
        assertThat(BasicFiles.commonPath(toPath("ab/c/d"), toPath("a/b/c/d"))).isEqualTo(toPath(""));
    }
}
