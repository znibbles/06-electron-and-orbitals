// This file is required by the index.html file and will
// be executed in the renderer process for that window.
// All of the Node.js APIs are available in this process.

const paper = require('paper');
const Tone = require('tone');


const delay = new Tone.FeedbackDelay(0.5, 0.7).toMaster();
delay.wet = 0.5;

const lowpass = new Tone.Filter(700, 'lowpass', -12).connect(delay);

const bitCrusher = new Tone.BitCrusher(6).connect(lowpass);

const bell = new Tone.MetalSynth({
  "frequency" : 300,
  "harmonicity" : 12,
  "resonance" : 800,
  "modulationIndex" : 20,
  "envelope" : {
    "decay" : 0.1,
  },
  "volume" : -15
}).connect(bitCrusher);

paper.install(window);
window.onload = function() {
  paper.setup('my-canvas');
  // Create a simple drawing tool:
  const tool = new Tool();
  tool.minDistance = 10;
  tool.maxDistance = 40;

  tool.onMouseDrag = function(event) {
    const circle = new Path.Circle({
      center: event.middlePoint,
      radius: event.delta.length / 2
    });
    circle.fillColor = 'black';

    const velocity = event.delta.length / 40;
    const frequency = event.middlePoint.y;
    bell.frequency.setValueAtTime(frequency, 0);
    bell.triggerAttackRelease(20, 0, velocity);

    const cutoff = event.middlePoint.x * 2;
    lowpass.frequency.setValueAtTime(cutoff, 0);
  }
}