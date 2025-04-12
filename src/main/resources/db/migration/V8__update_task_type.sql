UPDATE tasks
SET type = 'HABIT',
    start_date = '2025-04-08 00:00:00',
    end_date = '2025-04-30 23:59:59',
    recurrence_days = 23
WHERE title IN ('Reading', 'Workout', 'Journaling');