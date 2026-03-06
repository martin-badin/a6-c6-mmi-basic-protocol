/*
 * Helpers to parse and render Command 0x31 frames (Screen data / pixel blocks)
 * Usage:
 *   renderCommand31(frame, canvas, { scale: 3 });
 */
function toByteArray(frame) {
  if (frame instanceof Uint8Array) return frame;
  if (Array.isArray(frame)) {
    return Uint8Array.from(
      frame.map((x) => (typeof x === "string" ? parseInt(x, 16) : x)),
    );
  }
  if (typeof frame === "string") {
    // accept "04 0B F4 ..." or "0x04,0x0B,..."
    const tokens = frame
      .replace(/0x/g, "")
      .split(/[^0-9A-Fa-f]+/)
      .filter(Boolean);
    return Uint8Array.from(tokens.map((t) => parseInt(t, 16)));
  }
  throw new TypeError("Unsupported frame format");
}

/**
 * Renders pixel blocks based on Command 31 data. The function extracts the color type, position, dimensions, and pixel data from the frame and draws the corresponding pixels on the canvas.
 *
 * @param {string|Array|Uint8Array} frame - The input frame containing command data.
 * @param {HTMLCanvasElement} canvas - The canvas element to draw on.
 * @param {Object} [opts={}] - Optional parameters for rendering.
 * @param {number} [opts.scale=3] - The scale factor for rendering the pixels.
 * @throws {Error} If the frame is too short for Command 31.
 */
function renderCommand31(frame, canvas, opts = {}) {
  const dataFull = toByteArray(frame);
  if (dataFull.length < 10) throw new Error("Frame too short for Command 31");

  // data portion starts at index 3 (skip id,dlc,crc)
  const data = Array.from(dataFull).slice(3);

  const colorType = data[2]; // as per Kotlin: getData()[2]
  const x = data[3];
  const y = data[4];
  const height = data[5];
  const width = data[6];

  const bytes = data.slice(7, data.length - 1); // exclude final PLC-like byte

  // number of byte-rows per column
  const count = Math.floor(bytes.length / width) || 0;
  const scale = opts.scale ?? 3;
  const ctx = canvas.getContext("2d");

  // helper to set one pixel (scaled rectangle)
  function setPixel(px, py, color) {
    ctx.fillStyle = color;
    ctx.fillRect(px * scale, py * scale, scale, scale);
  }

  // build columns
  for (let col = 0; col < width; col++) {
    const start = col * count;
    let colBits = "";
    for (let b = 0; b < count; b++) {
      const byte = bytes[start + b] ?? 0;
      let bin = byte.toString(2).padStart(8, "0");
      bin = bin.split("").reverse().join(""); // reversed as in Kotlin
      colBits += bin;
    }

    // render rows up to declared height
    for (let row = 0; row < Math.min(height, colBits.length); row++) {
      const bit = colBits[row];
      const xPos = x + col;
      const yPos = y + row;

      if (colorType === 0x00) {
        // both colors: '1' -> red, '0' -> black
        if (bit === "1") setPixel(xPos, yPos, "#ff0000");
        else setPixel(xPos, yPos, "#000000");
      } else if (colorType === 0x01) {
        if (bit === "1") setPixel(xPos, yPos, "#ff0000");
      } else if (colorType === 0x02) {
        if (bit === "1") setPixel(xPos, yPos, "#000000");
      }
    }
  }
}

if (typeof module !== "undefined" && module.exports)
  module.exports = { renderCommand31 };
