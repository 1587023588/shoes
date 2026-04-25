const fs = require('fs');

let ktPath = 'android-native/app/src/main/java/com/example/shoes/MuseumActivity.kt';
let ktContent = fs.readFileSync(ktPath, 'utf-8');

let ktItems = [];
for (let i = 1; i <= 33; i++) {
    ktItems.push(`            ExhibitionItem("\u5c55\u51fa\u85cf\u54c1 ${i}", R.drawable.m${i})`);
}
let ktItemsStr = ktItems.join(',\n');

ktContent = ktContent.replace(/exhibitionItems = listOf\([\s\S]*?\)/, `exhibitionItems = listOf(\n${ktItemsStr}\n        )`);
fs.writeFileSync(ktPath, ktContent, 'utf-8');

let jsPath = 'pages/museum/index.js';
let jsContent = fs.readFileSync(jsPath, 'utf-8');

let jsItems = [];
for (let i = 1; i <= 33; i++) {
    jsItems.push(`      { id: ${i}, title: '\u5c55\u51fa\u85cf\u54c1 ${i}', url: '/images/museum/m${i}.jpg' }`);
}
let jsItemsStr = jsItems.join(',\n');

jsContent = jsContent.replace(/photos: \[\s*\{[\s\S]*?\}\s*\]/, `photos: [\n${jsItemsStr}\n    ]`);
fs.writeFileSync(jsPath, jsContent, 'utf-8');
console.log("Updated both files via node");
