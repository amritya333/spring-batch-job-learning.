# spring-batch-job-learning

Learning Spring Batch: jobs, steps, readers, processors and writers.

## Stack
Java 17 · Spring Boot 3.3 · Spring Batch 5 · H2 (in-memory) · Maven

## What the sample job does
`importPersonJob` has one chunk-oriented step (`importPersonStep`, chunk size 2):

| Stage | Class / bean | Role |
|-------|--------------|------|
| Reader | `FlatFileItemReader` | reads `src/main/resources/people.csv` |
| Processor | `PersonItemProcessor` | upper-cases first and last name |
| Writer | `JdbcBatchItemWriter` | inserts rows into the `person` table |

Spring Batch also stores job/step execution metadata in the `BATCH_*` tables.

## Run
```bash
mvn spring-boot:run     # runs the job on startup
mvn test                # runs ImportPersonJobTest
```

## Learning roadmap
- [x] Job, Step, chunk-oriented processing
- [ ] Job parameters and restartability
- [ ] Skip / retry / fault tolerance
- [ ] Listeners (job, step, chunk)
- [ ] Multi-step flows and conditional transitions
- [ ] Tasklet steps
- [ ] Partitioning and parallel steps
- [ ] Persistent DB (PostgreSQL/MySQL) instead of H2
