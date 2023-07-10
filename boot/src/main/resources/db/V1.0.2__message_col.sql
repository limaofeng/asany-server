BEGIN;
ALTER TABLE `sms_message` ADD `provider` varchar(50);
COMMIT;