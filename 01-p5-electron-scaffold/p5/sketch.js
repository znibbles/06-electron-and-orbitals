const osc = require('osc');


let posX = 0;
let posY = 0;

// listens on port 57121
const udpPort = new osc.UDPPort({
    localAddress: '0.0.0.0',
    localPort: 57121,
    metadata: true,
});

udpPort.on('message', (oscMessage) => {
    switch(oscMessage.address) {
        case '/pos_x/': posX = oscMessage.args[0].value; break;
        case '/pos_y/': posY = oscMessage.args[0].value; break;
    }
});
  
udpPort.open();


module.exports = (p) => {
    
    p.setup = () => {
        const canvas = p.createCanvas(p.windowWidth, p.windowHeight);
        canvas.parent('my-canvas');
    }

    p.draw = () => {
        p.background(200);

        p.fill(51);
        p.ellipse(posX, posY, 55, 55);
    }

}