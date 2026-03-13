```markdown
# Command ID 0A — Host request / block request

Frames where the host requests a display/content block from the device.

Summary

- Direction: host -> device
- Purpose: request transfer of a display block identified by a block id
- Key fields: block id (payload[0]), mode/flags (payload[1]), params (payload[2..])

Frame structure (observed examples)

| ID   | LEN  | PLC  | Payload (example)        | CRC  |
| ---- | ---- | ---- | ------------------------ | ---- |
| 0x0A | 0x06 | 0xF9 | 0x03 0x01 0x03 0xDE 0x02 | 0x28 |
| 0x0A | 0x06 | 0xF9 | 0x05 0x02 0x00 0x16 0x04 | 0xE0 |
| 0x0A | 0x06 | 0xF9 | 0x07 0x02 0x00 0x0F 0x05 | 0xFA |

Fields

- Block ID: `payload[0]` — identifies requested block (examples: `0x03`, `0x05`).
- Mode / Flags: `payload[1]` — observed small set of values (commonly `0x01` or `0x02`), likely transfer mode or options.
- Param A/B/C: `payload[2..]` — transfer-specific parameters, may include part/index, checks, or other flags.
- CRC: final byte (frame-level CRC, XOR of previous bytes)

Parsing / rendering notes

- Extract `blockId = payload[0]` and preserve following bytes for correlation with subsequent device frames.
- In this capture `payload[1]` is often `0x01` or `0x02` and appears to control the reply form; the remaining bytes vary per request.
- Typical sequence in capture: `to 0x0A (request N)` → `from 0x0D (ACK N)` → `from (0x31|0x39|0x55|0x53|0x3F) (N ... )` where the device sends content for block N.
- Implementation tip: expose `mode` and `params` in your parser to help correlate requests with multi-frame responses.

Notes & tips

- Some device responses contain large payloads split across frames — use the block id to join related frames.
- If you need deterministic decoding, start by extracting `payload[0]` and grouping subsequent `from` frames that begin with that same byte.

See also

- [docs/commands/Command0D.md](docs/commands/Command0D.md) — device ACKs for requested blocks
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel block payloads
```
