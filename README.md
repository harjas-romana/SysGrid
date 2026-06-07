Yo — this is SysGrid (tiny brain for networks)

Short and messy README cuz who reads these properly anyway.

What it is
- A lil Spring Boot app that keeps an in-memory graph of `Node`s and `Edge`s.
- Exposes a daft endpoint to grab the whole graph: GET /api/graph
- Uses Lombok for model boilerplate so the code stays chill.

Why you'd care
- Wanna mock a system topology quick
- Wanna play with nodes/edges in memory without DB fuss

Key bits (look if you actually care):
- SysGrid app entry: [src/main/java/com/sysgrid/SysGridApplication.java](src/main/java/com/sysgrid/SysGridApplication.java)
- HTTP bits: [src/main/java/com/sysgrid/controller/GraphController.java](src/main/java/com/sysgrid/controller/GraphController.java)
- Business rules: [src/main/java/com/sysgrid/service/GraphService.java](src/main/java/com/sysgrid/service/GraphService.java)
- Model: [src/main/java/com/sysgrid/model/Graph.java](src/main/java/com/sysgrid/model/Graph.java)
- Node & Edge: [src/main/java/com/sysgrid/model/Node.java](src/main/java/com/sysgrid/model/Node.java) and [src/main/java/com/sysgrid/model/Edge.java](src/main/java/com/sysgrid/model/Edge.java)

Running it (fast):
1. Make sure Java is set up
2. From repo root run:

```bash
./gradlew bootRun
```

3. Then hit:

```bash
curl -s http://localhost:8080/api/graph | jq
```

Notes & quirks
- This thing stores the graph in memory — restart and poof, it's gone.
- Lombok is used, so your IDE should have the Lombok plugin or stuff might look weird.
- No auth, no DB, no persistence — it's intentionally dumb and simple.

Wanna add stuff?
- Add endpoints in `GraphController` and call `GraphService`.
- Service already has helpers to create/remove nodes and edges.

License / vibes
- Do whatever. This repo is chill. Don't sue me if it misbehaves.

If you want a less lazy README, lemme know and I'll make it proper.
