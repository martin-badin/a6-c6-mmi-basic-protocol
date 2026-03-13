import { Command } from "./Command";

// 0x39 0x08 0xF7 0x08 0x01 0x02 0xC8 0x0C 0x1B 0x18 0x0A

type Options = {
  scale?: number;
};

export class Command39 extends Command {
  static readonly ID = "0x39";
  private payload: Uint8Array;

  static readonly COLOR_RED = 0x00;
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

  render(canvas: HTMLCanvasElement, opts?: Options) {
    this.isValid();

    const color = this.getColorType();
    const { x, y, height, width } = this.getDimensions();

    if (
      typeof color === "undefined" ||
      typeof x === "undefined" ||
      typeof y === "undefined" ||
      typeof height === "undefined" ||
      typeof width === "undefined"
    ) {
      throw new Error("Invalid Command 39 data");
    }

    const scale = opts?.scale ?? 3;
    const ctx = canvas.getContext("2d");

    if (!ctx) {
      throw new Error("Failed to get canvas context");
    }

    const fillColor =
      color === Command39.COLOR_RED
        ? Command39.COLORS.RED
        : color === Command39.COLOR_BLACK
          ? Command39.COLORS.BLACK
          : Command39.COLORS.BLACK;
    ctx.fillStyle = fillColor;
    ctx.fillRect(x * scale, y * scale, width * scale, height * scale);
  }
}
