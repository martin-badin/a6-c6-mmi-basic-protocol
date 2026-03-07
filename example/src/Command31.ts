import { Command } from "./Command";

// 0x31 0x0F 0xF0 0x0D 0x01 0x00 0xAE 0x02 0x07 0x07 0x7F 0x20 0x10 0x08 0x10 0x20 0x7F 0x66

type Options = {
  scale?: number;
};

export class Command31 extends Command {
  private payload: Uint8Array;

  constructor(frame: string[]) {
    super(frame);

    this.payload = this.getPayload();
  }

  private getColorType() {
    return this.payload[2];
  }

  private getDimensions() {
    return {
      x: this.payload[3],
      y: this.payload[4],
      height: this.payload[5],
      width: this.payload[6],
    };
  }

  render(canvas: HTMLCanvasElement, opts?: Options) {
    this.isValid();

    const colorType = this.getColorType();
    const { x, y, height, width } = this.getDimensions();

    const bytes = this.payload.slice(7, this.payload.length); // exclude final PLC-like byte

    // number of byte-rows per column
    const count = Math.floor(bytes.length / width) || 0;
    const scale = opts?.scale ?? 3;
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
}
