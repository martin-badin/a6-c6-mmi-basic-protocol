# Command ID 31 — Screen data (pixel blocks)

Short description

Documents `0x31` frames used to transfer rectangular pixel blocks to the display.

Summary

- Direction: device -> host
- Purpose: transfer a rectangular block of pixel data to be drawn on the display
- Key fields: color type, X, Y, height, width, bitmap bytes

Frame structure (example)

| ID   | LEN  | PLC  | Payload (example)                                                     | CRC  |
| ---- | ---- | ---- | --------------------------------------------------------------------- | ---- |
| 0x31 | 0x0F | 0xF0 | 0x0D 0x01 0x00 0xAE 0x02 0x07 0x07 0x7F 0x20 0x10 0x08 0x10 0x20 0x7F | 0x66 |

Bitmap packing rules

- Bytes are packed column-by-column. The renderer divides the bitmap bytes
  into `width` columns; for each column take `count = bytes.length / width` bytes.
- Each byte is treated LSB-first (the implementation reverses each byte so
  low-order bit corresponds to the top row of the byte segment).

Fields

- Header / reserved bytes: `payload[0..1]` — observed examples show `0x0D 0x01`; meaning not fully identified (may include block id or flags).
- Color: `payload[2]` — value `0x00` = both layers, `0x01` = red-only, `0x02` = black-only.
- X: `payload[3]` — X coordinate (single byte).
- Y: `payload[4]` — Y coordinate (single byte).
- Height: `payload[5]` — rectangle height in pixels (single byte).
- Width: `payload[6]` — rectangle width in pixels (single byte).
- Bitmap bytes: `payload[7..n]` — concatenated column bytes covering `height*width` bits (packed per column).

Rendering semantics

- For each column, interpret the concatenated bitstring for rows 0..height-1.
- Color rules:
  - `0x00` (both): bit==1 -> red; bit==0 -> black (draw both layers)
  - `0x01` (red-only): bit==1 -> red; bit==0 -> do nothing
  - `0x02` (black-only): bit==1 -> black; bit==0 -> do nothing

Notes & tips

- Per-byte reversal is required to match captured frames; omitting it will
  produce vertically misaligned pixels.
- Correlate `0x31` frames with `0x0A`/`0x0D` when identifying which host
  request triggered the block transfer.
- Note: host->device (`to`) frames include an order/sequence byte at the
  4th byte of the full frame (`bytes[3]`), which is `payload[0]` in the
  local payload indexing. Use this order byte when correlating requests
  (`0x0A`) with subsequent `0x31` block transfers.
- Compute column count dynamically from `bytes.length / width` rather than
  assuming fixed column sizes.

Example decode (brief)

- Given the example payload above: header `0x0D 0x01`, `color=0x00`, `x=0xAE`, `y=0x02`, `h=0x07`, `w=0x07`. The remaining 7 bytes are the 7 column byte sequences (after bit reversal) that render a 7x7 bitmap.

See also

- [docs/commands/Command0A.md](docs/commands/Command0A.md) — host block request
- [docs/commands/Command0D.md](docs/commands/Command0D.md) — device ACK for requested block
