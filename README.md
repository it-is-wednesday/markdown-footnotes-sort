# markdown-footnotes-sort

Sort footnotes and their links in a Markdown document.

For example, the script will take this:

``` markdown
Lorem[1] ipsum[3] dolor[2] sit[3]

[1]: sharks.com
[2]: dogs.com
[3]: cats.com
```

And print (or write) back this:

``` markdown
Lorem[1] ipsum[2] dolor[3] sit[2]

[1]: sharks.com
[2]: cats.com 
[3]: dogs.com 
```

## Installation
Grab the latest file from the [releases
page](https://github.com/it-is-wednesday/markdown-footnotes-sort/releases)


## Usage

To merely print how the sorted file would look like, without writing:

``` bash
./mdsort MARKDOWN_FILE
```

To override the file's content with new sorted content, pass `--in-place`/`-i`.
