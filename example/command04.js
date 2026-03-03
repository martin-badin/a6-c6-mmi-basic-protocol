/*
 * Helpers to parse and render Command 0x31 frames (Screen data / pixel blocks)
 * Usage:
 *   renderCommand04(frame, canvas, { scale: 3 });
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
 * Renders the display size based on Command 04 data. The function extracts the height and width from the frame and sets the canvas dimensions accordingly. It also fills the background with a specified color.
 *
 * @param {string|Array|Uint8Array} frame - The input frame containing command data.
 * @param {HTMLCanvasElement} canvas - The canvas element to set dimensions and fill background.
 * @param {Object} [opts={}] - Optional parameters for rendering.
 * @param {number} [opts.scale=3] - The scale factor for rendering the display.
 * @throws {Error} If the frame is too short for Command 04.
 */
function renderCommand04(frame, canvas, opts = {}) {
  const data = toByteArray(frame);

  // Full frame layout (example): [id, dlc, crc, ..., height_hi, height_lo, width_hi, width_lo, ...]
  if (data.length < 11) throw new Error("Frame too short for Command 04");

  // height is at indices 8/9 in the full frame (0-based), width at 10/11
  const height = (data[8] << 8) | data[9];
  const width = (data[10] << 8) | data[11];

  const scale = opts.scale ?? 3;

  canvas.width = width * scale;
  canvas.height = height * scale;

  const ctx = canvas.getContext("2d");
  // fill background (Command 04 indicates display size; default screen is black)
  ctx.fillStyle = "#000000";
  ctx.fillRect(0, 0, canvas.width, canvas.height);
}

if (typeof module !== "undefined" && module.exports)
  module.exports = { renderCommand04 };
