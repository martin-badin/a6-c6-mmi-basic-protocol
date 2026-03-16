// | ID   | LEN  | PLC  | Data (example)           | CRC  |
// | ---- | ---- | ---- | ------------------------ | ---- |
// | 0x0A | 0x06 | 0xF9 | 0x03 0x01 0x03 0xDE 0x02 | 0x28 |
export class Command {
  protected frame: Uint8Array;

  constructor(frame: string[]) {
    this.frame = this.toByteArray(frame);
  }

  isValid() {
    const length = this.getPayloadLength();

    if (length === undefined || length < 0) {
      console.error("Invalid payload length:", {
        payloadLength: length,
        frame: this.toRawArray(this.frame),
      });

      throw new Error("Invalid payload length");
    }

    if (this.frame.length != length + 3) {
      console.error("Frame length does not match expected length:", {
        frameLength: this.frame.length,
        expectedLength: length + 3,
        frame: this.toRawArray(this.frame),
      });

      throw new Error("Frame too short");
    }

    if (this.getPayloadLengthChecksum() != (length ^ 0xff)) {
      console.error("Payload length checksum does not match expected value:", {
        payloadLength: length,
        expectedChecksum: this.toRawArray(Uint8Array.from([length ^ 0xff])),
        actualChecksum: this.toRawArray(
          Uint8Array.from([this.getPayloadLengthChecksum() ?? 0]),
        ),
        frame: this.toRawArray(this.frame),
      });

      throw new Error("Invalid payload length checksum");
    }

    if (this.getCRC() != this.calculateCRC()) {
      console.error("CRC does not match expected value:", {
        expectedCRC: this.toRawArray(Uint8Array.from([this.calculateCRC()])),
        actualCRC: this.toRawArray(Uint8Array.from([this.getCRC() ?? 0])),
        frame: this.toRawArray(this.frame),
      });

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

  // Order / sequence byte (4th byte of the full frame). For host->device
  // frames this is commonly used to correlate requests and replies.
  getOrder() {
    return this.frame[3];
  }

  getPayload() {
    const payloadLength = this.getPayloadLength();

    if (payloadLength === undefined) {
      throw new Error("Invalid payload length");
    }

    return this.frame.slice(3, 3 + payloadLength - 1);
  }

  getCRC() {
    return this.frame[this.frame.length - 1];
  }

  toByteArray = (frame: string[]) => {
    return Uint8Array.from(frame.map((x) => parseInt(x, 16)));
  };

  toRawArray = (bytes: Uint8Array) => {
    return Array.from(bytes).map(
      (byte) => `0x${byte.toString(16).padStart(2, "0").toUpperCase()}`,
    );
  };
}
