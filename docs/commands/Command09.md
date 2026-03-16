# Command ID 09 — ACK for Command 04

Short description

This file documents the `0x09` frame used as an acknowledgment for the
`0x04` display-info frame. Implementations observing `0x04` may emit or
expect `0x09` frames to confirm reception or readiness.

Summary

- Direction: host -> device (ACK)
- Purpose: acknowledge receipt of `0x04` and echo the identifying byte
- Key fields: identification byte (first payload byte), status/flags

Frame structure (observed/example)

| ID   | LEN  | PLC  | Payload (example) | CRC  |
| ---- | ---- | ---- | ----------------- | ---- |
| 0x09 | 0x02 | 0xFD | 0x02              | 0xF4 |

Fields

- Identification byte: first payload byte (example `0x02`). This byte is
  the same value as the first payload byte (`0x02`) contained in the
  corresponding `0x04` frame. Implementations should match this to the
  `0x04` instance being acknowledged.

Parsing / rendering notes

- When parsing an incoming `0x09`, verify the identification byte matches
  the previously-received `0x04` payload's `0x02` byte when correlating
  ACKs to earlier frames.
- The exact meaning of the status/flags byte is implementation-specific;
  treat non-zero values as non-success unless a concrete mapping is
  documented elsewhere.

Notes & tips

- `0x09` is a compact ACK frame; it intentionally repeats the key
  identifier from `0x04` so the receiver can correlate asynchronous
  acknowledgements.
- If your parser maintains a queue of outstanding `0x04` frames, match
  incoming `0x09` frames by the identification byte before marking the
  original frame as acknowledged.

See also

- [docs/commands/Command04.md](docs/commands/Command04.md) — original
  display-info frame whose payload's `0x02` is echoed here
