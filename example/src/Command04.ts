import { Command } from "./Command";

// 0x04 0x0B 0xF4 0x02 0x53 0x10 0x31 0x30 0x00 0x50 0x00 0xE0 0x11 0x1A

type Options = {
  scale?: number;
};

export class Command04 extends Command {
  static readonly ID = "0x04";
  private payload: Uint8Array;

  constructor(frame: string[]) {
    super(frame);

    this.payload = this.getPayload();
  }

  private getDimensions() {
    return {
      height: (this.payload[5] << 8) | this.payload[6],
      width: (this.payload[7] << 8) | this.payload[8],
    };
  }

  render(canvas: HTMLCanvasElement, opts?: Options) {
    this.isValid();

    const { height, width } = this.getDimensions();

    const scale = opts?.scale ?? 3;

    canvas.width = width * scale;
    canvas.height = height * scale;

    const ctx = canvas.getContext("2d");

    if (!ctx) {
      throw new Error("Failed to get canvas context");
    }

    // fill background (Command 04 indicates display size; default screen is black)
    ctx.fillStyle = "#000000";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    console.log("Command 04 - Set display size:", { width, height, scale });
  }
}
