# CAN frame fields

This document describes the `ID`, `LEN`, `PLC`, `DATA`, and `CRC` fields used by the
project's frame format and explains how the PLC is computed. The implementation
is in `CanFrame.kt`.

## Frame layout (byte order)

1. `ID` — command identifier (first byte)
2. `LEN` — data length code (second byte)
3. `PLC` — payload length checksum (third byte)
4. `PAYLOAD` - payload bytes
5. `CRC` - checksum (last byte)

In code the parse loop accesses bytes as: `id = bytes[0]`, `len = bytes[1]`,
`plc = bytes[2]`, and `payload = bytes[3 .. 3 + len - 1]` (the `payload` array includes
the frame's trailing checksum byte). The frame's CRC/checksum is the last
byte of the `payload` array (equivalently `bytes[3 + len - 1]`).

## `PLC` (Payload Length Checksum)

- Algorithm: PLC = (0xFF - LEN) & 0xFF

## `CRC` (Checksum)

-- Algorithm: CRC is the XOR of all preceding frame bytes up to but excluding
the CRC byte. Concretely: CRC = XOR(ID, LEN, PLC, PAYLOAD[0], PAYLOAD[1], ..., PAYLOAD[n-2]).

In code this is implemented as a reduce/xor over the concatenation of
`id`, `payloadLength`, `payloadLengthChecksum` and `payload[0 .. payloadLength-2]`.
