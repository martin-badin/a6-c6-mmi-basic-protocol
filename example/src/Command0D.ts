import { Command } from "./Command";

// Example frame:
// 0x0D 0x02 0xFD 0x04 0xF6

export class Command0D extends Command {
  static readonly ID = "0x0D";
  private payload: Uint8Array;

  constructor(frame: string[]) {
    super(frame);

    this.payload = this.getPayload();
  }

  getAcknowledgedBlockId() {
    return this.payload[0];
  }

  // lightweight renderer/helper to inspect ACK packets
  render() {
    this.isValid();

    console.info("Command 0D ACK for block:", this.getAcknowledgedBlockId());
  }
}
