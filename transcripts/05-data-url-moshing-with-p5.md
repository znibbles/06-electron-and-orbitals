## Data URL Moshing with P5

Today I'd like to experiment with randomly altering textual image representations.

If you've done some web development in your career, you've probably come across a thing called _data URLs_.  As the MDN guide states, these are primarily used for inlining small files in documents, the typical use case being small icons in CSS files, for example.

Basically, such a data url looks like this: instead of the usual `http` or `https` schemes, they are prefixed by `data:`, followed by the media type (such as `image/png`, for example), and whether it is base64 encoded. Here are some simple examples.

To proceed with a simple image, I've taken this illustration of a planet from the [noun project](https://thenounproject.com/search/?q=planet&i=1065587).

## Setting Up the P5 Sketch

Okay, in this codepen I've already prepared the base64-encoded data string of this image.

We are going to start this example by setting up the p5 `setup` and `draw` functions. I'm deliberately setting a very low frame rate here, so we can easily observe what's happening.

Using `loadImage`, let's load the image into a variable. By try and error I've isolated the parts of the string that do not change (that appear to be header and footer of the PNG stream). First, we are going to simply draw this image 48x48 pixels wide.

In the `draw` function, we paint the background black and do the same.

## Mosh Pit

To perform the data moshing, we need a reference to the valid chars in a base64 encoded string, which I paste in here. And it seems I have forgotten the 0 here...

Now, let's find a random position in our image string by capping the `random` function at its length. Using `slice`, we are going to split it into a `head` and `tail` portion. In the head part we slice everything up to the `randomPosition`, then interleave one character, and start again at `randomPosition + 1`. 

We fetch a random char from the `validChars` string using the same method as above. After that, we construct a new image string, using our head and tail, enclosing our random character. Need to put a dollar sign here as well.

## Results

The result is a succession of glitched versions of the illustration. Obviously exchanging a character often renders the rest of the image invalid, which is why we get large black portions in the result.