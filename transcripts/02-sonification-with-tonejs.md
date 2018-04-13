## Disclaimer

So here's a disclaimer: This video is actually the cut-up of a live coding session I did the other day. I just removed the wrong paths I've taken and cut out some lengthy pauses in order to not confuse or bore you. But otherwise this clip has been recorded without prior preparation, I just wanted to see where I can get in half an hour without knowing to much of the library But don't worry, it won't be that lengthy, so let's see how it goes.

So I came along this library called **tonejs**, and I have already been looking for a decent Web Audio API wrapper for a longer time, I decided to give it a try. One of my other side projects includes a gamelike generative audio interaction pattern, so this sounded very promising.

We start out the same way as in the previous episode, by cloning the electron quick start. For reference I've included the node version I'm using here. Let's install the dependencies, plus the `paper`js and `tone`js packages. I'm using the former as a simple visualization for this tutorial, but save it up for future tutorials.

Let's see if this works - good. We open VS Code and remove the boilerplate HTML again. Let's add a `<canvas>` element and style it, like in the previous video. We start it and get our black window.

## Installing paper.js

We head to `renderer.js` and require in `paper`. Here's something you need to know before we continue. Paper does a little magic behind the scenes, that's why usually you wrap it in a `<script type="text/paperscript">` tag. You can, however, make it work in plain javascript if you make the paper scope globally available by _installing_ it: [http://paperjs.org/tutorials/getting-started/using-javascript-directly/](http://paperjs.org/tutorials/getting-started/using-javascript-directly/)

We copy and paste that sample code, which draws a simple line on the screen, set width, height and background color, and voilà, there it is. Now from the same page, we take the sample code to set up a mouse tool, and insert that. This is simply adding points to a path when the mouse is held down. Okay, that seems to work.

From another [tutorial](http://paperjs.org/tutorials/interaction/creating-mouse-tools/), we draw in an example where circles are drawn on the path according to a minimum and maximum distance between drag events. Let's see what that looks like. Okay, now play around with that distances a little - fine.

## Drawing in Tone.js

Let's require in `tone` now, and just take that example from the  [getting started page](https://tonejs.github.io/). Okay, let's refresh... oops, seems like we have an error here. Ah, of course, we have to assign that required module to a variable. Okay, let's trigger a sound whenever a drag event occurs.

Let's try a `MetalSynth` now, I've taken it from [this example](https://github.com/Tonejs/Tone.js/blob/master/examples/bembe.html). Let's look up what `triggerAttackRelease` does with this synth - if you ask me, it's a little black mark on the tonejs package that this isn't consistent across different instruments. Ah okay, duration, (offset) time and velocity. Let's try a duration of 500ms, starting immediately.

It seems that events are overlapping here, so let's get shorter. Obviously, polyphony is a little problem here. It turn's out, theres [`PolySynth`](https://github.com/Tonejs/Tone.js/blob/master/examples/polySynth.html) to achieve that, but we're not going this deep here. So, adjust minimum distance and duration again. Alright, this will do for now.

Let's look at the note's velocity (i.e., it's volume). We make that dependent on the distance between two events, of course normalized to those 40 pixels. We do the same to the frequency, but this time take the events Y coordinate. Let's `console.log` that - look's good.

## Designing Sound

Now off to some sound design. We make the duration even shorter,  seems much smoother now. Let's take a look at some effects that are at our disposal. I love a good [bitcrusher](https://tonejs.github.io/docs/r12/BitCrusher), so let's try that out. Oops, we need to pull that up so we can connect them in the right order. Send the bitcrusher to master and connect the synth to the bitcrusher.

8 bits is a little too soft, let's try 4. Okay, a bit too harsh, let's go with 6. Another thing we can add, is a [feedback delay](https://tonejs.github.io/docs/r12/FeedbackDelay). The creation arguments here are delay time and feedback amount. Here, the timing is in seconds obviously, another little inconsistency, or maybe I've overlooked something in the docs in this quick workout. We hook it up in correct order and take a listen.

Okay, as one last thing, we make a lowpass [filter](https://tonejs.github.io/docs/r12/Filter) and need to be careful to instantiate and connect everything in right order. We hook up the filter cutoff frequency to the mouse event's X coordinate, multiplied by 2. 

So that's it for this video, hope you enjoyed the _liveness_ character of it, see you!

