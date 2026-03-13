import { Command } from "./Command";

// Examples:
// 0x0A 0x06 0xF9 0x03 0x01 0x03 0xDE 0x02 0x28
// 0x0A 0x06 0xF9 0x05 0x02 0x00 0x16 0x04 0xE0

export class Command0A extends Command {
  static readonly ID = "0x0A";
  private payload: Uint8Array;

  constructor(frame: string[]) {
    super(frame);

    this.payload = this.getPayload();
  }

  getRequestedBlockId() {
    return this.payload[0];
  }

  // common observed fields (may vary by firmware/version)
  getMode() {
    return this.payload[1];
  }

  getParamA() {
    return this.payload[2];
  }

  getParamB() {
    return this.payload[3];
  }

  getParamC() {
    return this.payload[4];
  }

  getPayloadHex() {
    return Array.from(this.payload)
      .map((b) => "0x" + b.toString(16).padStart(2, "0"))
      .join(" ");
  }

  // lightweight renderer/helper to inspect request packets
  render() {
    this.isValid();

    console.info("Command 0A request:", {
      blockId: this.getRequestedBlockId(),
      mode: this.getMode(),
      paramA: this.getParamA(),
      paramB: this.getParamB(),
      paramC: this.getParamC(),
      raw: this.getPayloadHex(),
    });
  }
}
