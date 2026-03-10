import { Command } from "./Command";

// Example frames (captured):
// 55 04 FB 04 51 E0 1F
// 55 04 FB 87 51 E0 9C
// 55 04 FB A7 51 E0 BC
// 55 04 FB C6 51 E0 DD
// 55 04 FB E6 51 E0 FD
// 55 04 FB 05 51 E0 1E
// 55 04 FB 25 51 E0 3E
// 55 04 FB 44 51 E0 5F
// 55 04 FB 64 51 E0 7F
// 55 04 FB 83 51 E0 98

type Options = {
  scale?: number;
};

export class Command55 extends Command {
  private payload: Uint8Array;

  constructor(frame: string[]) {
    super(frame);

    this.payload = this.getPayload();
  }

  getBrightness(): number {
    return this.payload[1];
  }

  render(canvas: HTMLCanvasElement, opts?: Options) {
    this.isValid();

    const ctx = canvas.getContext("2d");
    if (!ctx) throw new Error("Failed to get canvas context");

    const brightness = Math.max(0, Math.min(255, this.getBrightness()));

    console.log("Rendering Command 55 with brightness:", brightness);

    // simple visual: fill the canvas with a gray level representing brightness
    // ctx.fillStyle = `rgb(${brightness},${brightness},${brightness})`;
    // ctx.fillRect(0, 0, canvas.width, canvas.height);
  }
}
