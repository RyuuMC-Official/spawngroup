# SpawnGroup

Plugin Paper untuk mengatur `/spawn` per **group world** (cocok dipakai bersama Multiverse-Core).

Contoh kasus:
- Group `survival`: `world`, `world_nether`, `world_the_end` → `/spawn` di world manapun ini akan teleport ke `lobby`.
- Group `rpg`: `rpg_world`, `rpg_world_nether`, `rpg_world_the_end` → `/spawn` di world manapun ini akan teleport ke `lobby_rpg`.

## Cara build

Butuh Java 17+ dan Maven, serta koneksi internet (untuk download Paper API dari repo.papermc.io).

```bash
cd spawngroup
mvn clean package
```

Hasil jar ada di `target/SpawnGroup.jar`. Copy ke folder `plugins/` server Paper kamu, lalu restart/reload server.

## Konfigurasi

Setelah plugin pertama kali dijalankan, akan muncul `plugins/SpawnGroup/config.yml`:

```yaml
groups:
  survival:
    worlds:
      - world
      - world_nether
      - world_the_end
    spawn:
      world: lobby
      x: 0.5
      y: 100.0
      z: 0.5
      yaw: 0.0
      pitch: 0.0

  rpg:
    worlds:
      - rpg_world
      - rpg_world_nether
      - rpg_world_the_end
    spawn:
      world: lobby_rpg
      x: 0.5
      y: 100.0
      z: 0.5
      yaw: 0.0
      pitch: 0.0
```

Tinggal ganti nama-nama world sesuai dengan yang kamu pakai di Multiverse, dan tambah group baru sesuai kebutuhan (bebas berapa pun jumlah group/world).

Cara paling gampang isi koordinat spawn: berdiri di titik yang diinginkan (misal di lobby), lalu jalankan:

```
/spawngroup setspawn survival
/spawngroup setspawn rpg
```

## Command

| Command | Fungsi | Permission |
|---|---|---|
| `/spawn` | Teleport ke spawn group dari world tempat kamu berdiri sekarang | - (semua player) |
| `/spawn <group>` | Teleport langsung ke spawn group tertentu | `spawngroup.spawn.others` (default: op) |
| `/spawngroup list` | Lihat semua group dan status spawn-nya | `spawngroup.admin` (default: op) |
| `/spawngroup setspawn <group>` | Set spawn group = lokasi kamu sekarang | `spawngroup.admin` (default: op) |
| `/spawngroup reload` | Reload config.yml tanpa restart server | `spawngroup.admin` (default: op) |

## Catatan penting

- Plugin ini **tidak** membuat world atau mengatur portal — itu tetap tugas Multiverse-Core / Multiverse-Portals seperti biasa. Plugin ini hanya menentukan tujuan `/spawn` berdasarkan world tempat player berada.
- Nama world di `worlds:` harus **persis sama** dengan nama world di server (case-insensitive, tapi sebaiknya samakan saja).
- Kalau world tujuan spawn (misal `lobby`) belum ter-load saat server start, plugin akan mencatat warning di log dan spawn dianggap "belum diset" sampai kamu jalankan `/spawngroup setspawn` ulang (atau `/spawngroup reload` setelah world tersebut ter-load).
