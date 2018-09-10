## Declarative Generative Graphics with Thi.ng

In the [visual programming facebook group](http://bit.ly/2Qg0PUO) lately an interesting library, which has nothing to do with visual programming on first sight, was brought to my attention: [thi.ng](http://thi.ng/). At first, the minimalistic, clean look and feel of the imagery presented on the website caught my eye, but I was also a little bit anxious, because the whole project is written in [Clojure](https://clojure.org/), and I'm by no means at all an expert in that language. Frankly, I hardly know the basics.

But, ignorance should never keep you from exploring, and I know an interesting library when I see one. So I started dabbling around, and was immediately stumped, because I seemingly couldn't get it to work on my machine. Clojure runs on the Java Virtual Machine (JVM) and uses a build tool called [leiningen](https://leiningen.org/), and I ran into one version conflict after another.

## Docker to the Rescue

Then I realized, there is one go-to tool to use when you need a defined, known-to-work environment to run your software in: Docker. So I'd like to share the process of how I got thi.ng to work and generate simple yet beautiful graphics with you. In a sense, this episode is fifty-fifty a hands-on Docker workshop, and an introduction to a language and library I'm a beginner in myself, but I will do my best to share my learnings with you.

To start out, let's look at what thi.ng's geom [project readme](https://github.com/thi-ng/geom/blob/master/src/index.org) tells us. Thi.ng is written in a literate programming style, which means that source code is intertwined with description and documentation in one document, and in order to use it, we must extract the code first. Here, this process is called _tangling_. 

So let's see how we can get a working draft up and running. First, we go to [hub.docker.com](https://hub.docker.com/) and search for a [clojure](https://hub.docker.com/_/clojure/) image, which we, not surprisingly, find. Afterwards, we can immediately jump in by starting a container based on this image. (If you try this the first time, you will need to sign up for the Docker Hub, but it's well worth it.) So we specify that we'd like to start a container with an interactive terminal (`-ti`) that will be deleted after closing (`--rm`) and run a `bash` shell on it. Easy enough. The process of downloading and extracting can take a few minutes, depending on your setup. 

We then continue to do some basic setup on our (debian-based) container: We update the `apt` cache and install `git` and `emacs`, which will both be needed to obtain the source code. Once we have done that, we can clone the repository, and start the tangling process. What's more, we need to install all modules into local jars, so we can use them later on.

Let's head to the svg examples directory and fire up a `lein repl`. We load an example file that downloads and renders Blender's Suzanne model. So it seems as if something was rendered. We just don't have the chance to see it because we have no way of accessing it from outside the container. Let's change that now.

## Connecting the Host's File System

Let's now go through this process again, but this time formalize it in a Dockerfile, so we can store and tag the image. As usual, we start with a `FROM` clause to specify our source image, and then sequentially run the commands we just did manually. We can now `build` the image specified and tag (`-t`) it locally as "thing".

I'll speed this part up, it's basically the same we just did, it's just going to be baked in an image this time. And here we got it. The important message is at the bottom: `Successfully tagged thing:latest`. We can now `docker run` a container based on that image, this time we just add a _bind mount_ (the `-v` option) from a local directory (which doesn't need to exist, it will be created) to the `/tmp/rendered` folder in the container.

Let's head to our examples again and load the Suzanne demo. We exit the REPL, copy the generated svg to our `/tmp/rendered` folder and exit the container, too. We open the directory on the host and see the SVG version of Suzanne has been created.

## Using Docker Containers as a Single Command

Now we get to a point where the use of Docker gets really interesting. We can run services in Docker (such as a database), or attach to shells, as we just did, but we don't need to. Docker containers can also be used to run a _single command_ on some source code. Let's therefore rework our Dockerfile: We actually don't need all the tangling and installing if we just use Docker to manage a Clojure project that we store on our host. We specify `/usr/src/app` as our work directory, since that's how it's specified in the image's documentation. 

