## The Case for Electron

I‘ve done quite a few projects with Processing in the past, but the switch to p5js has provided a convenient way to interface with the ever-increasing amount of available web technologies in the form of browser standards or, more simply, JavaScript libraries. Indeed, the whole world of npmjs can be at your disposal if you so wish. 

However, packaging up those dependencies can be cumbersome and require a build process set up, which is kind of discouraging for the creative bunch of you out there, who just want to use their projects in a performative environment. 

Clearly, having web technologies at your disposal is awesome, yet requiring a browser to be running all the time seems to be more of an overdo. Plus, there‘s privacy concerns. Will it transmit all my passwords? Do I need to run it in a sandbox? And, more down-to-earth, how does it connect to hardware? Will my MIDI device work?

Luckily, there‘s electron. Basically that’s a chromium core running in a nodejs process, capable of rendering everything just as in a browser, plus its very easy to get all the goodness from a desktop application, such as interfacing with periphery (think MIDI, OSC etc.)

So I decided to show you real quick how you can set up your p5 patch to run in electron and connect it to Max via OSC in just a few minutes. 

## A P5-Electron Boilerplate

First we‘re going to fire up a terminal and clone the electron quickstart repository from Github, which is the official boilerplate from Electron. We're going to install all dependencies that come with it using `npm i` right away. Additionally, let's install `p5`, `osc`, and `midi`, so we can start connecting our application to the outside world immediately. With `npm start` we can confirm that this setup works properly. Voilà, here is electron's `Hello World` output.

We'll start a code editor to inspect what we have come up with. Basically, the quickstart boilerplate consists of a `main.js` file responsible for the main process, a `renderer.js` containing code running in the embedded chromium browser, and an `index.html` displaying the initial page that will be rendered. 

We're going to delete the boilerplate output and add a container div and some styling for our p5.js canvas. Below you can see the `renderer.js` being required into the HTML's DOM. Let's run that. A black page, as expected.

## Adding a Sketch

Back to our project structure. We're going to create a `p5` directory, and a `sketch.js` in it. To be able to work with p5 here, we need to run it in [instance mode](https://github.com/processing/p5.js/wiki/Global-and-instance-mode). This basically means we pass a `p5` instance down to this module, this namespaces our sketch under the `p` variable and makes it more integration-friendly with other libraries - such as electron. 

Consequently, we need to define and call everything pertaining to p5 with dot notation: In `p.setup` we create the canvas with the window's width and height and assign it to the canvas's container. In `p.draw`  we just set the background to a light grey for now. Ok, let's leave that for now.

If we open up `renderer.js` now, there's a few things we need to  set up. First we'll require in, well, `p5`, then the `sketch`. Lastly, and this completes the _instance mode_, we instantiate an `app` using `p5`s constructor and pass in the `sketch`.

Ok, let's look at that. A grey canvas, cool.

## Interfacing with Max using OSC

Back in the sketch, we load the `osc` module and instantiate a `UDPPort`. When a message comes in, we're going to log that to the console for now. Finally, we'll need to open it. By default, it listens on port `57121`, for reasons unbeknownst to me.

Let's `npm start` that again. Being run in a chromium instance, we can fire up the dev tools typing [Cmd-Opt-I]. Using a simple Max patch, we can send in a little message, and voilà, here it is in the browser's console.

We're going to rework this patch a little, so we can send in positions.

In the sketch file, let's create two coordinate variables. We're  going to draw an ellipse located at these coordinates. Ok, here it is.

In the message receiving callback, we need to switch on the message's `address` and re-assign the position coordinates. Back in the Max patch, we can use the number boxes to control the circle's position. This concludes this video, and hopefully leaves you with plenty of ideas to use this setup.