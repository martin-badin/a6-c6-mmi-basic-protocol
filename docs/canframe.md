# CAN frame fields and CRC

This document describes the `ID`, `DLC`, and `CRC` fields used by the
project's frame format and explains how the CRC is computed. The implementation
is in `CanFrame.kt`.

## Frame layout (byte order)

1. `ID` — command identifier (first byte)
2. `DLC` — data length code (second byte): number of payload bytes that follow
3. `CRC` — checksum byte (third byte)
4. `Payload` — `DLC` bytes of data

In code the parse loop accesses bytes as: `id = bytes[0]`, `dlc = bytes[1]`,
`crc = bytes[2]`, and then reads `payload = bytes[3 .. 3 + dlc - 1]`.

## `DLC` (Data Length Code)

- `DLC` is an unsigned byte (`UByte`) that indicates how many bytes are present
  in the payload section for this frame. The parser computes the payload range
  using `fromIndex = index + 3` and `toIndex = dlc.toInt() + fromIndex`.

## `CRC` (Checksum)

The repository implements a simple checksum (CRC) function in
`CanFrame.computeCRC(dlc: UByte): UByte`.

- Algorithm: CRC = 0xFF - DLC
- Equivalent arithmetic: CRC = (255 - DLC) mod 256

Kotlin implementation (from `CanFrame.kt`):

```kotlin
fun computeCRC(dlc: UByte): UByte {
    return (0xFF.toUByte() - dlc).toUByte()
}
```

Example:

- If `DLC = 0x0B` (11 decimal), then `CRC = 0xFF - 0x0B = 0xF4`.

## Control byte (payload XOR)

There is also a helper that computes a control byte from a payload by XOR'ing
all bytes together. This is implemented as `computeControlByte(bytes: UByteArray)`.

Kotlin implementation (from `CanFrame.kt`):

```kotlin
fun computeControlByte(bytes: UByteArray): UByte {
    return bytes.reduce { acc, byte -> acc.xor(byte) }
}
```

This returns the bitwise XOR of every byte in the provided array. It is useful
for simple data integrity checks inside the payload but is distinct from the
frame-level `CRC` computed from `DLC`.

## Notes and usage

- The parser in `CanFrame.parse()` uses `CRC` as stored in the frame but the
  current validation logic is in `CommandModel` (see `CommandModel.kt`) — if
  you add or change checksum rules, update both the computation here and the
  validation code there.
- The CRC scheme is intentionally simple (derived only from length). If you
  plan to use frames in hostile environments consider a stronger checksum (e.g.
  CRC-8/CRC-16) and update both encoder and decoder.
