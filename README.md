# Penguin meeting room reservation prototype01

会議室予約システムプロトタイプ

## Validation

### Meeting
- subjectは必須
- sujectは3文字以上256文字以下
- meetingRoomは必須
- meetingRoomは1以上
- meetingRoomは存在するものであること
- numAtendeeは必須
- numAtendee1以上50以下
- numAtendeeはmeetingRoomのcapacityを超えない
- startは必須
- 登録時、startは現在時刻より後
- startを変更時、過去に変更できない
- startを過ぎていたら、startを変更できない
- endは必須
- 登録時、endは現在時刻より後
- endを変更時、過去に変更できない
- endを過ぎていたら、endを変更できない
- endはstartより後
- startからendは15分以上2時間以下
- 1つのmeetingRoomで、会議は重複して取れない

## Architecture

### Validation
以下の順でチェックを行う。各ステップでエラーがある場合、後続ステップのチェックは行われない。

1. 入力値のチェック。必須チェックや長さのチェック、フォーマットのチェックなど。外部への接続は行わない
1. 登録しようとしているmodelのField間のチェック。前後関係や条件付き必須チェックなど。外部への接続は行わない
1. DB値を用いたチェック。状態遷移や変更の検知など。外部への接続は行わない
1. 外部への接続(DBやAPI)を用いたチェック。

2.から4.はValidationOrderによって制御
