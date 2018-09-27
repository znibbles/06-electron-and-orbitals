## Further in the Generative Graphics Chain 

As a follow up to the thi.ng episode, I wanted to quickly give you a glance at what I did with the SVG it generated.

I wanted to do some procedural, interactive graphics processing on it, and I wanted to do it in a browser-based environment so I'd have the ability to use it in every context that provides a modern, Web-GL capable browser. This is the result.

So I could have gone to codepen.io and draft some graphics processing based on three.js. But then, what if I'd discover later on that I need some server-side computations, data persistence etc? Well, I could have gone all in, make a full web app with all its implications of choosing and implementing an architecture, deploying it somewhere, securing it etc. All just for trying something out and maybe tossing it into the trash anyway? The first approach seemed a little to light, the latter a little to heavy-weight.

Enter glitch.com. You can think of glitch as a symbiosis between an online REPL such as codepen, a code repository such as github, and a platform as a service (PaaS) such as heroku. Another platform I wanted to try out, and now I can share my learnings with you :)

Essentially, you can build anything from frontend JS snippets to full-blown express.js-based apps. (According to [this blog post](https://medium.com/@anildash/what-if-javascript-wins-84898e5341a), NodeJS as the backend language isn't even a requirement.) The key point is that it's based around a community-approach of creators helping other creators and _remixing_ their apps (glitch-speak for _forking_, just like you can do with a CodePen pen or a Github repo). And, also not unimportant, [glitch is made by FogCreek Software](https://glitch.com/about), the people behind StackOverflow and founders of Trello (now Atlassian).

So, enough praising, let's get hands-on.

## Setup of a Glitch App

If this is your first time coding at glitch.com you will need to sign in. At the moment you can do this with a facebook or github account. Once that is done, you can choose between three project templates: webpage, express, or sqlite. For this app we will use the hello-express template, we just need to use a unique name, so I'll just name it `znibbles-threejs` for short. 

For the Javascript devs out there: Glitch will happily auto-install any modules from a `package.json` you hand it, but there is of course also a console where you can do this by hand. Let's open the log and paste in a package.json I prepared.

As I understood it, glitch needs a build and a start script, just like most other platforms as a service, so we can use anything here, e.g. webpack, so we add a `webpack.config.js`.

We can also load assets, such as our SVG from the last episode.

## Feedbacking Geometry

Okay, fast forward to some working code. I just switched to the finished app, you can view, or remix it [here](https://znibbles-threejs-svg-loader.glitch.me) if you like. 

I just basically copy and pasted the [SVG loader example](https://threejs.org/examples/#webgl_loader_svg) from the three.js docs, as well as the [Afterimage example](https://threejs.org/examples/#webgl_postprocessing_afterimage).

Unfortunately, I have to say, the code from the three.js examples is not yet integrated into the ES6 module due to namespace pollution, so I had to use the CDN version of three and copy-paste the necessary shader and postprocessing files manually. But hey, that's what a glitch app is for. I did the work, you can go remix it!

We set up a scene, a camera, and a renderer, and load the SVG here. You can obtain the URL of the SVG from the assets folder, it's hosted on a separate CDN, not your project root. 

Other than re-use everything from the examples I just mentioned, I only assigned a random velocity to every generated circle here, and used it to update the circles' position in the `animate()` method. 

Everything else is taken care of by the afterimage shader (which I gave a quite high feedback value of 0.975) and the respective composition passes - first the `RenderPass` rendering the scene from the camera's viewpoint, and the `AfterimagePass` applying the visual feedback, much like a `jit.wake` object would do in MaxMSP, for example.