INSERT INTO submission_detail(submission_id,SOURCE)
SELECT solution_id,source FROM submission_source;

UPDATE submission_detail sd
JOIN compileinfo c ON sd.submission_id=c.solution_id
SET sd.compile_info=c.error;

UPDATE submission_detail sd
JOIN solution_details d ON sd.submission_id=d.solution_id
SET sd.detail=d.details;

INSERT IGNORE INTO submission_detail(submission_id,compile_info)
SELECT solution_id,error FROM compileinfo;

INSERT IGNORE INTO submission_detail(submission_id,detail)
SELECT solution_id,details FROM solution_details;

DROP TABLE compileinfo;
DROP TABLE solution_details;
DROP TABLE submission_source;
