# markdown-footnotes-sort

A [babashka](https://babashka.org) script to sort footnotes and their links in a Markdown document.
*The script replaces the file's content in-place!*

For example, for the following input:

``` markdown
Lorem[1] ipsum[3] dolor[2] sit[3]

[1]: sharks.com
[2]: dogs.com
[3]: cats.com
```

You'll get:

``` markdown
Lorem[1] ipsum[2] dolor[3] sit[2]

[1]: sharks.com
[2]: cats.com 
[3]: dogs.com 
```

## Usage

After [installing Babashka](https://github.com/babashka/babashka#installation):

``` bash
./mdsort.clj MARKDOWN_FILE
```

This will override the current file with its sorted version

## Credit

This is practically a clone of
[derdennis/sort-markdown-footnotes](https://github.com/derdennis/sort-markdown-footnotes)
