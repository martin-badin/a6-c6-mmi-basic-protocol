# Command ID 31 — Screen data (pixel blocks)

This file provides a concise, actionable explanation of `0x31` frames and how
to decode and render their payloads.

Summary

- Direction: device -> host
- Purpose: transfer a rectangular block of pixel data to be drawn on the
  display
- Key fields: color type, X, Y, height, width, bitmap bytes

Frame structure (example):

| ID   | DLC  | CRC  | ?         | Color | X    | Y    | height | width | Bitmap                                                                                                                                                                                                                                          | Order |
| ---- | ---- | ---- | --------- | ----- | ---- | ---- | ------ | ----- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| 0x31 | 0x38 | 0xC7 | 0x07 0x01 | 0x00  | 0xC9 | 0x0C | 0x1B   | 0x0C  | 0x03 0x00 0x00 0x00 0x0D 0x00 0x00 0x00 0x31 0x00 0x00 0x00 0xC1 0x00 0x00 0x00 0x07 0x03 0x00 0x00 0x18 0x1C 0x00 0x00 0x60 0x01 0x02 0x18 0x0D 0x37 0xB0 0x62 0x39 0x08 0xF7 0x36 0x01 0x02 0x14 0x17 0x23 0x04 0xD7 0x39 0x08 0xF7 0x37 0x01 | 0x02  |

Bitmap packing rules

- Bytes are packed column-by-column.
- The implementation in `Command31.getBytes()` divides the bitmap bytes into
  `width` columns. For each column it takes `count = bytes.length / width`
  bytes.
- Each byte is converted to an 8-bit binary string and then reversed
  (LSB becomes first). The bytes for a column are concatenated to form a
  single bitstring representing rows from top to bottom.

  Rendering semantics

- Interpret each column bitstring for rows 0..height-1.
- Color rules:
  - `0x00` (both): bit==1 -> red; bit==0 -> black (draw both layers)
  - `0x01` (red-only): bit==1 -> red; bit==0 -> do nothing
  - `0x02` (black-only): bit==1 -> black; bit==0 -> do nothing

Notes & tips

- The byte reversal (per-byte) is crucial — without it vertical alignment will
  be incorrect.
- `0x31` frames are often paired with `0x0A` (host request) and `0x0D` (ACK).
  Use that sequence to correlate which block id the `0x31` contents refer to.
- Some `0x31` frames contain large payloads spanning many columns; always
  compute column count from `bytes.length / width` rather than assuming 8- or
  16-byte columns.

See also

- `commands/Command31.kt` (in this repo) — actual parsing implementation
- `docs/commands/Command0A.md` — host block request
- `docs/commands/Command0D.md` — device ACK for requested block
