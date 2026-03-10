```markdown
# Command ID 0A — Host request / block request

Short description

Documents `0x0A` frames sent by the host to request a display block from the device.

Summary

- Direction: host -> device
- Purpose: request transfer of a display block identified by a block id
- Key fields: block id (payload[0]), flags/params

Frame structure (observed examples)

| ID   | LEN  | PLC  | Payload (example)        | CRC  |
| ---- | ---- | ---- | ------------------------ | ---- |
| 0x0A | 0x06 | 0xF9 | 0x03 0x01 0x03 0xDE 0x02 | 0x28 |
| 0x0A | 0x06 | 0xF9 | 0x05 0x02 0x00 0x16 0x04 | 0xE0 |
| 0x0A | 0x06 | 0xF9 | 0x07 0x02 0x00 0x0F 0x05 | 0xFA |

Fields

- Block ID: `payload[0]` — identifies requested block (examples: `0x03`, `0x05`).
- Flags/params: remaining bytes — behavior varies and is not fully decoded.

Parsing & correlation

- Extract `blockId = payload[0]` and preserve any following bytes for
  correlation with subsequent device frames.
- Typical sequence: `0x0A (request N)` -> `0x0D (ACK N)` -> `0x31`/`0x39` (content for N).

See also

- [docs/commands/Command0D.md](docs/commands/Command0D.md) — device ACKs for requested blocks
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel block payloads

Notes

- Optionally add a small parser helper (`commands/Command0A.kt` / JS) to
  extract named fields and annotate logs for easier correlation.
```
