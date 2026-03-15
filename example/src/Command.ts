const toByteArray = (frame: string[]) => {
  return Uint8Array.from(frame.map((x) => parseInt(x, 16)));
};

// | ID   | LEN  | PLC  | Data (example)           | CRC  |
// | ---- | ---- | ---- | ------------------------ | ---- |
// | 0x0A | 0x06 | 0xF9 | 0x03 0x01 0x03 0xDE 0x02 | 0x28 |
export class Command {
  private frame: Uint8Array;

  constructor(frame: string[]) {
    this.frame = toByteArray(frame);
  }

  isValid() {
    const length = this.getPayloadLength();

    if (length === undefined || length < 0) {
      throw new Error("Invalid payload length");
    }

    if (this.frame.length != length + 3) {
      throw new Error("Frame too short");
    }

    if (this.getPayloadLengthChecksum() != (length ^ 0xff)) {
      throw new Error("Invalid payload length checksum");
    }

    if (this.getCRC() != this.calculateCRC()) {
      throw new Error("Invalid CRC");
    }

    return true;
  }

  calculateCRC() {
    return this.frame
      .slice(0, this.frame.length - 1)
      .reduce((crc, item) => crc ^ item, 0);
  }

  getID() {
    return this.frame[0];
  }

  getPayloadLength() {
    return this.frame[1];
  }

  getPayloadLengthChecksum() {
    return this.frame[2];
  }

  getPayload() {
    const payloadLength = this.getPayloadLength();

    if (typeof payloadLength === "undefined") {
      throw new Error("Invalid payload length");
    }

    return this.frame.slice(3, 3 + payloadLength - 1);
  }

  getCRC() {
    return this.frame[this.frame.length - 1];
  }
}
