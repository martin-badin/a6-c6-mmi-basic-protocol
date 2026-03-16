import { Command } from "./Command";

// Examples:
// 0x0A 0x06 0xF9 0x03 0x01 0x03 0xDE 0x02 0x28
// 0x0A 0x06 0xF9 0x05 0x02 0x00 0x16 0x04 0xE0

export class Command0A extends Command {
  static readonly ID = "0x0A";

  constructor(frame: string[]) {
    super(frame);
  }

  // lightweight renderer/helper to inspect request packets
  render() {
    this.isValid();

    console.info("Command 0A request:", this.toRawArray(this.getPayload()));
  }
}
