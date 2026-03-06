# CAN frame fields

This document describes the `ID`, `LEN`, `PLC`, `DATA`, and `FLC` fields used by the
project's frame format and explains how the PLC is computed. The implementation
is in `CanFrame.kt`.

## Frame layout (byte order)

1. `ID` — command identifier (first byte)
2. `LEN` — data length code (second byte)
3. `PLC` — payload length checksum (third byte)
4. `DATA` - data bytes
5. `FLC` - frame length checksum (last byte)

In code the parse loop accesses bytes as: `id = bytes[0]`, `len = bytes[1]`,
`plc = bytes[2]`, `data = bytes[3 .. 3 + dlc - 2]`, and then reads `flc = bytes[3 + dlc - 1]`.

## `PLC` (Length Checksum)

- Algorithm: PLC = (0xFF - LEN) & 0xFF

## `FLC` (Length Checksum)

- Algorithm: FLC = bytes.reduce((acc, byte) => acc ^ byte, 0);
