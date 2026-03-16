# Command ID 0D — Acknowledge (ACK)

Short description

Documents `0x0D` short acknowledgements sent by the device, commonly echoing
the requested block id.

Summary

- Direction: device -> host
- Purpose: acknowledge a host `0x0A` block request and indicate readiness
- Key fields: acknowledged block id (single-byte payload)

Frame structure (observed example)

| ID   | LEN  | PLC  | Payload (example) | CRC  |
| ---- | ---- | ---- | ----------------- | ---- |
| 0x0D | 0x02 | 0xFD | 0x04              | 0xF6 |

Fields

- Acknowledged block id: single payload byte echoing the requested block id.

Note: host->device request frames include an order/sequence byte at the
4th byte of the full frame (`bytes[3]`) which appears as `payload[0]` in
the local payload indexing. The `0x0D` ACK echoes the requested block
id in its `payload[0]` (i.e. the same value sent by the host in the
4th frame byte).

Behavior & correlation

- Typical sequence: host sends `0x0A` (request) → device replies `0x0D` (ACK) →
  device sends `0x31`/`0x39` payload for the same block id.
- Treat `0x0D` as a ready/ACK indicator when correlating traffic.

See also

- [docs/commands/Command0A.md](docs/commands/Command0A.md) — host block requests
- [docs/commands/Command31.md](docs/commands/Command31.md) — pixel block payloads
