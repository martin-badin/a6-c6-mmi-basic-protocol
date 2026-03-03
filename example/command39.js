/*
 * Helpers to parse and render Command 0x31 frames (Screen data / pixel blocks)
 * Usage:
 *   renderCommand39(frame, canvas, { scale: 3 });
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
 * Renders a filled rectangle based on Command 39 data. The function extracts the color, position, and dimensions from the frame and draws the rectangle on the canvas.
 *
 * @param {string|Array|Uint8Array} frame - The input frame containing command data.
 * @param {HTMLCanvasElement} canvas - The canvas element to draw on.
 * @param {Object} [opts={}] - Optional parameters for rendering.
 * @param {number} [opts.scale=3] - The scale factor for rendering the rectangle.
 * @throws {Error} If the frame is too short for Command 39.
 */
function renderCommand39(frame, canvas, opts = {}) {
  const dataFull = toByteArray(frame);
  if (dataFull.length < 10) throw new Error("Frame too short for Command 39");

  const data = Array.from(dataFull).slice(3);
  const color = data[2];
  const x = data[3];
  const y = data[4];
  const height = data[5];
  const width = data[6];
  const scale = opts.scale ?? 3;
  const ctx = canvas.getContext("2d");

  const fillColor =
    color === 0x00 ? "#ff0000" : color === 0x02 ? "#000000" : "#000000";
  ctx.fillStyle = fillColor;
  ctx.fillRect(x * scale, y * scale, width * scale, height * scale);
}

if (typeof module !== "undefined" && module.exports)
  module.exports = { renderCommand39 };
