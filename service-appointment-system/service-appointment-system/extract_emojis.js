const fs = require('fs');
const path = require('path');

const dir = 'c:/Users/WISHVA/Desktop/service-appointment-system/service-appointment-system/src/main/resources/templates';

function getEmojis(dirPath, emojis) {
  const files = fs.readdirSync(dirPath);
  for (const file of files) {
    const fullPath = path.join(dirPath, file);
    if (fs.statSync(fullPath).isDirectory()) {
      getEmojis(fullPath, emojis);
    } else if (fullPath.endsWith('.html')) {
      const text = fs.readFileSync(fullPath, 'utf8');
      for (const char of text) {
        // Simple check for surrogate pairs or high code points typical of emojis
        const code = char.codePointAt(0);
        if (code > 0x2500 && code < 0x1FAFF && char.trim() !== '' && !/[a-zA-Z0-9<>/ \-\n\r\t,.'\";:{}\[\]=_+~!@#$%^&*()|\\?]/.test(char)) {
          emojis.add(char);
        }
      }
    }
  }
}

const emojis = new Set();
getEmojis(dir, emojis);
console.log("Emojis found:");
console.log(Array.from(emojis));