And then we specify two related directives: `ENTRYPOINT` and `CMD`. They are very similar, the differences between them are very subtle, but let's just say that in our case `ENTRYPOINT` specifies the command that is run by Docker _every time this container is run_, and `CMD` is the arguments/actions we pass to it. There are cases when you don't need an `ENTRYPOINT`, for example, but here we can make good use of it. We specify the `ENTRYPOINT` as `lein`, the leiningen build tool, and `CMD` as a chained action of `do`, `deps`, and `repl`, which will load the project dependencies and start the REPL one after the other. The key point is that you can **overwrite** the `CMD`, but not the `ENTRYPOINT` when you run the container.

We re-build and re-tag our image and do the following: We run our container with a _bind mount_ pointing to a `thing` folder in our host's working directory and say `new app lcg-viz` at the end. As mentioned above, Docker will expand this to `lein new app lcg-viz` and, well, create a new app skeleton for us here in the `thing` folder. 

We open our editor and see the project structure. First, we are going to make some changes to the `project.clj` definition file, especially adding the `thi.ng` dependency, which will then downloaded automatically from [clojars.org](https://clojars.org/thi.ng/geom)

## The Linear Congruential Generator

So I wondered what would make a good little starting project here and an old project of Patrik's came to my mind, which dealt with the Linear Congruential Generator, which most of you know as a pseudo-random-number generator in most programming languages. It is defined recursively like so:

`X[n+1] = (aX[n] + c) % m`

Now just looking at this you see that every next number depends on the current number with some math applied and then a modulo division afterwards (usually by a very large number, see [https://en.wikipedia.org/wiki/Linear_congruential_generator](https://en.wikipedia.org/wiki/Linear_congruential_generator) for examples). What this means is that the sequence of numbers generated by this algorithm is in fact periodic, because if we ever reach the same `X[n]` again, and all other parameters remain unchanged, it has to start all over again, because of the modulus. 

And it turns out that you can visualize this if you take tuples (pairs, triples, ...) of successive generated numbers and plot them in their respective (2D, 3D, ...) space. You get what's called _hyperplanes_ and it is pretty easy to visualize them.

## Code Walkthrough

So let's paste in some code here and I'll go over it very quickly, just to give you a hint of what's going on. It won't be very deep, because

1. I don't know enough about Clojure and the thi.ng framework, I barely got my feet wet myself, so please bear with me,
2. this episode is meant to give you a hint about what it's like creating applications with Docker, and
3. because we're over time here already.

So at the bottom here we see that an SVG file is spit out by a function named `lcg-svg-2d` which obviously takes some of the LCG parameters and an iteration count. We are going to look at what this function is composed of now. 

At the top we have the `lcg` function itself, which spits out the next pseudo-random based on the passed in arguments. Nothing surprising here. Next is the `lcg-seq` function, which additionally receives a `start` value and an `iter`ation count. This function loops until an accumulator array has the specified length, and on each recursion `conj`oins the current `acc` with the next `lcg` value and passes it to the loop again (that's what `recur` does). 

Next is the `lcg-svg-2d` function which does nothing more than make dots from such a sequence. We start with a sequence, partition it in pairs, and make a circle with a random bluish color, that's all what this `map` function does. So out of it we get an array of `svg/circle`s which can be passed to `svg/group`, which will effectively capture them together in a `<g>` tag and give it some background.

Eventually, this is piped into `svg-doc`, which just assembles an SVG document and serializes it to be written to a file afterwards. Let's try that out real quick in our container. And there is our output, some two dimensional LCG hyperplanes ("lines").

## Conclusion

Now, maybe just a quick thoughts what I find interesting about this approach: its declarative nature. If you compare it to other popular frameworks, you can almost say, the code _IS_ the graphics. Because it's _pure_ in the functional sense, you can insert or skip transformations at will and see what comes out. When you've gained enough experience with the framework and the language, I can imagine you can almost _see_ the patterns that emerge, especially when they are of iterative/recursive nature. In other programming environment, this is the point where the code almost always becomes procedural and/or imperative. 

I don't know enough of this framework yet to say what's possible and what's not, but as of now it _at least_ serves as an opportunity to create interesting material to use somewhere else. Let's try and explore that in a future episode.

