import { ASCII } from "./ASCII";
import { Command } from "./Command";

// 0x31 0x0F 0xF0 0x0D 0x01 0x00 0xAE 0x02 0x07 0x07 0x7F 0x20 0x10 0x08 0x10 0x20 0x7F 0x66

type Options = {
  scale?: number;
};

export class Command31 extends Command {
  static readonly ID = "0x31";
  private payload: Uint8Array;

  static readonly COLOR_BOTH = 0x00;
  static readonly COLOR_RED = 0x01;
  static readonly COLOR_BLACK = 0x02;

  static readonly COLORS = {
    RED: "#ff0000",
    BLACK: "#000000",
  } as const;

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

  // Convert the pixel bits in the payload into an ASCII table string.
  toBitmap(bytes: Uint8Array, height: number, width: number) {
    const count = Math.floor(bytes.length / width) || 0;

    const maxRows = Math.min(height, count * 8);
    const rows = Array.from({ length: maxRows }, () => "");

    for (let col = 0; col < width; col++) {
      const start = col * count;
      let colBits = "";
      for (let b = 0; b < count; b++) {
        const byte = bytes[start + b] ?? 0;
        let bin = byte.toString(2).padStart(8, "0");
        bin = bin.split("").reverse().join("");
        colBits += bin;
      }

      rows[col] = colBits;
    }

    return rows
      .reduce<string[][]>((acc, row) => {
        const rowBits = row.split("");

        rowBits.forEach((bit, index) => {
          acc[index] = acc[index] ?? [];
          acc[index]?.push(bit);
        });

        return acc;
      }, [])
      .map((rowBits) => rowBits.join(""));
  }

  render(canvas: HTMLCanvasElement, opts?: Options) {
    this.isValid();

    const colorType = this.getColorType();
    const { x, y, height, width } = this.getDimensions();

    if (
      typeof colorType === "undefined" ||
      typeof x === "undefined" ||
      typeof y === "undefined" ||
      typeof height === "undefined" ||
      typeof width === "undefined"
    ) {
      throw new Error("Invalid Command 31 data");
    }

    const bytes = this.payload.slice(7, this.payload.length); // exclude final PLC-like byte

    const char = ASCII.bitmapToChar(this.toBitmap(bytes, height, width));

    if (char === "█") {
      console.warn(
        "No matching ASCII character found for bitmap, rendering as block:",
        {
          bitmap: this.toBitmap(bytes, height, width),
          raw: this.toRawArray(bytes),
        },
      );
    }

    // number of byte-rows per column
    const count = Math.floor(bytes.length / width) || 0;
    const scale = opts?.scale ?? 3;
    const ctx = canvas.getContext("2d");

    // helper to set one pixel (scaled rectangle)
    function setPixel(px: number, py: number, color: string) {
      if (ctx) {
        ctx.fillStyle = color;
        ctx.fillRect(px * scale, py * scale, scale, scale);
      }
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

        if (colorType === Command31.COLOR_BOTH) {
          // both colors: '1' -> red, '0' -> black
          if (bit === "1") setPixel(xPos, yPos, Command31.COLORS.RED);
          else setPixel(xPos, yPos, Command31.COLORS.BLACK);
        } else if (colorType === Command31.COLOR_RED) {
          if (bit === "1") setPixel(xPos, yPos, Command31.COLORS.RED);
        } else if (colorType === Command31.COLOR_BLACK) {
          if (bit === "1") setPixel(xPos, yPos, Command31.COLORS.BLACK);
        }
      }
    }
  }
}
