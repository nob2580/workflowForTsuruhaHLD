SET client_encoding TO 'SJIS';
SET search_path TO :SCHEMA_NAME;

create table batch_haita_seigyo (
  dummy character(1) not null
  , constraint batch_haita_seigyo_PKEY primary key (dummy)
);

create table batch_log (
  serial_no bigserial not null
  , start_time timestamp without time zone not null
  , end_time timestamp without time zone
  , batch_name character varying(50) not null
  , batch_status character varying(1) not null
  , count_name character varying(20) not null
  , count integer
  , batch_kbn character varying(1) not null
  , constraint batch_log_PKEY primary key (serial_no)
);

create table batch_log_invalid_denpyou_log (
  file_name character varying not null
  , denpyou_start_gyou integer not null
  , denpyou_end_gyou integer not null
  , denpyou_date date not null
  , denpyou_bangou character varying(8) not null
  , taishaku_sagaku_kingaku numeric(19) not null
  , gaiyou character varying not null
  , naiyou character varying not null
);

create table batch_log_invalid_file_log (
  file_name character varying not null
  , gyou_no integer not null
  , koumoku_no integer not null
  , koumoku_name character varying not null
  , invalid_value character varying not null
  , error_naiyou character varying not null
);

create table batch_log_invalid_log_himoduke (
  serial_no bigint not null
  , edaban integer not null
  , file_name character varying not null
  , constraint batch_log_invalid_log_himoduke_PKEY primary key (serial_no,edaban)
);

create table bookmark (
  user_id character varying(30) not null
  , denpyou_kbn character varying(4) not null
  , hyouji_jun integer not null
  , memo character varying(160) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bookmark_PKEY primary key (user_id,denpyou_kbn)
);

create table bumon_kamoku_edaban_yosan (
  futan_bumon_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , edaban_code character varying(12) not null
  , futan_bumon_name character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint
  , yosan_01 numeric(19) not null
  , yosan_02 numeric(19) not null
  , yosan_03 numeric(19) not null
  , yosan_03_shu numeric(19) not null
  , yosan_04 numeric(19) not null
  , yosan_05 numeric(19) not null
  , yosan_06 numeric(19) not null
  , yosan_06_shu numeric(19) not null
  , yosan_07 numeric(19) not null
  , yosan_08 numeric(19) not null
  , yosan_09 numeric(19) not null
  , yosan_09_shu numeric(19) not null
  , yosan_10 numeric(19) not null
  , yosan_11 numeric(19) not null
  , yosan_12 numeric(19) not null
  , yosan_12_shu numeric(19) not null
  , constraint bumon_kamoku_edaban_yosan_PKEY primary key (futan_bumon_cd,kamoku_gaibu_cd,kessanki_bangou,edaban_code)
);

create table bumon_kamoku_edaban_zandaka (
  futan_bumon_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , edaban_code character varying(12) not null
  , futan_bumon_name character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint bumon_kamoku_edaban_zandaka_PKEY primary key (futan_bumon_cd,kamoku_gaibu_cd,kessanki_bangou,edaban_code)
);

create table bumon_kamoku_yosan (
  futan_bumon_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , futan_bumon_name character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint
  , yosan_01 numeric(19) not null
  , yosan_02 numeric(19) not null
  , yosan_03 numeric(19) not null
  , yosan_03_shu numeric(19) not null
  , yosan_04 numeric(19) not null
  , yosan_05 numeric(19) not null
  , yosan_06 numeric(19) not null
  , yosan_06_shu numeric(19) not null
  , yosan_07 numeric(19) not null
  , yosan_08 numeric(19) not null
  , yosan_09 numeric(19) not null
  , yosan_09_shu numeric(19) not null
  , yosan_10 numeric(19) not null
  , yosan_11 numeric(19) not null
  , yosan_12 numeric(19) not null
  , yosan_12_shu numeric(19) not null
  , constraint bumon_kamoku_yosan_PKEY primary key (futan_bumon_cd,kamoku_gaibu_cd,kessanki_bangou)
);

create table bumon_kamoku_zandaka (
  futan_bumon_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , futan_bumon_name character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint
  , zandaka_kari00 numeric(19) not null
  , zandaka_kashi00 numeric(19) not null
  , zandaka_kari01 numeric(19) not null
  , zandaka_kashi01 numeric(19) not null
  , zandaka_kari02 numeric(19) not null
  , zandaka_kashi02 numeric(19) not null
  , zandaka_kari03 numeric(19) not null
  , zandaka_kashi03 numeric(19) not null
  , zandaka_kari03_shu numeric(19) not null
  , zandaka_kashi03_shu numeric(19) not null
  , zandaka_kari04 numeric(19) not null
  , zandaka_kashi04 numeric(19) not null
  , zandaka_kari05 numeric(19) not null
  , zandaka_kashi05 numeric(19) not null
  , zandaka_kari06 numeric(19) not null
  , zandaka_kashi06 numeric(19) not null
  , zandaka_kari06_shu numeric(19) not null
  , zandaka_kashi06_shu numeric(19) not null
  , zandaka_kari07 numeric(19) not null
  , zandaka_kashi07 numeric(19) not null
  , zandaka_kari08 numeric(19) not null
  , zandaka_kashi08 numeric(19) not null
  , zandaka_kari09 numeric(19) not null
  , zandaka_kashi09 numeric(19) not null
  , zandaka_kari09_shu numeric(19) not null
  , zandaka_kashi09_shu numeric(19) not null
  , zandaka_kari10 numeric(19) not null
  , zandaka_kashi10 numeric(19) not null
  , zandaka_kari11 numeric(19) not null
  , zandaka_kashi11 numeric(19) not null
  , zandaka_kari12 numeric(19) not null
  , zandaka_kashi12 numeric(19) not null
  , zandaka_kari12_shu numeric(19) not null
  , zandaka_kashi12_shu numeric(19) not null
  , constraint bumon_kamoku_zandaka_PKEY primary key (futan_bumon_cd,kamoku_gaibu_cd,kessanki_bangou)
);

create table bumon_master (
  futan_bumon_cd character varying(8) not null,
  futan_bumon_name character varying(20) not null,
  kessanki_bangou smallint,
  shaukei_bumon_flg smallint not null,
  shiire_kbn smallint,
  nyuryoku_from_date date,
  nyuryoku_to_date date,
  constraint bumon_master_PKEY primary key (futan_bumon_cd)
);

create table bumon_role (
  bumon_role_id character varying(5) not null
  , bumon_role_name character varying not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_role_PKEY primary key (bumon_role_id)
);

create table bumon_suishou_route_ko (
  denpyou_kbn character varying(4) not null
  , bumon_cd character varying(8) not null
  , edano integer not null
  , edaedano integer not null
  , bumon_role_id character varying(5) not null
  , shounin_shori_kengen_no bigint
  , gougi_pattern_no bigint
  , gougi_edano integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_suishou_route_ko_PKEY primary key (denpyou_kbn,bumon_cd,edano,edaedano)
);

create table bumon_suishou_route_oya (
  denpyou_kbn character varying(4) not null
  , bumon_cd character varying(8) not null
  , edano integer not null
  , default_flg character varying(1) not null
  , shiwake_edano integer[]
  , kingaku_from numeric(15)
  , kingaku_to numeric(15)
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint bumon_suishou_route_oya_PKEY primary key (denpyou_kbn,bumon_cd,edano)
);

create table bus_line_master (
  line_cd character varying(6) not null
  , line_name character varying not null
  , constraint bus_line_master_PKEY primary key (line_cd)
);

create table daikou_shitei (
  daikou_user_id character varying(30) not null
  , hi_daikou_user_id character varying(30) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint daikou_shitei_PKEY primary key (daikou_user_id,hi_daikou_user_id)
);

create table denpyou (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , denpyou_joutai character varying(2) not null
  , sanshou_denpyou_id character varying(19) not null
  , daihyou_futan_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , serial_no bigint not null
  , chuushutsu_zumi_flg character varying(1) default '0' not null
  , shounin_route_henkou_flg character varying(1) default '0' not null
  , maruhi_flg character varying(1) default '0' not null
  , yosan_check_nengetsu character varying(6) default '' not null
  , constraint denpyou_PKEY primary key (denpyou_id)
);

create table denpyou_ichiran (
  denpyou_id character varying(19) not null,
  name character varying not null,
  denpyou_kbn character varying(4),
  jisshi_kian_bangou character varying(15) not null,
  shishutsu_kian_bangou character varying(15) not null,
  yosan_shikkou_taishou character varying not null,
  yosan_check_nengetsu character varying(6) not null,
  serial_no bigint,
  denpyou_shubetsu_url character varying(240) not null,
  touroku_time timestamp without time zone,
  bumon_full_name character varying not null,
  user_full_name character varying(50) not null,
  user_id character varying(30) not null,
  denpyou_joutai character varying(2) not null,
  koushin_time timestamp without time zone,
  shouninbi timestamp without time zone,
  maruhi_flg character varying(1) not null,
  all_cnt bigint,
  cur_cnt bigint,
  zan_cnt bigint,
  gen_bumon_full_name character varying not null,
  gen_user_full_name character varying not null,
  gen_gyoumu_role_name character varying not null,
  gen_name character varying not null,
  version integer,
  kingaku numeric(15),
  gaika character varying not null,
  houjin_kingaku numeric(15),
  tehai_kingaku numeric(15),
  torihikisaki1 character varying not null,
  shiharaibi date,
  shiharaikiboubi date,
  shiharaihouhou character varying not null,
  sashihiki_shikyuu_kingaku numeric(15),
  keijoubi date,
  shiwakekeijoubi date,
  seisan_yoteibi date,
  karibarai_denpyou_id character varying(19) not null,
  houmonsaki character varying(200) not null,
  mokuteki character varying(240) not null,
  kenmei character varying not null,
  naiyou character varying not null,
  user_sei character varying(10) not null,
  user_mei character varying(10) not null,
  seisankikan_from date,
  seisankikan_to date,
  gen_user_id character varying not null,
  gen_gyoumu_role_id character varying not null,
  kian_bangou_unyou_flg character varying(1) not null,
  yosan_shikkou_taishou_cd character varying not null,
  kian_syuryou_flg character varying not null,
  futan_bumon_cd character varying[],
  kari_futan_bumon_cd character varying[],
  kari_kamoku_cd character varying[],
  kari_kamoku_edaban_cd character varying[],
  kari_torihikisaki_cd character varying[],
  kashi_futan_bumon_cd character varying[],
  kashi_kamoku_cd character varying[],
  kashi_kamoku_edaban_cd character varying[],
  kashi_torihikisaki_cd character varying[],
  meisai_kingaku numeric(15)[],
  tekiyou character varying not null,
  houjin_card_use character varying(1) not null,
  kaisha_tehai_use character varying(1) not null,
  ryoushuusho_exist character varying not null,
  miseisan_karibarai_exist character varying(1) not null,
  miseisan_ukagai_exist character varying(1) not null,
  shiwake_status character varying not null,
  fb_status character varying not null,
  jisshi_nendo character varying(4) not null,
  shishutsu_nendo character varying(4) not null,
  bumon_cd character varying(8) not null,
  kian_bangou_input character varying(1) not null,
  jigyousha_kbn character varying[],
  shain_no character varying(15),
  shiharai_name character varying[],
  zeinuki_meisai_kingaku numeric[],
  zeinuki_kingaku numeric(15) not null default 0,
  constraint denpyou_ichiran_PKEY primary key (denpyou_id)
);

create table denpyou_id_saiban (
  touroku_date date not null
  , denpyou_kbn character varying(4) not null
  , sequence_val integer
  , constraint denpyou_id_saiban_PKEY primary key (touroku_date,denpyou_kbn)
);

create table denpyou_kian_himozuke (
  denpyou_id character varying(19) not null
  , bumon_cd character varying(8)
  , nendo character varying(4)
  , ryakugou character varying(7)
  , kian_bangou_from integer
  , kian_bangou integer
  , kian_syuryo_flg character varying(1)
  , kian_syuryo_bi date
  , kian_denpyou character varying(19)
  , kian_denpyou_kbn character varying(4)
  , jisshi_nendo character varying(4)
  , jisshi_kian_bangou character varying(15)
  , shishutsu_nendo character varying(4)
  , shishutsu_kian_bangou character varying(15)
  , ringi_kingaku numeric(15)
  , ringi_kingaku_hikitsugimoto_denpyou character varying(19) not null
  , ringi_kingaku_chouka_comment character varying(240) not null
  , constraint denpyou_kian_himozuke_PKEY primary key (denpyou_id)
);

create table denpyou_serial_no_saiban (
  sequence_val integer not null
  , max_value integer not null
  , min_value integer not null
);

create table denpyou_shubetsu_ichiran (
  denpyou_kbn character varying(4) not null
  , version integer default 0 not null
  , denpyou_shubetsu character varying(20) not null
  , denpyou_karibarai_nashi_shubetsu character varying(20)
  , denpyou_print_shubetsu character varying(20)
  , denpyou_print_karibarai_nashi_shubetsu character varying(20)
  , hyouji_jun integer not null
  , gyoumu_shubetsu character varying(20) not null
  , naiyou character varying(160) not null
  , denpyou_shubetsu_url character varying(240) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , kanren_sentaku_flg character varying(1) not null
  , kanren_hyouji_flg character varying(1) not null
  , denpyou_print_flg character varying(1) not null
  , kianbangou_unyou_flg character varying(1) default '0' not null
  , yosan_shikkou_taishou character varying(1) default 'X' not null
  , route_hantei_kingaku character varying(1) default '0' not null
  , route_torihiki_flg character varying(1) default '0' not null
  , shounin_jyoukyou_print_flg character varying(1) default '0' not null
  , shinsei_shori_kengen_name character varying(6) not null
  , shiiresaki_flg character varying(1) default '0' not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint denpyou_shubetsu_ichiran_PKEY primary key (denpyou_kbn)
);

create table ebunsho_data (
  ebunsho_no character varying(19) not null
  , ebunsho_edano integer not null
  , ebunsho_shubetsu integer not null
  , ebunsho_nengappi date
  , ebunsho_kingaku numeric(15)
  , ebunsho_hakkousha character varying(20)
  , ebunsho_hinmei character varying(50) not null
  , constraint ebunsho_data_PKEY primary key (ebunsho_no,ebunsho_edano)
);

create table ebunsho_file (
  denpyou_id character varying(19) not null
  , edano integer not null
  , ebunsho_no character varying(19) not null
  , binary_data bytea not null
  , denshitorihiki_flg character varying(1) not null
  , tsfuyo_flg character varying(1) not null
  , touroku_user_id character varying(30)
  , touroku_time timestamp without time zone
  , constraint ebunsho_file_PKEY primary key (denpyou_id,edano)
);

alter table ebunsho_file add constraint ebunsho_file_ebunsho_no_key
  unique (ebunsho_no) ;

create table eki_master (
  region_cd character varying(3) not null
  , line_cd character varying(3) not null
  , eki_cd character varying(3) not null
  , line_name character varying not null
  , eki_name character varying not null
  , constraint eki_master_PKEY primary key (region_cd,line_cd,eki_cd)
);

create table event_log (
  serial_no bigserial not null
  , start_time timestamp without time zone not null
  , end_time timestamp without time zone
  , user_id character varying(30) not null
  , gamen_id character varying(50) not null
  , event_id character varying(30) not null
  , result character varying(30) not null
  , constraint event_log_PKEY primary key (serial_no)
);

create table extension_setting (
  extension_cd character varying(20) not null
  , extension_flg character varying(1) not null
  , constraint extension_setting_PKEY primary key (extension_cd)
);

create table fb (
  serial_no bigserial not null
  , denpyou_id character varying(19) not null
  , user_id character varying(30) not null
  , fb_status character varying(1) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , shubetsu_cd character varying(2) not null
  , cd_kbn character varying(1) not null
  , kaisha_cd character varying(10) not null
  , kaisha_name_hankana character varying(40) not null
  , furikomi_date date not null
  , moto_kinyuukikan_cd character varying(4) not null
  , moto_kinyuukikan_name_hankana character varying(15) not null
  , moto_kinyuukikan_shiten_cd character varying(3) not null
  , moto_kinyuukikan_shiten_name_hankana character varying(15) not null
  , moto_yokin_shumoku_cd character varying(1) not null
  , moto_kouza_bangou character varying(7) not null
  , saki_kinyuukikan_cd character varying(4) not null
  , saki_kinyuukikan_name_hankana character varying(15) not null
  , saki_kinyuukikan_shiten_cd character varying(3) not null
  , saki_kinyuukikan_shiten_name_hankana character varying(15) not null
  , saki_yokin_shumoku_cd character varying(1) not null
  , saki_kouza_bangou character varying(7) not null
  , saki_kouza_meigi_kana character varying(30) not null
  , kingaku numeric(15) not null
  , shinki_cd character varying(1) not null
  , kokyaku_cd1 character varying(10) not null
  , furikomi_kbn character varying(1) not null
  , constraint fb_PKEY primary key (serial_no)
);

create table furikae (
  denpyou_id character varying(19) not null,
  denpyou_date date not null,
  shouhyou_shorui_flg character varying(1) not null,
  kingaku numeric(15) not null,
  hontai_kingaku numeric(15) not null,
  shouhizeigaku numeric(15),
  kari_zeiritsu numeric(3) not null,
  kari_keigen_zeiritsu_kbn character(1) default 0 not null,
  tekiyou character varying(120) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  bikou character varying(40) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kari_torihikisaki_cd character varying(12) not null,
  kari_torihikisaki_name_ryakushiki character varying(20) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  kashi_torihikisaki_cd character varying(12) not null,
  kashi_torihikisaki_name_ryakushiki character varying(20) not null,
  kari_uf1_cd character varying(20) not null,
  kari_uf1_name_ryakushiki character varying(20) not null,
  kari_uf2_cd character varying(20) not null,
  kari_uf2_name_ryakushiki character varying(20) not null,
  kari_uf3_cd character varying(20) not null,
  kari_uf3_name_ryakushiki character varying(20) not null,
  kari_uf4_cd character varying(20) not null,
  kari_uf4_name_ryakushiki character varying(20) not null,
  kari_uf5_cd character varying(20) not null,
  kari_uf5_name_ryakushiki character varying(20) not null,
  kari_uf6_cd character varying(20) not null,
  kari_uf6_name_ryakushiki character varying(20) not null,
  kari_uf7_cd character varying(20) not null,
  kari_uf7_name_ryakushiki character varying(20) not null,
  kari_uf8_cd character varying(20) not null,
  kari_uf8_name_ryakushiki character varying(20) not null,
  kari_uf9_cd character varying(20) not null,
  kari_uf9_name_ryakushiki character varying(20) not null,
  kari_uf10_cd character varying(20) not null,
  kari_uf10_name_ryakushiki character varying(20) not null,
  kashi_uf1_cd character varying(20) not null,
  kashi_uf1_name_ryakushiki character varying(20) not null,
  kashi_uf2_cd character varying(20) not null,
  kashi_uf2_name_ryakushiki character varying(20) not null,
  kashi_uf3_cd character varying(20) not null,
  kashi_uf3_name_ryakushiki character varying(20) not null,
  kashi_uf4_cd character varying(20) not null,
  kashi_uf4_name_ryakushiki character varying(20) not null,
  kashi_uf5_cd character varying(20) not null,
  kashi_uf5_name_ryakushiki character varying(20) not null,
  kashi_uf6_cd character varying(20) not null,
  kashi_uf6_name_ryakushiki character varying(20) not null,
  kashi_uf7_cd character varying(20) not null,
  kashi_uf7_name_ryakushiki character varying(20) not null,
  kashi_uf8_cd character varying(20) not null,
  kashi_uf8_name_ryakushiki character varying(20) not null,
  kashi_uf9_cd character varying(20) not null,
  kashi_uf9_name_ryakushiki character varying(20) not null,
  kashi_uf10_cd character varying(20) not null,
  kashi_uf10_name_ryakushiki character varying(20) not null,
  kari_project_cd character varying(12) not null,
  kari_project_name character varying(20) not null,
  kari_segment_cd character varying(8) not null,
  kari_segment_name_ryakushiki character varying(20) not null,
  kashi_project_cd character varying(12) not null,
  kashi_project_name character varying(20) not null,
  kashi_segment_cd character varying(8) not null,
  kashi_segment_name_ryakushiki character varying(20) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  kari_bunri_kbn character varying(1),
  kashi_bunri_kbn character varying(1),
  kari_jigyousha_kbn character varying(1) not null default '0',
  kari_shiire_kbn character varying(1) not null default '',
  kari_zeigaku_houshiki character varying(1) not null default '0',
  kashi_jigyousha_kbn character varying(1) not null default '0',
  kashi_shiire_kbn character varying(1) not null default '',
  kashi_zeigaku_houshiki character varying(1) not null default '0',
  invoice_denpyou character varying(1) not null default '1',
  kashi_zeiritsu numeric(3) not null,
  kashi_keigen_zeiritsu_kbn character(1) default 0 not null,
  constraint furikae_PKEY primary key (denpyou_id)
);

create table furikomi_bi_rule_hi (
  kijun_date smallint not null
  , furikomi_date smallint not null
  , constraint furikomi_bi_rule_hi_PKEY primary key (kijun_date)
);

create table furikomi_bi_rule_youbi (
  kijun_weekday smallint not null
  , furikomi_weekday smallint not null
  , constraint furikomi_bi_rule_youbi_PKEY primary key (kijun_weekday)
);

create table gamen_kengen_seigyo (
  gamen_id character varying(50) not null
  , gamen_name character varying(50) not null
  , bumon_shozoku_riyoukanou_flg character varying(1) not null
  , system_kanri_riyoukanou_flg character varying(1) not null
  , workflow_riyoukanou_flg character varying(1) not null
  , kaishasettei_riyoukanou_flg character varying(1) not null
  , keirishori_riyoukanou_flg character varying(1) not null
  , kinou_seigyo_cd character varying(2) not null
  , constraint gamen_kengen_seigyo_PKEY primary key (gamen_id)
);

create table gamen_koumoku_seigyo (
  denpyou_kbn character varying(4) not null
  , koumoku_id character varying not null
  , default_koumoku_name character varying not null
  , koumoku_name character varying not null
  , hyouji_flg character varying(1) not null
  , hissu_flg character varying(1) not null
  , hyouji_seigyo_flg character varying(1) not null
  , pdf_hyouji_flg character varying(1) not null
  , pdf_hyouji_seigyo_flg character varying(1) not null
  , code_output_flg character varying(1) not null
  , code_output_seigyo_flg character varying(1) not null
  , hyouji_jun integer not null
  , constraint gamen_koumoku_seigyo_PKEY primary key (denpyou_kbn,koumoku_id)
);

create table gazou_file (
  serial_no bigserial not null
  , file_name character varying not null
  , file_size bigint not null
  , content_type character varying not null
  , binary_data bytea not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gazou_file_PKEY primary key (serial_no)
);

create table gougi_pattern_ko (
  gougi_pattern_no bigint not null
  , edano integer not null
  , bumon_cd character varying(8) not null
  , bumon_role_id character varying(5) not null
  , shounin_shori_kengen_no bigserial not null
  , shounin_ninzuu_cd character varying(1) not null
  , shounin_ninzuu_hiritsu integer
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gougi_pattern_ko_PKEY primary key (gougi_pattern_no,edano)
);

create table gougi_pattern_oya (
  gougi_pattern_no bigserial not null
  , gougi_name character varying(20) not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gougi_pattern_oya_PKEY primary key (gougi_pattern_no)
);

create table gyoumu_role (
  gyoumu_role_id character varying(5) not null
  , gyoumu_role_name character varying not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gyoumu_role_PKEY primary key (gyoumu_role_id)
);

create table gyoumu_role_kinou_seigyo (
  gyoumu_role_id character varying(5) not null
  , gyoumu_role_kinou_seigyo_cd character varying(2) not null
  , gyoumu_role_kinou_seigyo_kbn character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gyoumu_role_kinou_seigyo_PKEY primary key (gyoumu_role_id,gyoumu_role_kinou_seigyo_cd)
);

create table gyoumu_role_wariate (
  user_id character varying(30) not null
  , gyoumu_role_id character varying(5) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , shori_bumon_cd character varying(8) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint gyoumu_role_wariate_PKEY primary key (user_id,gyoumu_role_id,shori_bumon_cd)
);

create table heishu_master (
  heishu_cd character varying(4) not null
  , currency_unit character varying(20) not null
  , country_name character varying(40)
  , conversion_unit smallint default 1 not null
  , decimal_position smallint default 2 not null
  , availability_flg smallint default 0 not null
  , display_order smallint default 0 not null
  , constraint heishu_master_PKEY primary key (heishu_cd)
);

create table help (
  gamen_id character varying(50) not null
  , help_rich_text character varying(50000) not null
  , touroku_user_id character varying(30)
  , touroku_time timestamp without time zone
  , koushin_user_id character varying(30)
  , koushin_time timestamp without time zone
  , constraint help_PKEY primary key (gamen_id)
);

create table hf1_ichiran (
  hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf1_ichiran_PKEY primary key (hf1_cd)
);

create table hf10_ichiran (
  hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf10_ichiran_PKEY primary key (hf10_cd)
);

create table hf2_ichiran (
  hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf2_ichiran_PKEY primary key (hf2_cd)
);

create table hf3_ichiran (
  hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf3_ichiran_PKEY primary key (hf3_cd)
);

create table hf4_ichiran (
  hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf4_ichiran_PKEY primary key (hf4_cd)
);

create table hf5_ichiran (
  hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf5_ichiran_PKEY primary key (hf5_cd)
);

create table hf6_ichiran (
  hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf6_ichiran_PKEY primary key (hf6_cd)
);

create table hf7_ichiran (
  hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf7_ichiran_PKEY primary key (hf7_cd)
);

create table hf8_ichiran (
  hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf8_ichiran_PKEY primary key (hf8_cd)
);

create table hf9_ichiran (
  hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint hf9_ichiran_PKEY primary key (hf9_cd)
);

create table hizuke_control (
  system_kanri_date date not null
  , fb_data_sakuseibi_flg character varying(1) not null
  , kojinseisan_shiharaibi date
  , kinyuukikan_eigyoubi_flg character varying(1) not null
  , toujitsu_kbn_flg character varying(1) not null
  , keijoubi_flg character varying(1) default '0' not null
  , constraint hizuke_control_PKEY primary key (system_kanri_date)
);

create table houjin_card_import (
  user_id character varying(30) not null
  , houjin_card_shubetsu character varying(1) not null
  , constraint houjin_card_import_PKEY primary key (user_id)
);

create table houjin_card_jouhou (
  card_jouhou_id bigserial not null
  , card_shubetsu character varying(3) not null
  , torikomi_denpyou_id character varying(19) not null
  , busho_cd character varying(15) not null
  , shain_bangou character varying(20) not null
  , shiyousha character varying(30) not null
  , riyoubi date not null
  , kingaku numeric(15) not null
  , card_bangou character varying(16) not null
  , kameiten character varying(60) not null
  , gyoushu_cd character varying(15) not null
  , jyogai_flg character varying(1) not null
  , jyogai_riyuu character varying(60) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint houjin_card_jouhou_PKEY primary key (card_jouhou_id)
);

create table ic_card (
  ic_card_no character varying(16) not null
  , token character varying not null
  , user_id character varying(30) not null
  , constraint ic_card_PKEY primary key (ic_card_no)
);

create table ic_card_rireki (
  ic_card_no character varying(16) not null,
  ic_card_sequence_no character varying(10) not null,
  ic_card_riyoubi date not null,
  tanmatu_cd character varying(6) not null,
  line_cd_from character varying(6) not null,
  line_name_from character varying not null,
  eki_cd_from character varying(6) not null,
  eki_name_from character varying not null,
  line_cd_to character varying(6) not null,
  line_name_to character varying not null,
  eki_cd_to character varying(6) not null,
  eki_name_to character varying not null,
  kingaku numeric(15) not null,
  user_id character varying(30) not null,
  jyogai_flg character varying(1) not null,
  shori_cd character varying(6) not null,
  balance numeric(15) not null,
  all_byte character varying not null,
  constraint ic_card_rireki_pkey PRIMARY KEY (ic_card_no,ic_card_sequence_no)
);

create table info_id_saiban (
  touroku_date date not null
  , sequence_val integer not null
  , constraint info_id_saiban_PKEY primary key (touroku_date)
);

create table information (
  info_id character varying(14) not null
  , tsuuchi_kikan_from date not null
  , tsuuchi_kikan_to date not null
  , tsuuchi_naiyou character varying(3000) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint information_PKEY primary key (info_id)
);

create index information_idx1
  on information(tsuuchi_kikan_from,tsuuchi_kikan_to);

create table information_sort (
  info_id character varying(14) not null
  , hyouji_jun integer
  , constraint information_sort_PKEY primary key (info_id)
);

create table jidouhikiotoshi (
  denpyou_id character varying(19) not null,
  keijoubi date,
  hikiotoshibi date not null,
  shouhyou_shorui_flg character varying(1) not null,
  hontai_kingaku_goukei numeric(15) not null,
  shouhizeigaku_goukei numeric(15) not null,
  shiharai_kingaku_goukei numeric(15) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  nyuryoku_houshiki character varying(1) not null default '0',
  invoice_denpyou character varying(1) not null default '1',
  constraint jidouhikiotoshi_PKEY primary key (denpyou_id)
);

create table jidouhikiotoshi_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  shiwake_edano integer not null,
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  shiharai_kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  jigyousha_kbn character varying(1) not null default '0',
  bunri_kbn character varying(1) not null,
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  constraint jidouhikiotoshi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table jishou (
  jishou_id bigserial not null
  , midashi_id bigint not null
  , jishou_name character varying(20) not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint jishou_PKEY primary key (jishou_id)
);

create table jishou_dpkbn_kanren (
  jishou_id bigint not null
  , denpyou_kbn character varying(4) not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint jishou_dpkbn_kanren_PKEY primary key (jishou_id,denpyou_kbn)
);

create table kaigai_koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , constraint kaigai_koutsuu_shudan_master_PKEY primary key (sort_jun,koutsuu_shudan)
);

create table kaigai_nittou_nado_master (
  shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , yakushoku_cd character varying(10) not null
  , tanka numeric(15)
  , heishu_cd character varying(4) not null
  , currency_unit character varying(20) not null
  , tanka_gaika numeric(19,2)
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , nittou_shukuhakuhi_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , constraint kaigai_nittou_nado_master_PKEY primary key (shubetsu1,shubetsu2,yakushoku_cd)
);

create table kaigai_ryohi_karibarai (
  denpyou_id character varying(19) not null
  , karibarai_on character varying(1) default '0' not null
  , dairiflg character varying(1) not null
  , user_id character varying(30)
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , houmonsaki character varying(200) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(120) not null
  , kingaku numeric(15) not null
  , karibarai_kingaku numeric(15)
  , sashihiki_num numeric(2)
  , sashihiki_tanka numeric(6)
  , sashihiki_num_kaigai numeric(2)
  , sashihiki_tanka_kaigai numeric(6)
  , sashihiki_heishu_cd_kaigai character varying(4) not null
  , sashihiki_rate_kaigai numeric(11,5)
  , sashihiki_tanka_kaigai_gaika numeric(8,2)
  , hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , seisan_kanryoubi date
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohi_karibarai_PKEY primary key (denpyou_id)
);

create table kaigai_ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kaigai_flg character varying(1) not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(120) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , kazei_flg character varying(1) not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);

create table kaigai_ryohi_karibarai_meisai (
  denpyou_id character varying(19) not null
  , kaigai_flg character varying(1) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , haya_flg character varying(1)
  , yasu_flg character varying(1)
  , raku_flg character varying(1)
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , heishu_cd character varying(4)
  , rate numeric(11, 5)
  , gaika numeric(19, 2)
  , currency_unit character varying(20)
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kaigai_ryohi_karibarai_meisai_PKEY primary key (denpyou_id,kaigai_flg,denpyou_edano)
);

create table kaigai_ryohiseisan (
  denpyou_id character varying(19) not null,
  karibarai_denpyou_id character varying(19) not null,
  karibarai_on character varying(1) not null,
  karibarai_mishiyou_flg character varying(1) default '0' not null,
  shucchou_chuushi_flg character varying(1) default '0' not null,
  dairiflg character varying(1) not null,
  user_id character varying(30) not null,
  shain_no character varying(15) not null,
  user_sei character varying(10) not null,
  user_mei character varying(10) not null,
  houmonsaki character varying(200) not null,
  mokuteki character varying(240) not null,
  seisankikan_from date,
  seisankikan_from_hour character varying(2),
  seisankikan_from_min character varying(2),
  seisankikan_to date,
  seisankikan_to_hour character varying(2),
  seisankikan_to_min character varying(2),
  keijoubi date,
  shiharaibi date,
  shiharaikiboubi date,
  shiharaihouhou character varying(1) not null,
  tekiyou character varying(120) not null,
  kaigai_tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  goukei_kingaku numeric(15) not null,
  houjin_card_riyou_kingaku numeric(15) not null,
  kaisha_tehai_kingaku numeric(15) not null,
  sashihiki_shikyuu_kingaku numeric(15) not null,
  sashihiki_num numeric(2),
  sashihiki_tanka numeric(6),
  sashihiki_num_kaigai numeric(2),
  sashihiki_tanka_kaigai numeric(6),
  sashihiki_heishu_cd_kaigai character varying(4) not null,
  sashihiki_rate_kaigai numeric(11,5),
  sashihiki_tanka_kaigai_gaika numeric(8,2),
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  shiwake_edano integer,
  torihiki_name character varying(20) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  ryohi_kazei_flg character varying(1) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  kaigai_shiwake_edano integer,
  kaigai_torihiki_name character varying(20) not null,
  kaigai_kari_futan_bumon_cd character varying(8) not null,
  kaigai_kari_futan_bumon_name character varying(20) not null,
  kaigai_torihikisaki_cd character varying(12) not null,
  kaigai_torihikisaki_name_ryakushiki character varying(20) not null,
  kaigai_kari_kamoku_cd character varying(6) not null,
  kaigai_kari_kamoku_name character varying(22) not null,
  kaigai_kari_kamoku_edaban_cd character varying(12) not null,
  kaigai_kari_kamoku_edaban_name character varying(20) not null,
  kaigai_kari_kazei_kbn character varying(3) not null,
  kaigai_kazei_flg character varying(1) not null,
  kaigai_kashi_futan_bumon_cd character varying(8) not null,
  kaigai_kashi_futan_bumon_name character varying(20) not null,
  kaigai_kashi_kamoku_cd character varying(6) not null,
  kaigai_kashi_kamoku_name character varying(22) not null,
  kaigai_kashi_kamoku_edaban_cd character varying(12) not null,
  kaigai_kashi_kamoku_edaban_name character varying(20) not null,
  kaigai_kashi_kazei_kbn character varying(3) not null,
  kaigai_uf1_cd character varying(20) not null,
  kaigai_uf1_name_ryakushiki character varying(20) not null,
  kaigai_uf2_cd character varying(20) not null,
  kaigai_uf2_name_ryakushiki character varying(20) not null,
  kaigai_uf3_cd character varying(20) not null,
  kaigai_uf3_name_ryakushiki character varying(20) not null,
  kaigai_uf4_cd character varying(20) not null,
  kaigai_uf4_name_ryakushiki character varying(20) not null,
  kaigai_uf5_cd character varying(20) not null,
  kaigai_uf5_name_ryakushiki character varying(20) not null,
  kaigai_uf6_cd character varying(20) not null,
  kaigai_uf6_name_ryakushiki character varying(20) not null,
  kaigai_uf7_cd character varying(20) not null,
  kaigai_uf7_name_ryakushiki character varying(20) not null,
  kaigai_uf8_cd character varying(20) not null,
  kaigai_uf8_name_ryakushiki character varying(20) not null,
  kaigai_uf9_cd character varying(20) not null,
  kaigai_uf9_name_ryakushiki character varying(20) not null,
  kaigai_uf10_cd character varying(20) not null,
  kaigai_uf10_name_ryakushiki character varying(20) not null,
  kaigai_project_cd character varying(12) not null,
  kaigai_project_name character varying(20) not null,
  kaigai_segment_cd character varying(8) not null,
  kaigai_segment_name_ryakushiki character varying(20) not null,
  kaigai_tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  kaigai_bunri_kbn character varying(1),
  kaigai_kari_shiire_kbn character varying(1) not null default '',
  kaigai_kashi_shiire_kbn character varying(1) not null default '',
  invoice_denpyou character varying(1) not null default '1',
  constraint kaigai_ryohiseisan_PKEY primary key (denpyou_id)
);

create table kaigai_ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  kaigai_flg character varying(1) not null,
  shiwake_edano integer not null,
  shiyoubi date,
  shouhyou_shorui_flg character varying(1),
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  kazei_flg character varying(1) not null,
  shiharai_kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  kousaihi_shousai_hyouji_flg character varying(1) not null,
  kousaihi_ninzuu_riyou_flg character varying(1) not null,
  kousaihi_shousai character varying(240),
  kousaihi_ninzuu integer,
  kousaihi_hitori_kingaku numeric(15),
  heishu_cd character varying(4),
  rate numeric(11, 5),
  gaika numeric(19, 2),
  currency_unit character varying(20),
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  constraint kaigai_ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano,kaigai_flg)
);

create table kaigai_ryohiseisan_meisai (
  denpyou_id character varying(19) not null,
  kaigai_flg character varying(1) not null,
  denpyou_edano integer not null,
  kikan_from date not null,
  kikan_to date,
  kyuujitsu_nissuu numeric,
  shubetsu_cd character varying(1) not null,
  shubetsu1 character varying(20) not null,
  shubetsu2 character varying(20) not null,
  haya_flg character varying(1),
  yasu_flg character varying(1),
  raku_flg character varying(1),
  koutsuu_shudan character varying(10) not null,
  shouhyou_shorui_hissu_flg character varying(1) not null,
  ryoushuusho_seikyuusho_tou_flg character(1) not null,
  naiyou character varying(512) not null,
  bikou character varying not null,
  oufuku_flg character varying(1) not null,
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  jidounyuuryoku_flg character varying(1) not null,
  nissuu numeric,
  heishu_cd character varying(4),
  rate numeric(11, 5),
  gaika numeric(19, 2),
  currency_unit character varying(20),
  tanka numeric(15, 3) not null,
  suuryou_nyuryoku_type character varying(1) not null,
  suuryou numeric(15, 2),
  suuryou_kigou character varying(5),
  meisai_kingaku numeric(15) not null,
  zei_kubun character varying(1),
  kazei_flg character varying(1),
  ic_card_no character varying(16) not null,
  ic_card_sequence_no character varying(10) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  zeinuki_kingaku numeric(15) not null default 0,
  shouhizeigaku numeric(15) not null default 0,
  zeigaku_fix_flg character varying(1) not null default '0',
  constraint kaigai_ryohiseisan_meisai_PKEY primary key (denpyou_id,kaigai_flg,denpyou_edano)
);

create table kaisha_info (
  kessanki_bangou smallint not null
  , hf1_shiyou_flg smallint not null
  , hf1_hissu_flg character varying(1) not null
  , hf1_name character varying not null
  , hf2_shiyou_flg smallint not null
  , hf2_hissu_flg character varying(1) not null
  , hf2_name character varying not null
  , hf3_shiyou_flg smallint not null
  , hf3_hissu_flg character varying(1) not null
  , hf3_name character varying not null
  , hf4_shiyou_flg smallint not null
  , hf4_hissu_flg character varying(1) not null
  , hf4_name character varying not null
  , hf5_shiyou_flg smallint not null
  , hf5_hissu_flg character varying(1) not null
  , hf5_name character varying not null
  , hf6_shiyou_flg smallint not null
  , hf6_hissu_flg character varying(1) not null
  , hf6_name character varying not null
  , hf7_shiyou_flg smallint not null
  , hf7_hissu_flg character varying(1) not null
  , hf7_name character varying not null
  , hf8_shiyou_flg smallint not null
  , hf8_hissu_flg character varying(1) not null
  , hf8_name character varying not null
  , hf9_shiyou_flg smallint not null
  , hf9_hissu_flg character varying(1) not null
  , hf9_name character varying not null
  , hf10_shiyou_flg smallint not null
  , hf10_hissu_flg character varying(1) not null
  , hf10_name character varying not null
  , uf1_shiyou_flg smallint not null
  , uf1_name character varying not null
  , uf2_shiyou_flg smallint not null
  , uf2_name character varying not null
  , uf3_shiyou_flg smallint not null
  , uf3_name character varying not null
  , uf4_shiyou_flg smallint not null
  , uf4_name character varying not null
  , uf5_shiyou_flg smallint not null
  , uf5_name character varying not null
  , uf6_shiyou_flg smallint not null
  , uf6_name character varying not null
  , uf7_shiyou_flg smallint not null
  , uf7_name character varying not null
  , uf8_shiyou_flg smallint not null
  , uf8_name character varying not null
  , uf9_shiyou_flg smallint not null
  , uf9_name character varying not null
  , uf10_shiyou_flg smallint not null
  , uf10_name character varying not null
  , uf_kotei1_shiyou_flg smallint not null
  , uf_kotei1_name character varying not null
  , uf_kotei2_shiyou_flg smallint not null
  , uf_kotei2_name character varying not null
  , uf_kotei3_shiyou_flg smallint not null
  , uf_kotei3_name character varying not null
  , uf_kotei4_shiyou_flg smallint not null
  , uf_kotei4_name character varying not null
  , uf_kotei5_shiyou_flg smallint not null
  , uf_kotei5_name character varying not null
  , uf_kotei6_shiyou_flg smallint not null
  , uf_kotei6_name character varying not null
  , uf_kotei7_shiyou_flg smallint not null
  , uf_kotei7_name character varying not null
  , uf_kotei8_shiyou_flg smallint not null
  , uf_kotei8_name character varying not null
  , uf_kotei9_shiyou_flg smallint not null
  , uf_kotei9_name character varying not null
  , uf_kotei10_shiyou_flg smallint not null
  , uf_kotei10_name character varying not null
  , pjcd_shiyou_flg smallint not null
  , sgcd_shiyou_flg smallint not null
  , saimu_shiyou_flg character varying(1) not null
  , kamoku_cd_type smallint not null
  , kamoku_edaban_cd_type smallint not null
  , futan_bumon_cd_type smallint not null
  , torihikisaki_cd_type smallint not null
  , segment_cd_type smallint not null
  , houjin_bangou character varying(13) not null
  , keigen_umu_flg character varying(1) not null
);

create table kamoku_edaban_zandaka (
  kamoku_gaibu_cd character varying(8) not null,
  kamoku_edaban_cd character varying(12) not null,
  kamoku_naibu_cd character varying(15) not null,
  kamoku_name_ryakushiki character varying(22) not null,
  kamoku_name_seishiki character varying(40) not null,
  edaban_name character varying(20) not null,
  kessanki_bangou smallint,
  chouhyou_shaturyoku_no smallint,
  taishaku_zokusei smallint not null,
  kishu_zandaka numeric(19) not null,
  kazei_kbn smallint,
  bunri_kbn smallint,
  constraint kamoku_edaban_zandaka_PKEY primary key (kamoku_gaibu_cd,kamoku_edaban_cd)
);

create table kamoku_master (
  kamoku_gaibu_cd character varying(8) not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , kessanki_bangou smallint
  , chouhyou_shaturyoku_no smallint
  , taishaku_zokusei smallint
  , kamoku_group_kbn smallint
  , kamoku_group_bangou smallint
  , shori_group smallint
  , taikakingaku_nyuuryoku_flg smallint
  , kazei_kbn smallint
  , bunri_kbn smallint
  , shiire_kbn smallint
  , gyousha_kbn smallint
  , zeiritsu_kbn smallint
  , tokusha_hyouji_kbn smallint
  , edaban_minyuuryoku_check smallint
  , torihikisaki_minyuuryoku_check smallint
  , bumon_minyuuryoku_check smallint
  , bumon_edaban_flg smallint
  , segment_minyuuryoku_check smallint
  , project_minyuuryoku_check smallint
  , uf1_minyuuryoku_check smallint
  , uf2_minyuuryoku_check smallint
  , uf3_minyuuryoku_check smallint
  , uf4_minyuuryoku_check smallint
  , uf5_minyuuryoku_check smallint
  , uf6_minyuuryoku_check smallint
  , uf7_minyuuryoku_check smallint
  , uf8_minyuuryoku_check smallint
  , uf9_minyuuryoku_check smallint
  , uf10_minyuuryoku_check smallint
  , uf_kotei1_minyuuryoku_check smallint
  , uf_kotei2_minyuuryoku_check smallint
  , uf_kotei3_minyuuryoku_check smallint
  , uf_kotei4_minyuuryoku_check smallint
  , uf_kotei5_minyuuryoku_check smallint
  , uf_kotei6_minyuuryoku_check smallint
  , uf_kotei7_minyuuryoku_check smallint
  , uf_kotei8_minyuuryoku_check smallint
  , uf_kotei9_minyuuryoku_check smallint
  , uf_kotei10_minyuuryoku_check smallint
  , kouji_minyuuryoku_check smallint
  , kousha_minyuuryoku_check smallint
  , tekiyou_cd_minyuuryoku_check smallint
  , bumon_torhikisaki_kamoku_flg smallint
  , bumon_torihikisaki_edaban_shiyou_flg smallint
  , torihikisaki_kamoku_edaban_flg smallint
  , segment_torihikisaki_kamoku_flg smallint
  , karikanjou_keshikomi_no_flg smallint
  , gaika_flg smallint
  , constraint kamoku_master_PKEY primary key (kamoku_gaibu_cd)
);

create table kani_todoke (
  denpyou_id character varying(19) not null
  , denpyou_kbn character varying(4) not null
  , version integer not null
  , item_name character varying not null
  , value1 character varying not null
  , value2 character varying not null
  , touroku_user_id character varying(30)
  , touroku_time timestamp without time zone
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_PKEY primary key (denpyou_id,item_name)
);

create table kani_todoke_checkbox (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , label_name character varying not null
  , hissu_flg character varying not null
  , checkbox_label_name character varying
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_checkbox_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_ichiran (
  denpyou_id character varying(19) not null
  , shinsei01 character varying not null
  , shinsei02 character varying not null
  , shinsei03 character varying not null
  , shinsei04 character varying not null
  , shinsei05 character varying not null
  , shinsei06 character varying not null
  , shinsei07 character varying not null
  , shinsei08 character varying not null
  , shinsei09 character varying not null
  , shinsei10 character varying not null
  , shinsei11 character varying not null
  , shinsei12 character varying not null
  , shinsei13 character varying not null
  , shinsei14 character varying not null
  , shinsei15 character varying not null
  , shinsei16 character varying not null
  , shinsei17 character varying not null
  , shinsei18 character varying not null
  , shinsei19 character varying not null
  , shinsei20 character varying not null
  , shinsei21 character varying not null
  , shinsei22 character varying not null
  , shinsei23 character varying not null
  , shinsei24 character varying not null
  , shinsei25 character varying not null
  , shinsei26 character varying not null
  , shinsei27 character varying not null
  , shinsei28 character varying not null
  , shinsei29 character varying not null
  , shinsei30 character varying not null
  , constraint kani_todoke_ichiran_PKEY primary key (denpyou_id)
);

create table kani_todoke_koumoku (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , hyouji_jun integer not null
  , buhin_type character varying not null
  , default_value1 character varying not null
  , default_value2 character varying not null
  , yosan_shikkou_koumoku_id character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_koumoku_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_list_ko (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , hyouji_jun integer not null
  , text character varying not null
  , value character varying not null
  , select_item character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_list_ko_PKEY primary key (denpyou_kbn,version,area_kbn,item_name,hyouji_jun)
);

create table kani_todoke_list_oya (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , label_name character varying not null
  , hissu_flg character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_list_oya_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_master (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , label_name character varying not null
  , hissu_flg character varying not null
  , master_kbn character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_master_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , item_name character varying not null
  , value1 character varying not null
  , value2 character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_meisai_PKEY primary key (denpyou_id,denpyou_edano,item_name)
);

create table kani_todoke_meisai_ichiran (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , meisai01 character varying not null
  , meisai02 character varying not null
  , meisai03 character varying not null
  , meisai04 character varying not null
  , meisai05 character varying not null
  , meisai06 character varying not null
  , meisai07 character varying not null
  , meisai08 character varying not null
  , meisai09 character varying not null
  , meisai10 character varying not null
  , meisai11 character varying not null
  , meisai12 character varying not null
  , meisai13 character varying not null
  , meisai14 character varying not null
  , meisai15 character varying not null
  , meisai16 character varying not null
  , meisai17 character varying not null
  , meisai18 character varying not null
  , meisai19 character varying not null
  , meisai20 character varying not null
  , meisai21 character varying not null
  , meisai22 character varying not null
  , meisai23 character varying not null
  , meisai24 character varying not null
  , meisai25 character varying not null
  , meisai26 character varying not null
  , meisai27 character varying not null
  , meisai28 character varying not null
  , meisai29 character varying not null
  , meisai30 character varying not null
  , constraint kani_todoke_meisai_ichiran_PKEY primary key (denpyou_id,denpyou_edano)
);

create table kani_todoke_meta (
  denpyou_kbn character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_meta_PKEY primary key (denpyou_kbn)
);

create table kani_todoke_select_item (
  select_item character varying not null
  , cd character varying not null
  , name character varying not null
  , constraint kani_todoke_select_item_PKEY primary key (select_item,cd)
);

create table kani_todoke_summary (
  denpyou_id character varying(19) not null
  , ringi_kingaku numeric(15)
  , shishutsu_kingaku_goukei numeric(15)
  , shuunyuu_kingaku_goukei numeric(15)
  , kenmei character varying not null
  , naiyou character varying not null
  , constraint kani_todoke_summary_PKEY primary key (denpyou_id)
);

create table kani_todoke_text (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , label_name character varying not null
  , hissu_flg character varying not null
  , buhin_format character varying not null
  , buhin_width character varying not null
  , max_length integer not null
  , decimal_point character varying not null
  , kotei_hyouji character varying not null
  , min_value numeric(15) not null
  , max_value numeric(15) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_text_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_textarea (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , area_kbn character varying not null
  , item_name character varying not null
  , label_name character varying not null
  , hissu_flg character varying not null
  , buhin_width character varying not null
  , buhin_height character varying not null
  , max_length integer not null
  , kotei_hyouji character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_textarea_PKEY primary key (denpyou_kbn,version,area_kbn,item_name)
);

create table kani_todoke_version (
  denpyou_kbn character varying(4) not null
  , version integer not null
  , denpyou_shubetsu character varying(20) not null
  , naiyou character varying(160) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint kani_todoke_version_PKEY primary key (denpyou_kbn,version)
);

create table kanren_denpyou (
  denpyou_id character varying(19) not null
  , kanren_denpyou character varying(19) not null
  , kanren_denpyou_kbn character varying(4) not null
  , kanren_denpyou_kihyoubi date not null
  , kanren_denpyou_shouninbi date not null
  , constraint kanren_denpyou_PKEY primary key (denpyou_id,kanren_denpyou)
);

create table karibarai (
  denpyou_id character varying(19) not null
  , seisan_yoteibi date
  , seisan_kanryoubi date
  , shiharaibi date
  , karibarai_on character varying(1)
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(120) not null
  , kingaku numeric(15)
  , karibarai_kingaku numeric(15)
  , hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint karibarai_PKEY primary key (denpyou_id)
);

create table keihiseisan (
  denpyou_id character varying(19) not null
  , karibarai_denpyou_id character varying(19) not null
  , karibarai_on character varying(1)
  , karibarai_mishiyou_flg character varying(1) default '0' not null
  , dairiflg character varying(1) default '0' not null
  , keijoubi date
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , hontai_kingaku_goukei numeric(15) not null
  , shouhizeigaku_goukei numeric(15) not null
  , shiharai_kingaku_goukei numeric(15) not null
  , houjin_card_riyou_kingaku numeric(15) not null
  , kaisha_tehai_kingaku numeric(15) not null
  , sashihiki_shikyuu_kingaku numeric(15) not null
  , hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , hosoku character varying(240) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , invoice_denpyou character varying(1) not null default '1'
  , constraint keihiseisan_PKEY primary key (denpyou_id)
);

create table keihiseisan_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  shiwake_edano integer not null,
  user_id character varying(30) not null,
  shain_no character varying(15) not null,
  user_sei character varying(10) not null,
  user_mei character varying(10) not null,
  shiyoubi date,
  shouhyou_shorui_flg character varying(1),
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  shiharai_kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  kousaihi_shousai_hyouji_flg character varying(1) not null,
  kousaihi_ninzuu_riyou_flg character varying(1) not null,
  kousaihi_shousai character varying(240),
  kousaihi_ninzuu integer,
  kousaihi_hitori_kingaku numeric(15),
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  jigyousha_kbn character varying(1) not null default '0',
  shiharaisaki_name character varying(60),
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  constraint keihiseisan_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table keijoubi_shimebi (
  denpyou_kbn character varying(4) not null
  , shimebi date not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint keijoubi_shimebi_PKEY primary key (denpyou_kbn,shimebi)
);

create table ki_bumon (
  kesn smallint not null,
  futan_bumon_cd character varying(8) not null,
  futan_bumon_name character varying(20) not null,
  oya_syuukei_bumon_cd character varying(8) not null,
  shiire_kbn smallint,
  nyuryoku_from_date date,
  nyuryoku_to_date date,
  constraint ki_bumon_PKEY primary key (kesn,futan_bumon_cd,oya_syuukei_bumon_cd)
);

create table ki_bumon_security (
  kesn smallint not null
  , sptn integer not null
  , futan_bumon_cd character varying(8) not null
  , constraint ki_bumon_security_PKEY primary key (kesn,sptn,futan_bumon_cd)
);

create table ki_busho_shiwake (
  kesn smallint not null
  , dkei smallint not null
  , dseq integer not null
  , sseq integer not null
  , dymd date not null
  , dcno character varying(8) not null
  , valu numeric(15) not null
  , rkmk character varying(15) not null
  , reda character varying(12) not null
  , rbmn character varying(8) not null
  , skmk character varying(15) not null
  , seda character varying(12) not null
  , sbmn character varying(8) not null
  , constraint ki_busho_shiwake_PKEY primary key (kesn,dkei,dseq,sseq)
);

create table ki_kamoku (
  kesn smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , taishaku_zokusei smallint not null
  , constraint ki_kamoku_PKEY primary key (kesn,kamoku_naibu_cd)
);

create table ki_kamoku_edaban (
  kesn smallint not null
  , kamoku_naibu_cd character varying(15) not null
  , kamoku_edaban_cd character varying(12) not null
  , edaban_name character varying(20) not null
  , constraint ki_kamoku_edaban_PKEY primary key (kesn,kamoku_naibu_cd,kamoku_edaban_cd)
);

create table ki_kamoku_security (
  kesn smallint not null
  , sptn integer not null
  , kamoku_naibu_cd character varying(15) not null
  , constraint ki_kamoku_security_PKEY primary key (kesn,sptn,kamoku_naibu_cd)
);

create table ki_kesn (
  kesn smallint not null
  , kessanki_bangou smallint not null
  , from_date date not null
  , to_date date not null
  , constraint ki_kesn_PKEY primary key (kesn,kessanki_bangou,from_date,to_date)
);

create table ki_shiwake (
  kesn smallint not null
  , dkei smallint not null
  , dseq integer not null
  , sseq integer not null
  , dymd date not null
  , dcno character varying(8) not null
  , valu numeric(15) not null
  , rkmk character varying(15) not null
  , reda character varying(12) not null
  , rbmn character varying(8) not null
  , rtky character varying(120) not null
  , rtor character varying(12) not null
  , skmk character varying(15) not null
  , seda character varying(12) not null
  , sbmn character varying(8) not null
  , stky character varying(120) not null
  , stor character varying(12) not null
  , fway smallint not null
  , constraint ki_shiwake_PKEY primary key (kesn,dkei,dseq,sseq)
);

-- �i���ʁj����Őݒ�e�[�u���̍쐬
create table if not exists ki_shouhizei_setting (
  kesn smallint not null,
  shiire_zeigaku_anbun_flg smallint not null,
  shouhizei_kbn smallint not null,
  hasuu_shori_flg smallint not null,
  zeigaku_keisan_flg smallint not null,
  shouhizeitaishou_minyuryoku_flg smallint not null,
  shisan smallint not null default 0,
  uriage smallint not null default 0,
  shiire smallint not null default 0,
  keihi smallint not null default 0,
  bumonbetsu_shori smallint not null default 0,
  tokuteishiire smallint not null default 0,
  zero_shouhizei smallint not null default 0,
  shouhizei_bumon smallint not null default 0,
  shouhizei_torihikisaki smallint not null default 0,
  shouhizei_edaban smallint not null default 0,
  shouhizei_project smallint not null default 0,
  shouhizei_segment smallint not null default 0,
  shouhizei_uf1 smallint not null default 0,
  shouhizei_uf2 smallint not null default 0,
  shouhizei_uf3 smallint not null default 0,
  shouhizei_kouji smallint not null default 0,
  shouhizei_koushu smallint not null default 0,
  ukeshouhizei_uriage character varying(15) not null default '',
  ukeshouhizei_shisan character varying(15) not null default '',
  haraishouhizei_shiire character varying(15) not null default '',
  haraishouhizei_keihi character varying(15) not null default '',
  haraisyouhizei_shisan character varying(15) not null default '',
  uriagezeigaku_keisan smallint not null default 0,
  shiirezeigaku_keisan smallint not null default 0,
  shiirezeigaku_keikasothi smallint not null default 0,
  constraint ki_shouhizei_setting_pkey PRIMARY KEY (kesn)
);

create table ki_syuukei_bumon (
  kesn smallint not null
  , syuukei_bumon_cd character varying(8) not null
  , futan_bumon_cd character varying(8) not null
  , syuukei_bumon_name character varying(20) not null
  , futan_bumon_name character varying(20) not null
  , constraint ki_syuukei_bumon_PKEY primary key (kesn,syuukei_bumon_cd,futan_bumon_cd)
);

create table kian_bangou_bo (
  bumon_cd character varying(8) not null
  , nendo character varying(4) not null
  , ryakugou character varying(7) not null
  , kian_bangou_from integer default 1 not null
  , kian_bangou_to integer default 999999 not null
  , kbn_naiyou character varying not null
  , kianbangou_bo_sentaku_hyouji_flg character varying(1) default '1' not null
  , denpyou_kensaku_hyouji_flg character varying(1) default '1' not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , hyouji_jun integer not null
  , constraint kian_bangou_bo_PKEY primary key (bumon_cd,nendo,ryakugou,kian_bangou_from)
);

create table kian_bangou_saiban (
  bumon_cd character varying(8) not null
  , nendo character varying(4) not null
  , ryakugou character varying(7) not null
  , kian_bangou_from integer not null
  , kian_bangou_last integer not null
  , constraint kian_bangou_saiban_PKEY primary key (bumon_cd,nendo,ryakugou,kian_bangou_from)
);

create table kinou_seigyo (
  kinou_seigyo_cd character varying(2) not null
  , kinou_seigyo_kbn character varying(1) not null
  , constraint kinou_seigyo_PKEY primary key (kinou_seigyo_cd)
);

create table kinyuukikan (
  kinyuukikan_cd character varying(4) not null
  , kinyuukikan_shiten_cd character varying(3) not null
  , kinyuukikan_name_hankana character varying(15) not null
  , kinyuukikan_name_kana character varying(30) not null
  , shiten_name_hankana character varying(15) not null
  , shiten_name_kana character varying(30) not null
  , constraint kinyuukikan_PKEY primary key (kinyuukikan_cd,kinyuukikan_shiten_cd)
);

create table koutsuu_shudan_master (
  sort_jun character(3) not null
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , tanka numeric(15, 3)
  , suuryou_kigou character varying(5)
  , constraint koutsuu_shudan_master_PKEY primary key (sort_jun,koutsuu_shudan)
);

create table koutsuuhiseisan (
  denpyou_id character varying(19) not null,
  mokuteki character varying(240) not null,
  seisankikan_from date,
  seisankikan_from_hour character varying(2),
  seisankikan_from_min character varying(2),
  seisankikan_to date,
  seisankikan_to_hour character varying(2),
  seisankikan_to_min character varying(2),
  keijoubi date,
  shiharaibi date,
  shiharaikiboubi date,
  shiharaihouhou character varying(1) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  goukei_kingaku numeric(15) not null,
  houjin_card_riyou_kingaku numeric(15) not null,
  kaisha_tehai_kingaku numeric(15) not null,
  sashihiki_shikyuu_kingaku numeric(15) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  shiwake_edano integer,
  torihiki_name character varying(20) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  invoice_denpyou character varying(1) not null default '1',
  constraint koutsuuhiseisan_PKEY primary key (denpyou_id)
);

create table koutsuuhiseisan_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  kikan_from date not null,
  shubetsu_cd character varying(1) not null,
  shubetsu1 character varying(20) not null,
  shubetsu2 character varying(20) not null,
  haya_flg character varying(1),
  yasu_flg character varying(1),
  raku_flg character varying(1),
  koutsuu_shudan character varying(10) not null,
  shouhyou_shorui_hissu_flg character varying(1) not null,
  ryoushuusho_seikyuusho_tou_flg character(1) not null,
  naiyou character varying(512) not null,
  bikou character varying not null,
  oufuku_flg character varying(1) not null,
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  jidounyuuryoku_flg character varying(1) not null,
  tanka numeric(15, 3) not null,
  suuryou_nyuryoku_type character varying(1) not null,
  suuryou numeric(15, 2),
  suuryou_kigou character varying(5),
  meisai_kingaku numeric(15) not null,
  ic_card_no character varying(16) not null,
  ic_card_sequence_no character varying(10) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  zeinuki_kingaku numeric(15) not null default 0,
  shouhizeigaku numeric(15) not null default 0,
  zeigaku_fix_flg character varying(1) not null default '0',
  constraint koutsuuhiseisan_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table list_item_control (
  kbn character varying(1) not null
  , user_id character varying(30) not null
  , gyoumu_role_id character varying(5) not null
  , index integer not null
  , name character varying(50) not null
  , display_flg character varying(1) default '1' not null
  , constraint list_item_control_PKEY primary key (kbn,user_id,gyoumu_role_id,index)
);

create table mail_settei (
  smtp_server_name character varying not null
  , port_no character varying not null
  , ninshou_houhou character varying(2) not null
  , angouka_houhou character varying not null
  , mail_address character varying(50) not null
  , mail_id character varying not null
  , mail_password character varying not null
);

create table mail_tsuuchi (
  user_id character varying(30) not null
  , tsuuchi_kbn character varying(3) not null
  , soushinumu character varying(1) not null
  , constraint mail_tsuuchi_PKEY primary key (user_id,tsuuchi_kbn)
);

create table master_kanri_hansuu (
  master_id character varying(50) not null
  , version integer not null
  , delete_flg character varying(1) not null
  , file_name character varying not null
  , file_size bigint not null
  , content_type character varying not null
  , binary_data bytea not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint master_kanri_hansuu_PKEY primary key (master_id,version)
);

create table master_kanri_ichiran (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , henkou_kahi_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint master_kanri_ichiran_PKEY primary key (master_id)
);

create table master_torikomi_ichiran_de3 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_ichiran_de3_PKEY primary key (master_id)
);

create table master_torikomi_ichiran_mk2 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_ichiran_mk2_PKEY primary key (master_id)
);

create table master_torikomi_ichiran_sias (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_ichiran_sias_PKEY primary key (master_id)
);

create table master_torikomi_shousai_de3 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_shousai_de3_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_shousai_mk2 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_shousai_mk2_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_shousai_sias (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_shousai_sias_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_term_ichiran_de3 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_term_ichiran_de3_PKEY primary key (master_id)
);

create table master_torikomi_term_ichiran_mk2 (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_term_ichiran_mk2_PKEY primary key (master_id)
);

create table master_torikomi_term_ichiran_sias (
  master_id character varying(50) not null
  , master_name character varying(50) not null
  , op_master_id character varying not null
  , op_master_name character varying not null
  , torikomi_kahi_flg character varying(1) not null
  , constraint master_torikomi_term_ichiran_sias_PKEY primary key (master_id)
);

create table master_torikomi_term_shousai_de3 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_term_shousai_de3_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_term_shousai_mk2 (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_term_shousai_mk2_PKEY primary key (master_id,et_column_id)
);

create table master_torikomi_term_shousai_sias (
  master_id character varying(50) not null
  , et_column_id character varying not null
  , et_column_name character varying not null
  , et_data_type character varying not null
  , op_colume_id character varying not null
  , op_column_name character varying not null
  , op_data_type character varying not null
  , entry_order integer not null
  , pk_flg character varying(1) not null
  , constraint master_torikomi_term_shousai_sias_PKEY primary key (master_id,et_column_id)
);

create table midashi (
  midashi_id bigserial not null
  , midashi_name character varying(20) not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint midashi_PKEY primary key (midashi_id)
);

create table moto_kouza (
  moto_kinyuukikan_cd character varying(4) not null
  , moto_kinyuukikan_shiten_cd character varying(3) not null
  , moto_yokinshubetsu character varying(1) not null
  , moto_kouza_bangou character varying(7) not null
  , moto_kinyuukikan_name_hankana character varying(15) not null
  , moto_kinyuukikan_shiten_name_hankana character varying(15) not null
  , shubetsu_cd character varying(2) not null
  , cd_kbn character varying(1) not null
  , kaisha_cd character varying(10) not null
  , kaisha_name_hankana character varying(40) not null
  , shinki_cd character varying(1) not null
  , furikomi_kbn character varying(1) not null
  , constraint moto_kouza_PKEY primary key (moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou)
);

create table moto_kouza_shiharaiirai (
  moto_kinyuukikan_cd character varying(4) not null
  , moto_kinyuukikan_shiten_cd character varying(3) not null
  , moto_yokinshubetsu character varying(1) not null
  , moto_kouza_bangou character varying(7) not null
  , moto_kinyuukikan_name_hankana character varying(15) not null
  , moto_kinyuukikan_shiten_name_hankana character varying(15) not null
  , shubetsu_cd character varying(2) not null
  , cd_kbn character varying(1) not null
  , kaisha_cd character varying(10) not null
  , kaisha_name_hankana character varying(40) not null
  , shinki_cd character varying(1) not null
  , furikomi_kbn character varying(1) not null
  , constraint moto_kouza_shiharaiirai_PKEY primary key (moto_kinyuukikan_cd,moto_kinyuukikan_shiten_cd,moto_yokinshubetsu,moto_kouza_bangou)
);

create table naibu_cd_setting (
  naibu_cd_name character varying not null
  , naibu_cd character varying not null
  , name character varying not null
  , hyouji_jun integer not null
  , option1 character varying not null
  , option2 character varying not null
  , option3 character varying not null
  , constraint naibu_cd_setting_PKEY primary key (naibu_cd_name,naibu_cd)
);

create table nini_comment (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , comment character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint nini_comment_PKEY primary key (denpyou_id,edano)
);

create table nittou_nado_master (
  shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , yakushoku_cd character varying(10) not null
  , tanka numeric(15)
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , nittou_shukuhakuhi_flg character varying(1) not null
  , zei_kubun character varying(1)
  , edaban character varying(12) not null
  , constraint nittou_nado_master_PKEY primary key (shubetsu1,shubetsu2,yakushoku_cd)
);

create table open21_kinyuukikan (
  kinyuukikan_cd character varying(4) not null
  , kinyuukikan_shiten_cd character varying(3) not null
  , kinyuukikan_name_hankana character varying(15) not null
  , kinyuukikan_name_kana character varying(30) not null
  , shiten_name_hankana character varying(15) not null
  , shiten_name_kana character varying(30) not null
  , constraint open21_kinyuukikan_PKEY primary key (kinyuukikan_cd,kinyuukikan_shiten_cd)
);

create table password (
  user_id character varying(30) not null
  , password character varying not null
  , constraint password_PKEY primary key (user_id)
);

create table project_master (
  project_cd character varying(12) not null
  , project_name character varying(20) not null
  , shuuryou_kbn smallint DEFAULT 0 NOT NULL
  , constraint project_master_PKEY primary key (project_cd)
);

create table rate_master (
  heishu_cd character varying(4) not null
  , start_date date not null
  , rate numeric(11, 5)
  , rate1 numeric(11, 5)
  , rate2 numeric(11, 5)
  , rate3 numeric(11, 5)
  , availability_flg smallint default 1 not null
  , constraint rate_master_PKEY primary key (heishu_cd)
);

create table ryohi_karibarai (
  denpyou_id character varying(19) not null
  , karibarai_on character varying(1) default '0' not null
  , dairiflg character varying(1) not null
  , user_id character varying(30)
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , houmonsaki character varying(200) not null
  , mokuteki character varying(240) not null
  , seisankikan_from date
  , seisankikan_from_hour character varying(2)
  , seisankikan_from_min character varying(2)
  , seisankikan_to date
  , seisankikan_to_hour character varying(2)
  , seisankikan_to_min character varying(2)
  , shiharaibi date
  , shiharaikiboubi date
  , shiharaihouhou character varying(1) not null
  , tekiyou character varying(120) not null
  , kingaku numeric(15) not null
  , karibarai_kingaku numeric(15)
  , sashihiki_num numeric(2)
  , sashihiki_tanka numeric(6)
  , hf1_cd character varying(20) not null
  , hf1_name_ryakushiki character varying(20) not null
  , hf2_cd character varying(20) not null
  , hf2_name_ryakushiki character varying(20) not null
  , hf3_cd character varying(20) not null
  , hf3_name_ryakushiki character varying(20) not null
  , hf4_cd character varying(20) not null
  , hf4_name_ryakushiki character varying(20) not null
  , hf5_cd character varying(20) not null
  , hf5_name_ryakushiki character varying(20) not null
  , hf6_cd character varying(20) not null
  , hf6_name_ryakushiki character varying(20) not null
  , hf7_cd character varying(20) not null
  , hf7_name_ryakushiki character varying(20) not null
  , hf8_cd character varying(20) not null
  , hf8_name_ryakushiki character varying(20) not null
  , hf9_cd character varying(20) not null
  , hf9_name_ryakushiki character varying(20) not null
  , hf10_cd character varying(20) not null
  , hf10_name_ryakushiki character varying(20) not null
  , hosoku character varying(240) not null
  , shiwake_edano integer
  , torihiki_name character varying(20) not null
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , seisan_kanryoubi date
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohi_karibarai_PKEY primary key (denpyou_id)
);

create table ryohi_karibarai_keihi_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , shiwake_edano integer not null
  , shiyoubi date
  , shouhyou_shorui_flg character varying(1)
  , torihiki_name character varying(20) not null
  , tekiyou character varying(120) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , shiharai_kingaku numeric(15) not null
  , hontai_kingaku numeric(15)
  , shouhizeigaku numeric(15)
  , kousaihi_shousai_hyouji_flg character varying(1) not null
  , kousaihi_ninzuu_riyou_flg character varying(1) not null
  , kousaihi_shousai character varying(240)
  , kousaihi_ninzuu integer
  , kousaihi_hitori_kingaku numeric(15)
  , kari_futan_bumon_cd character varying(8) not null
  , kari_futan_bumon_name character varying(20) not null
  , torihikisaki_cd character varying(12) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , kari_kamoku_cd character varying(6) not null
  , kari_kamoku_name character varying(22) not null
  , kari_kamoku_edaban_cd character varying(12) not null
  , kari_kamoku_edaban_name character varying(20) not null
  , kari_kazei_kbn character varying(3) not null
  , kashi_futan_bumon_cd character varying(8) not null
  , kashi_futan_bumon_name character varying(20) not null
  , kashi_kamoku_cd character varying(6) not null
  , kashi_kamoku_name character varying(22) not null
  , kashi_kamoku_edaban_cd character varying(12) not null
  , kashi_kamoku_edaban_name character varying(20) not null
  , kashi_kazei_kbn character varying(3) not null
  , uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , project_cd character varying(12) not null
  , project_name character varying(20) not null
  , segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , tekiyou_cd character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohi_karibarai_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table ryohi_karibarai_meisai (
  denpyou_id character varying(19) not null
  , denpyou_edano integer not null
  , kikan_from date not null
  , kikan_to date
  , kyuujitsu_nissuu numeric
  , shubetsu_cd character varying(1) not null
  , shubetsu1 character varying(20) not null
  , shubetsu2 character varying(20) not null
  , haya_flg character varying(1)
  , yasu_flg character varying(1)
  , raku_flg character varying(1)
  , koutsuu_shudan character varying(10) not null
  , shouhyou_shorui_hissu_flg character varying(1) not null
  , naiyou character varying(512) not null
  , bikou character varying not null
  , oufuku_flg character varying(1) not null
  , jidounyuuryoku_flg character varying(1) not null
  , nissuu numeric
  , tanka numeric(15, 3) not null
  , suuryou_nyuryoku_type character varying(1) not null
  , suuryou numeric(15, 2)
  , suuryou_kigou character varying(5)
  , meisai_kingaku numeric(15) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint ryohi_karibarai_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table ryohiseisan (
  denpyou_id character varying(19) not null,
  karibarai_denpyou_id character varying(19) not null,
  karibarai_on character varying(1) not null,
  karibarai_mishiyou_flg character varying(1) default '0' not null,
  shucchou_chuushi_flg character varying(1) default '0' not null,
  dairiflg character varying(1) not null,
  user_id character varying(30) not null,
  shain_no character varying(15) not null,
  user_sei character varying(10) not null,
  user_mei character varying(10) not null,
  houmonsaki character varying(200) not null,
  mokuteki character varying(240) not null,
  seisankikan_from date,
  seisankikan_from_hour character varying(2),
  seisankikan_from_min character varying(2),
  seisankikan_to date,
  seisankikan_to_hour character varying(2),
  seisankikan_to_min character varying(2),
  keijoubi date,
  shiharaibi date,
  shiharaikiboubi date,
  shiharaihouhou character varying(1) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  goukei_kingaku numeric(15) not null,
  houjin_card_riyou_kingaku numeric(15) not null,
  kaisha_tehai_kingaku numeric(15) not null,
  sashihiki_shikyuu_kingaku numeric(15) not null,
  sashihiki_num numeric(2),
  sashihiki_tanka numeric(6),
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  shiwake_edano integer,
  torihiki_name character varying(20) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  bunri_kbn character varying(1) not null,
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  invoice_denpyou character varying(1) not null default '1',
  constraint ryohiseisan_PKEY primary key (denpyou_id)
);

create table ryohiseisan_keihi_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  shiwake_edano integer not null,
  shiyoubi date,
  shouhyou_shorui_flg character varying(1),
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  shiharai_kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  kousaihi_shousai_hyouji_flg character varying(1) not null,
  kousaihi_ninzuu_riyou_flg character varying(1) not null,
  kousaihi_shousai character varying(240),
  kousaihi_ninzuu integer,
  kousaihi_hitori_kingaku numeric(15),
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  constraint ryohiseisan_keihi_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table ryohiseisan_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  kikan_from date not null,
  kikan_to date,
  kyuujitsu_nissuu numeric,
  shubetsu_cd character varying(1) not null,
  shubetsu1 character varying(20) not null,
  shubetsu2 character varying(20) not null,
  haya_flg character varying(1),
  yasu_flg character varying(1),
  raku_flg character varying(1),
  koutsuu_shudan character varying(10) not null,
  shouhyou_shorui_hissu_flg character varying(1) not null,
  ryoushuusho_seikyuusho_tou_flg character(1) not null,
  naiyou character varying(512) not null,
  bikou character varying not null,
  oufuku_flg character varying(1) not null,
  houjin_card_riyou_flg character varying(1) not null,
  kaisha_tehai_flg character varying(1) not null,
  jidounyuuryoku_flg character varying(1) not null,
  nissuu numeric,
  tanka numeric(15, 3) not null,
  suuryou_nyuryoku_type character varying(1) not null,
  suuryou numeric(15, 2),
  suuryou_kigou character varying(5),
  meisai_kingaku numeric(15) not null,
  ic_card_no character varying(16) not null,
  ic_card_sequence_no character varying(10) not null,
  himoduke_card_meisai bigint,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  zeinuki_kingaku numeric(15) not null default 0,
  shouhizeigaku numeric(15) not null default 0,
  zeigaku_fix_flg character varying(1) not null default '0',
  constraint ryohiseisan_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table saiban_kanri (
  saiban_kbn character varying not null
  , sequence_val integer
  , constraint saiban_kanri_PKEY primary key (saiban_kbn)
);

create table saishuu_syounin_route_ko (
  denpyou_kbn character varying(4) not null
  , edano integer not null
  , edaedano integer not null
  , gyoumu_role_id character varying(5) not null
  , saishuu_shounin_shori_kengen_name character varying(6) not null
  , touroku_user_id character varying(30)
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint saishuu_syounin_route_ko_PKEY primary key (denpyou_kbn,edano,edaedano)
);

create table saishuu_syounin_route_oya (
  denpyou_kbn character varying(4) not null
  , edano integer not null
  , chuuki_mongon character varying(400) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint saishuu_syounin_route_oya_PKEY primary key (denpyou_kbn,edano)
);

create table security_log (
  serial_no bigserial not null
  , event_time timestamp without time zone default now() not null
  , ip character varying(40) not null
  , ip_xforwarded character varying(255)
  , user_id character varying(30)
  , gyoumu_role_id character varying(5)
  , target character varying(100) not null
  , type character varying(100) not null
  , detail character varying(100) not null
  , constraint security_log_PKEY primary key (serial_no)
);

create table segment_kamoku_zandaka (
  segment_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , torihikisaki_name_seishiki character varying(60) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint segment_kamoku_zandaka_PKEY primary key (segment_cd,kamoku_gaibu_cd)
);

create table segment_master (
  segment_cd character varying(8) not null
  , segment_name_ryakushiki character varying(20) not null
  , segment_name_seishiki character varying(60) not null
  , constraint segment_master_PKEY primary key (segment_cd)
);

create table seikyuushobarai (
  denpyou_id character varying(19) not null,
  keijoubi date,
  shiharai_kigen date,
  shiharaibi date,
  masref_flg character varying(1) not null,
  shouhyou_shorui_flg character varying(1) not null,
  kake_flg character varying(1) not null,
  hontai_kingaku_goukei numeric(15) not null,
  shouhizeigaku_goukei numeric(15) not null,
  shiharai_kingaku_goukei numeric(15) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  nyuryoku_houshiki character varying(1) not null default '0',
  invoice_denpyou character varying(1) not null default '1',
  constraint seikyuushobarai_PKEY primary key (denpyou_id)
);

create table seikyuushobarai_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  shiwake_edano integer not null,
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  shiharai_kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  kousaihi_shousai_hyouji_flg character varying(1) not null,
  kousaihi_ninzuu_riyou_flg character varying(1) not null,
  kousaihi_shousai character varying(240),
  kousaihi_ninzuu integer,
  kousaihi_hitori_kingaku numeric(15),
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  furikomisaki_jouhou character varying(240) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  jigyousha_kbn character varying(1) not null default '0',
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  constraint seikyuushobarai_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table setting_info (
  setting_name character varying not null
  , setting_name_wa character varying not null
  , setting_val character varying not null
  , category character varying not null
  , hyouji_jun integer not null
  , editable_flg character varying(1) not null
  , hissu_flg character varying(1) not null
  , format_regex character varying not null
  , description character varying not null
  , constraint setting_info_PKEY primary key (setting_name)
);

create table shain (
  shain_no character varying(15) not null
  , user_full_name character varying(50) not null
  , daihyou_futan_bumon_cd character varying(8) not null
  , yakushoku_cd character varying(10) not null
  , constraint shain_PKEY primary key (shain_no)
);

create table shain_kouza (
  shain_no character varying(15) not null
  , saki_kinyuukikan_cd character varying(4) not null
  , saki_ginkou_shiten_cd character varying(3) not null
  , saki_yokin_shabetsu character varying(1) not null
  , saki_kouza_bangou character varying(7) not null
  , saki_kouza_meigi_kanji character varying(30) not null
  , saki_kouza_meigi_kana character varying(30) not null
  , moto_kinyuukikan_cd character varying(4) not null
  , moto_kinyuukikan_shiten_cd character varying(3) not null
  , moto_yokinshubetsu character varying(1) not null
  , moto_kouza_bangou character varying(7) not null
  , zaimu_edaban_cd character varying(12) DEFAULT '' not null
  , constraint shain_kouza_PKEY primary key (shain_no)
);

create table shiharai_irai (
  denpyou_id character varying(19) not null,
  keijoubi date not null,
  yoteibi date not null,
  shiharaibi date,
  shiharai_kijitsu date,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  ichigensaki_torihikisaki_name character varying not null,
  edi character varying(20) not null,
  shiharai_goukei numeric(15) not null,
  sousai_goukei numeric(15) not null,
  sashihiki_shiharai numeric(15) not null,
  manekin_gensen numeric(15) not null,
  shouhyou_shorui_flg character varying(1) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  shiharai_houhou character varying(1) not null,
  shiharai_shubetsu character varying(1) not null,
  furikomi_ginkou_cd character varying(4) not null,
  furikomi_ginkou_name character varying(30) not null,
  furikomi_ginkou_shiten_cd character varying(3) not null,
  furikomi_ginkou_shiten_name character varying(30) not null,
  yokin_shubetsu character varying(1) not null,
  kouza_bangou character varying(7) not null,
  kouza_meiginin character varying(60) not null,
  tesuuryou character varying(1) not null,
  hosoku character varying(240) not null,
  gyaku_shiwake_flg character varying(1) default '0' not null,
  shutsuryoku_flg character varying(1) default '0' not null,
  csv_upload_flg character varying(1) default '0' not null,
  hassei_shubetsu character varying default '�o��' not null,
  saimu_made_flg character varying(1) default '0' not null,
  fb_made_flg character varying(1) default '0' not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  nyuryoku_houshiki character varying(1) not null default '0',
  jigyousha_kbn character varying(15) not null default '0',
  jigyousha_no character varying(15) not null default '',
  invoice_denpyou character varying(1) not null default '1',
  constraint shiharai_irai_PKEY primary key (denpyou_id)
);

create table shiharai_irai_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  shiwake_edano integer not null,
  torihiki_name character varying(20) not null,
  tekiyou character varying(120) not null,
  shiharai_kingaku numeric(15) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  zeinuki_kingaku numeric(15) not null default 0,
  shouhizeigaku numeric(15) not null default 0,
  constraint shiharai_irai_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table shikkou_joukyou_ichiran_jouhou (
  user_id character varying(30) not null
  , yosan_tani character varying(1) not null
  , constraint shikkou_joukyou_ichiran_jouhou_PKEY primary key (user_id)
);

create table shiwake_de3 (
  serial_no bigserial not null
  , denpyou_id character varying(19) not null
  , shiwake_status character varying(1) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , dymd date
  , seiri character varying(1) not null
  , dcno character varying(8) not null
  , rbmn character varying(8) not null
  , rtor character varying(12) not null
  , rkmk character varying(6) not null
  , reda character varying(12) not null
  , rkoj character varying(10) not null
  , rkos character varying(6) not null
  , rprj character varying(12) not null
  , rseg character varying(8) not null
  , rdm1 character varying(20) not null
  , rdm2 character varying(20) not null
  , rdm3 character varying(20) not null
  , tky character varying(60) not null
  , tno character varying(4) not null
  , sbmn character varying(8) not null
  , stor character varying(12) not null
  , skmk character varying(6) not null
  , seda character varying(12) not null
  , skoj character varying(10) not null
  , skos character varying(6) not null
  , sprj character varying(12) not null
  , sseg character varying(8) not null
  , sdm1 character varying(20) not null
  , sdm2 character varying(20) not null
  , sdm3 character varying(20) not null
  , exvl numeric(15)
  , valu numeric(15)
  , zkmk character varying(6) not null
  , zrit numeric(3)
  , zkeigen character varying(1) not null
  , zzkb character varying(3) not null
  , zgyo character varying(1) not null
  , zsre character varying(1) not null
  , rrit numeric(3)
  , rkeigen character varying(1) not null
  , srit numeric(3)
  , skeigen character varying(1) not null
  , rzkb character varying(3) not null
  , rgyo character varying(1) not null
  , rsre character varying(1) not null
  , szkb character varying(3) not null
  , sgyo character varying(1) not null
  , ssre character varying(1) not null
  , symd date
  , skbn character varying(2) not null
  , skiz date
  , uymd date
  , ukbn character varying(2) not null
  , ukiz date
  , sten character varying(1) not null
  , dkec character varying(12) not null
  , kymd date
  , kbmn character varying(8) not null
  , kusr character varying(12) not null
  , fusr character varying(4) not null
  , fsen character varying(2) not null
  , sgno character varying(4) not null
  , bunri character varying(1) not null
  , rate character varying(12) not null
  , gexvl character varying(14) not null
  , gvalu character varying(14) not null
  , gsep character varying(1) not null
  , rurizeikeisan character varying(1) default 0
  , surizeikeisan character varying(1) default 0
  , zurizeikeisan character varying(1) default 0
  , rmenzeikeika character varying(1) default 0
  , smenzeikeika character varying(1) default 0
  , zmenzeikeika character varying(1) default 0
  , constraint shiwake_de3_PKEY primary key (serial_no)
);

create table shiwake_pattern_master (
  denpyou_kbn character varying(4) not null,
  shiwake_edano integer not null,
  delete_flg character varying(1) default '0' not null,
  yuukou_kigen_from date not null,
  yuukou_kigen_to date not null,
  bunrui1 character varying(20) not null,
  bunrui2 character varying(20) not null,
  bunrui3 character varying(20) not null,
  torihiki_name character varying(20) not null,
  tekiyou_flg character varying(1) not null,
  tekiyou character varying(60),
  default_hyouji_flg character varying(1) not null,
  kousaihi_hyouji_flg character varying(1) not null,
  kousaihi_ninzuu_riyou_flg character varying(1) not null,
  kousaihi_kijun_gaku numeric(6),
  kousaihi_check_houhou character varying(1) not null,
  kousaihi_check_result character varying(1) not null,
  kake_flg character varying(1) not null,
  hyouji_jun integer not null,
  shain_cd_renkei_flg character varying(1) not null,
  edaban_renkei_flg character varying(1) not null,
  kari_futan_bumon_cd character varying not null,
  kari_kamoku_cd character varying not null,
  kari_kamoku_edaban_cd character varying not null,
  kari_torihikisaki_cd character varying not null,
  kari_project_cd character varying not null,
  kari_segment_cd character varying(8) not null,
  kari_uf1_cd character varying(20) not null,
  kari_uf2_cd character varying(20) not null,
  kari_uf3_cd character varying(20) not null,
  kari_uf4_cd character varying(20) not null,
  kari_uf5_cd character varying(20) not null,
  kari_uf6_cd character varying(20) not null,
  kari_uf7_cd character varying(20) not null,
  kari_uf8_cd character varying(20) not null,
  kari_uf9_cd character varying(20) not null,
  kari_uf10_cd character varying(20) not null,
  kari_uf_kotei1_cd character varying(20) not null,
  kari_uf_kotei2_cd character varying(20) not null,
  kari_uf_kotei3_cd character varying(20) not null,
  kari_uf_kotei4_cd character varying(20) not null,
  kari_uf_kotei5_cd character varying(20) not null,
  kari_uf_kotei6_cd character varying(20) not null,
  kari_uf_kotei7_cd character varying(20) not null,
  kari_uf_kotei8_cd character varying(20) not null,
  kari_uf_kotei9_cd character varying(20) not null,
  kari_uf_kotei10_cd character varying(20) not null,
  kari_kazei_kbn character varying not null,
  kari_zeiritsu character varying(10) not null,
  kari_keigen_zeiritsu_kbn character varying(1) not null,
  kashi_futan_bumon_cd1 character varying not null,
  kashi_kamoku_cd1 character varying not null,
  kashi_kamoku_edaban_cd1 character varying not null,
  kashi_torihikisaki_cd1 character varying not null,
  kashi_project_cd1 character varying not null,
  kashi_segment_cd1 character varying(8) not null,
  kashi_uf1_cd1 character varying(20) not null,
  kashi_uf2_cd1 character varying(20) not null,
  kashi_uf3_cd1 character varying(20) not null,
  kashi_uf4_cd1 character varying(20) not null,
  kashi_uf5_cd1 character varying(20) not null,
  kashi_uf6_cd1 character varying(20) not null,
  kashi_uf7_cd1 character varying(20) not null,
  kashi_uf8_cd1 character varying(20) not null,
  kashi_uf9_cd1 character varying(20) not null,
  kashi_uf10_cd1 character varying(20) not null,
  kashi_uf_kotei1_cd1 character varying(20) not null,
  kashi_uf_kotei2_cd1 character varying(20) not null,
  kashi_uf_kotei3_cd1 character varying(20) not null,
  kashi_uf_kotei4_cd1 character varying(20) not null,
  kashi_uf_kotei5_cd1 character varying(20) not null,
  kashi_uf_kotei6_cd1 character varying(20) not null,
  kashi_uf_kotei7_cd1 character varying(20) not null,
  kashi_uf_kotei8_cd1 character varying(20) not null,
  kashi_uf_kotei9_cd1 character varying(20) not null,
  kashi_uf_kotei10_cd1 character varying(20) not null,
  kashi_kazei_kbn1 character varying not null,
  kashi_futan_bumon_cd2 character varying not null,
  kashi_torihikisaki_cd2 character varying not null,
  kashi_kamoku_cd2 character varying not null,
  kashi_kamoku_edaban_cd2 character varying not null,
  kashi_project_cd2 character varying not null,
  kashi_segment_cd2 character varying(8) not null,
  kashi_uf1_cd2 character varying(20) not null,
  kashi_uf2_cd2 character varying(20) not null,
  kashi_uf3_cd2 character varying(20) not null,
  kashi_uf4_cd2 character varying(20) not null,
  kashi_uf5_cd2 character varying(20) not null,
  kashi_uf6_cd2 character varying(20) not null,
  kashi_uf7_cd2 character varying(20) not null,
  kashi_uf8_cd2 character varying(20) not null,
  kashi_uf9_cd2 character varying(20) not null,
  kashi_uf10_cd2 character varying(20) not null,
  kashi_uf_kotei1_cd2 character varying(20) not null,
  kashi_uf_kotei2_cd2 character varying(20) not null,
  kashi_uf_kotei3_cd2 character varying(20) not null,
  kashi_uf_kotei4_cd2 character varying(20) not null,
  kashi_uf_kotei5_cd2 character varying(20) not null,
  kashi_uf_kotei6_cd2 character varying(20) not null,
  kashi_uf_kotei7_cd2 character varying(20) not null,
  kashi_uf_kotei8_cd2 character varying(20) not null,
  kashi_uf_kotei9_cd2 character varying(20) not null,
  kashi_uf_kotei10_cd2 character varying(20) not null,
  kashi_kazei_kbn2 character varying not null,
  kashi_futan_bumon_cd3 character varying not null,
  kashi_torihikisaki_cd3 character varying not null,
  kashi_kamoku_cd3 character varying not null,
  kashi_kamoku_edaban_cd3 character varying not null,
  kashi_project_cd3 character varying not null,
  kashi_segment_cd3 character varying(8) not null,
  kashi_uf1_cd3 character varying(20) not null,
  kashi_uf2_cd3 character varying(20) not null,
  kashi_uf3_cd3 character varying(20) not null,
  kashi_uf4_cd3 character varying(20) not null,
  kashi_uf5_cd3 character varying(20) not null,
  kashi_uf6_cd3 character varying(20) not null,
  kashi_uf7_cd3 character varying(20) not null,
  kashi_uf8_cd3 character varying(20) not null,
  kashi_uf9_cd3 character varying(20) not null,
  kashi_uf10_cd3 character varying(20) not null,
  kashi_uf_kotei1_cd3 character varying(20) not null,
  kashi_uf_kotei2_cd3 character varying(20) not null,
  kashi_uf_kotei3_cd3 character varying(20) not null,
  kashi_uf_kotei4_cd3 character varying(20) not null,
  kashi_uf_kotei5_cd3 character varying(20) not null,
  kashi_uf_kotei6_cd3 character varying(20) not null,
  kashi_uf_kotei7_cd3 character varying(20) not null,
  kashi_uf_kotei8_cd3 character varying(20) not null,
  kashi_uf_kotei9_cd3 character varying(20) not null,
  kashi_uf_kotei10_cd3 character varying(20) not null,
  kashi_kazei_kbn3 character varying not null,
  kashi_futan_bumon_cd4 character varying not null,
  kashi_torihikisaki_cd4 character varying not null,
  kashi_kamoku_cd4 character varying not null,
  kashi_kamoku_edaban_cd4 character varying not null,
  kashi_project_cd4 character varying not null,
  kashi_segment_cd4 character varying(8) not null,
  kashi_uf1_cd4 character varying(20) not null,
  kashi_uf2_cd4 character varying(20) not null,
  kashi_uf3_cd4 character varying(20) not null,
  kashi_uf4_cd4 character varying(20) not null,
  kashi_uf5_cd4 character varying(20) not null,
  kashi_uf6_cd4 character varying(20) not null,
  kashi_uf7_cd4 character varying(20) not null,
  kashi_uf8_cd4 character varying(20) not null,
  kashi_uf9_cd4 character varying(20) not null,
  kashi_uf10_cd4 character varying(20) not null,
  kashi_uf_kotei1_cd4 character varying(20) not null,
  kashi_uf_kotei2_cd4 character varying(20) not null,
  kashi_uf_kotei3_cd4 character varying(20) not null,
  kashi_uf_kotei4_cd4 character varying(20) not null,
  kashi_uf_kotei5_cd4 character varying(20) not null,
  kashi_uf_kotei6_cd4 character varying(20) not null,
  kashi_uf_kotei7_cd4 character varying(20) not null,
  kashi_uf_kotei8_cd4 character varying(20) not null,
  kashi_uf_kotei9_cd4 character varying(20) not null,
  kashi_uf_kotei10_cd4 character varying(20) not null,
  kashi_kazei_kbn4 character varying not null,
  kashi_futan_bumon_cd5 character varying not null,
  kashi_torihikisaki_cd5 character varying not null,
  kashi_kamoku_cd5 character varying not null,
  kashi_kamoku_edaban_cd5 character varying not null,
  kashi_project_cd5 character varying not null,
  kashi_segment_cd5 character varying(8) not null,
  kashi_uf1_cd5 character varying(20) not null,
  kashi_uf2_cd5 character varying(20) not null,
  kashi_uf3_cd5 character varying(20) not null,
  kashi_uf4_cd5 character varying(20) not null,
  kashi_uf5_cd5 character varying(20) not null,
  kashi_uf6_cd5 character varying(20) not null,
  kashi_uf7_cd5 character varying(20) not null,
  kashi_uf8_cd5 character varying(20) not null,
  kashi_uf9_cd5 character varying(20) not null,
  kashi_uf10_cd5 character varying(20) not null,
  kashi_uf_kotei1_cd5 character varying(20) not null,
  kashi_uf_kotei2_cd5 character varying(20) not null,
  kashi_uf_kotei3_cd5 character varying(20) not null,
  kashi_uf_kotei4_cd5 character varying(20) not null,
  kashi_uf_kotei5_cd5 character varying(20) not null,
  kashi_uf_kotei6_cd5 character varying(20) not null,
  kashi_uf_kotei7_cd5 character varying(20) not null,
  kashi_uf_kotei8_cd5 character varying(20) not null,
  kashi_uf_kotei9_cd5 character varying(20) not null,
  kashi_uf_kotei10_cd5 character varying(20) not null,
  kashi_kazei_kbn5 character varying not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  old_kari_kazei_kbn character varying not null default '',
  old_kashi_kazei_kbn1 character varying not null default '',
  old_kashi_kazei_kbn2 character varying not null default '',
  old_kashi_kazei_kbn3 character varying not null default '',
  old_kashi_kazei_kbn4 character varying not null default '',
  old_kashi_kazei_kbn5 character varying not null default '',
  kari_bunri_kbn character varying(1),
  kashi_bunri_kbn1 character varying(1),
  kashi_bunri_kbn2 character varying(1),
  kashi_bunri_kbn3 character varying(1),
  kashi_bunri_kbn4 character varying(1),
  kashi_bunri_kbn5 character varying(1),
  kari_shiire_kbn character varying(1),
  kashi_shiire_kbn1 character varying(1),
  kashi_shiire_kbn2 character varying(1),
  kashi_shiire_kbn3 character varying(1),
  kashi_shiire_kbn4 character varying(1),
  kashi_shiire_kbn5 character varying(1),
  constraint shiwake_pattern_master_PKEY primary key (denpyou_kbn,shiwake_edano)
);

create table shiwake_pattern_setting (
  denpyou_kbn character varying(4) not null
  , setting_kbn character varying(1) not null
  , setting_item character varying(20) not null
  , default_value character varying(100) not null
  , hyouji_flg character varying(1) not null
  , shiwake_pattern_var1 character varying(20) not null
  , shiwake_pattern_var2 character varying(20) not null
  , shiwake_pattern_var3 character varying(20) not null
  , shiwake_pattern_var4 character varying(20) not null
  , shiwake_pattern_var5 character varying(20) not null
  , constraint shiwake_pattern_setting_PKEY primary key (denpyou_kbn,setting_kbn,setting_item)
);

create table shiwake_pattern_var_setting (
  shiwake_pattern_var character varying(20) not null
  , shiwake_pattern_var_name character varying(30) not null
  , var_setsumei character varying(40) not null
  , session_var character varying(30) not null
  , column_name character varying(30) not null
  , hyouji_jun integer not null
  , constraint shiwake_pattern_var_setting_PKEY primary key (shiwake_pattern_var)
);

create table shiwake_sias (
  serial_no bigserial not null
  , denpyou_id character varying(19) not null
  , shiwake_status character varying(1) not null
  , touroku_time timestamp without time zone
  , koushin_time timestamp without time zone
  , dymd date
  , seiri character varying(1) not null
  , dcno character varying(8) not null
  , kymd date
  , kbmn character varying(8) not null
  , kusr character varying(12) not null
  , sgno character varying(4) not null
  , hf1 character varying(20) not null
  , hf2 character varying(20) not null
  , hf3 character varying(20) not null
  , hf4 character varying(20) not null
  , hf5 character varying(20) not null
  , hf6 character varying(20) not null
  , hf7 character varying(20) not null
  , hf8 character varying(20) not null
  , hf9 character varying(20) not null
  , hf10 character varying(20) not null
  , rbmn character varying(8) not null
  , rtor character varying(12) not null
  , rkmk character varying(6) not null
  , reda character varying(12) not null
  , rkoj character varying(10) not null
  , rkos character varying(6) not null
  , rprj character varying(12) not null
  , rseg character varying(8) not null
  , rdm1 character varying(20) not null
  , rdm2 character varying(20) not null
  , rdm3 character varying(20) not null
  , rdm4 character varying(20) not null
  , rdm5 character varying(20) not null
  , rdm6 character varying(20) not null
  , rdm7 character varying(20) not null
  , rdm8 character varying(20) not null
  , rdm9 character varying(20) not null
  , rdm10 character varying(20) not null
  , rdm11 character varying(20) not null
  , rdm12 character varying(20) not null
  , rdm13 character varying(20) not null
  , rdm14 character varying(20) not null
  , rdm15 character varying(20) not null
  , rdm16 character varying(20) not null
  , rdm17 character varying(20) not null
  , rdm18 character varying(20) not null
  , rdm19 character varying(20) not null
  , rdm20 character varying(20) not null
  , rrit numeric(3)
  , rkeigen character varying(1) not null
  , rzkb character varying(3) not null
  , rgyo character varying(1) not null
  , rsre character varying(1) not null
  , rtky character varying(120) not null
  , rtno character varying(4) not null
  , sbmn character varying(8) not null
  , stor character varying(12) not null
  , skmk character varying(6) not null
  , seda character varying(12) not null
  , skoj character varying(10) not null
  , skos character varying(6) not null
  , sprj character varying(12) not null
  , sseg character varying(8) not null
  , sdm1 character varying(20) not null
  , sdm2 character varying(20) not null
  , sdm3 character varying(20) not null
  , sdm4 character varying(20) not null
  , sdm5 character varying(20) not null
  , sdm6 character varying(20) not null
  , sdm7 character varying(20) not null
  , sdm8 character varying(20) not null
  , sdm9 character varying(20) not null
  , sdm10 character varying(20) not null
  , sdm11 character varying(20) not null
  , sdm12 character varying(20) not null
  , sdm13 character varying(20) not null
  , sdm14 character varying(20) not null
  , sdm15 character varying(20) not null
  , sdm16 character varying(20) not null
  , sdm17 character varying(20) not null
  , sdm18 character varying(20) not null
  , sdm19 character varying(20) not null
  , sdm20 character varying(20) not null
  , srit numeric(3)
  , skeigen character varying(1) not null
  , szkb character varying(3) not null
  , sgyo character varying(1) not null
  , ssre character varying(1) not null
  , stky character varying(120) not null
  , stno character varying(4) not null
  , zkmk character varying(6) not null
  , zrit numeric(3)
  , zkeigen character varying(1) not null
  , zzkb character varying(3) not null
  , zgyo character varying(1) not null
  , zsre character varying(1) not null
  , exvl numeric(15)
  , valu numeric(15)
  , symd date
  , skbn character varying(2) not null
  , skiz date
  , uymd date
  , ukbn character varying(2) not null
  , ukiz date
  , dkec character varying(12) not null
  , fusr character varying(4) not null
  , fsen character varying(2) not null
  , tkflg character varying(1) not null
  , bunri character varying(1) not null
  , heic character varying(4) not null
  , rate character varying(12) not null
  , gexvl character varying(14) not null
  , gvalu character varying(14) not null
  , gsep character varying(1) not null
  , rurizeikeisan character varying(1) default 0
  , surizeikeisan character varying(1) default 0
  , zurizeikeisan character varying(1) default 0
  , rmenzeikeika character varying(1) default 0
  , smenzeikeika character varying(1) default 0
  , zmenzeikeika character varying(1) default 0
  
  , constraint shiwake_sias_PKEY primary key (serial_no)
);

create table shouhizeiritsu (
  sort_jun character(3) not null
  , zeiritsu numeric(3) not null
  , keigen_zeiritsu_kbn character(1) default 0 not null
  , hasuu_keisan_kbn character varying(1)
  , yuukou_kigen_from date
  , yuukou_kigen_to date
  , constraint shouhizeiritsu_PKEY primary key (sort_jun,zeiritsu)
);

create table shounin_joukyou (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_cd character varying(8) not null
  , bumon_full_name character varying not null
  , bumon_role_id character varying(5) not null
  , bumon_role_name character varying not null
  , gyoumu_role_id character varying(5) not null
  , gyoumu_role_name character varying not null
  , joukyou_cd character varying(1) not null
  , joukyou character varying not null
  , comment character varying not null
  , shounin_route_edano integer
  , shounin_route_gougi_edano integer
  , shounin_route_gougi_edaedano integer
  , shounin_shori_kengen_no bigint
  , shounin_shori_kengen_name character varying(6) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_joukyou_PKEY primary key (denpyou_id,edano)
);

create table shounin_route (
  denpyou_id character varying(19) not null
  , edano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_cd character varying(8) not null
  , bumon_full_name character varying not null
  , bumon_role_id character varying(5) not null
  , bumon_role_name character varying not null
  , gyoumu_role_id character varying(5) not null
  , gyoumu_role_name character varying not null
  , genzai_flg character varying(1) not null
  , saishu_shounin_flg character varying(1)
  , joukyou_edano integer
  , shounin_shori_kengen_no bigint
  , shounin_shori_kengen_name character varying(6) not null
  , kihon_model_cd character varying(1) not null
  , shounin_hissu_flg character varying(1) not null
  , shounin_ken_flg character varying(1) not null
  , henkou_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_route_PKEY primary key (denpyou_id,edano)
);

create table shounin_route_gougi_ko (
  denpyou_id character varying(19) not null
  , edano integer not null
  , gougi_edano integer not null
  , gougi_edaedano integer not null
  , user_id character varying(30) not null
  , user_full_name character varying(50) not null
  , bumon_cd character varying(8) not null
  , bumon_full_name character varying not null
  , bumon_role_id character varying(5) not null
  , bumon_role_name character varying not null
  , gougi_genzai_flg character varying(1) not null
  , shounin_shori_kengen_no bigint not null
  , shounin_shori_kengen_name character varying(6) not null
  , kihon_model_cd character varying(1) not null
  , shounin_hissu_flg character varying(1) not null
  , shounin_ken_flg character varying(1) not null
  , henkou_flg character varying(1) not null
  , shounin_ninzuu_cd character varying(1) not null
  , shounin_ninzuu_hiritsu integer
  , syori_cd character varying(1) not null
  , gouginai_group integer not null
  , joukyou_edano integer
  , constraint shounin_route_gougi_ko_PKEY primary key (denpyou_id,edano,gougi_edano,gougi_edaedano,user_id)
);

create table shounin_route_gougi_oya (
  denpyou_id character varying(19) not null
  , edano integer not null
  , gougi_edano integer not null
  , gougi_pattern_no bigint not null
  , gougi_name character varying(20) not null
  , syori_cd character varying(1) not null
  , constraint shounin_route_gougi_oya_PKEY primary key (denpyou_id,edano,gougi_edano)
);

create table shounin_shori_kengen (
  shounin_shori_kengen_no bigserial not null
  , shounin_shori_kengen_name character varying(6) not null
  , kihon_model_cd character varying(1) not null
  , shounin_hissu_flg character varying(1) not null
  , shounin_ken_flg character varying(1) not null
  , henkou_flg character varying(1) not null
  , setsumei character varying(100) not null
  , shounin_mongon character varying(6) not null
  , hanrei_hyouji_cd character varying(1) not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shounin_shori_kengen_PKEY primary key (shounin_shori_kengen_no)
);

create table shozoku_bumon (
  bumon_cd character varying(8) not null
  , bumon_name character varying(20) not null
  , oya_bumon_cd character varying(8) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , security_pattern character varying(4) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shozoku_bumon_PKEY primary key (bumon_cd,yuukou_kigen_from)
);

create table shozoku_bumon_shiwake_pattern_master (
  bumon_cd character varying(8) not null
  , denpyou_kbn character varying(4) not null
  , shiwake_edano integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shozoku_bumon_shiwake_pattern_master_PKEY primary key (bumon_cd,denpyou_kbn,shiwake_edano)
);

create table shozoku_bumon_wariate (
  bumon_cd character varying(8) not null
  , bumon_role_id character varying(5) not null
  , user_id character varying(30) not null
  , daihyou_futan_bumon_cd character varying(8) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , hyouji_jun integer not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint shozoku_bumon_wariate_PKEY primary key (bumon_cd,bumon_role_id,user_id,yuukou_kigen_from)
);

create index user_id
  on shozoku_bumon_wariate(user_id);

create table shukujitsu_master (
  shukujitsu date not null
  , shukujitsu_name character varying(30) not null
  , constraint shukujitsu_master_PKEY primary key (shukujitsu)
);

create table syuukei_bumon (
  syuukei_bumon_cd character varying(8) not null
  , syuukei_bumon_name character varying(20) not null
  , kessanki_bangou smallint
  , shaukei_bumon_flg smallint not null
  , constraint syuukei_bumon_PKEY primary key (syuukei_bumon_cd)
);

create table teiki_jouhou (
  user_id character varying(30) not null
  , shiyou_kaishibi date not null
  , shiyou_shuuryoubi date not null
  , intra_teiki_kukan character varying
  , intra_restoreroute character varying not null
  , web_teiki_kukan character varying
  , web_teiki_serialize_data character varying not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint teiki_jouhou_PKEY primary key (user_id,shiyou_kaishibi,shiyou_shuuryoubi)
);

create table temp_csvmake (
  row_num integer not null
  , data_rowbinary bytea
  , constraint temp_csvmake_PKEY primary key (row_num)
);

create table tenpu_denpyou_jyogai (
  denpyou_id character varying(19) not null
  , constraint tenpu_denpyou_jyogai_PKEY primary key (denpyou_id)
);

create table tenpu_file (
  denpyou_id character varying(19) not null
  , edano integer not null
  , file_name character varying not null
  , file_size bigint not null
  , content_type character varying not null
  , binary_data bytea not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint tenpu_file_PKEY primary key (denpyou_id,edano)
);

create table torihikisaki (
  torihikisaki_cd character varying(12) not null
  , yuubin_bangou character varying(7) not null
  , juusho1 character varying(40) not null
  , juusho2 character varying(40) not null
  , telno character varying(20) not null
  , faxno character varying(20) not null
  , kouza_meiginin character varying(44) not null
  , kouza_meiginin_furigana character varying(40) not null
  , shiharai_shubetsu smallint
  , yokin_shubetsu character varying(1) not null
  , kouza_bangou character varying(7) not null
  , tesuuryou_futan_kbn smallint
  , furikomi_kbn character varying(1) not null
  , furikomi_ginkou_cd character varying(4) not null
  , furikomi_ginkou_shiten_cd character varying(3) not null
  , shiharaibi smallint
  , shiharai_kijitsu smallint
  , yakujou_kingaku numeric(15)
  , torihikisaki_name_hankana character varying(4) not null
  , sbusyo character varying not null
  , stanto character varying not null
  , keicd smallint
  , nayose smallint not null
  , f_setuin smallint
  , stan character varying not null
  , sbcod character varying not null
  , skicd character varying not null
  , f_soufu smallint
  , annai smallint
  , tsokbn smallint
  , f_shitu smallint
  , cdm2 character varying not null
  , dm1 character varying not null
  , dm2 smallint
  , dm3 integer
  , gendo numeric(15)
  , constraint torihikisaki_PKEY primary key (torihikisaki_cd)
);

create table torihikisaki_furikomisaki (
  torihikisaki_cd character varying(12) not null
  , ginkou_id smallint not null
  , kinyuukikan_cd character varying(4) not null
  , kinyuukikan_shiten_cd character varying(3) not null
  , yokin_shubetsu character varying(1) not null
  , kouza_bangou character varying(7) not null
  , kouza_meiginin character varying(60) not null
  , constraint torihikisaki_furikomisaki_PKEY primary key (torihikisaki_cd,ginkou_id)
);

create table torihikisaki_hojo (
  torihikisaki_cd character varying(12) not null
  , dm1 character varying not null
  , dm2 smallint not null
  , dm3 integer not null
  , stflg smallint not null
  , constraint torihikisaki_hojo_PKEY primary key (torihikisaki_cd)
);

create table torihikisaki_kamoku_zandaka (
  torihikisaki_cd character varying(12) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , torihikisaki_name_ryakushiki character varying(20) not null
  , torihikisaki_name_seishiki character varying(60) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint torihikisaki_kamoku_zandaka_PKEY primary key (torihikisaki_cd,kamoku_gaibu_cd)
);

create table torihikisaki_master (
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  torihikisaki_name_seishiki character varying(60) not null,
  torihikisaki_name_hankana character varying(4) not null,
  nyuryoku_from_date date,
  nyuryoku_to_date date,
  tekikaku_no character varying(14),
  menzei_jigyousha_flg character varying(1) not null default '0',
  constraint torihikisaki_master_PKEY primary key (torihikisaki_cd)
);

create table torihikisaki_shiharaihouhou (
  torihikisaki_cd character varying(12) not null
  , shiharai_id smallint not null
  , shimebi smallint not null
  , shiharaibi_mm smallint not null
  , shiharaibi_dd smallint not null
  , shiharai_kbn character varying(2) not null
  , shiharaikijitsu_mm smallint not null
  , shiharaikijitsu_dd smallint not null
  , harai_h smallint not null
  , kijitu_h smallint not null
  , shiharai_houhou smallint not null
  , constraint torihikisaki_shiharaihouhou_PKEY primary key (torihikisaki_cd,shiharai_id)
);

create table tsukekae (
  denpyou_id character varying(19) not null,
  denpyou_date date not null,
  shouhyou_shorui_flg character varying(1) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  kingaku_goukei numeric(15) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  hosoku character varying(240) not null,
  tsukekae_kbn character varying(1) not null,
  moto_kamoku_cd character varying(8) not null,
  moto_kamoku_name character varying(22) not null,
  moto_kamoku_edaban_cd character varying(12) not null,
  moto_kamoku_edaban_name character varying(20) not null,
  moto_futan_bumon_cd character varying(8) not null,
  moto_futan_bumon_name character varying(20) not null,
  moto_torihikisaki_cd character varying(12) not null,
  moto_torihikisaki_name_ryakushiki character varying(20) not null,
  moto_kazei_kbn character varying(3) not null,
  moto_uf1_cd character varying(20) not null,
  moto_uf1_name_ryakushiki character varying(20) not null,
  moto_uf2_cd character varying(20) not null,
  moto_uf2_name_ryakushiki character varying(20) not null,
  moto_uf3_cd character varying(20) not null,
  moto_uf3_name_ryakushiki character varying(20) not null,
  moto_uf4_cd character varying(20) not null,
  moto_uf4_name_ryakushiki character varying(20) not null,
  moto_uf5_cd character varying(20) not null,
  moto_uf5_name_ryakushiki character varying(20) not null,
  moto_uf6_cd character varying(20) not null,
  moto_uf6_name_ryakushiki character varying(20) not null,
  moto_uf7_cd character varying(20) not null,
  moto_uf7_name_ryakushiki character varying(20) not null,
  moto_uf8_cd character varying(20) not null,
  moto_uf8_name_ryakushiki character varying(20) not null,
  moto_uf9_cd character varying(20) not null,
  moto_uf9_name_ryakushiki character varying(20) not null,
  moto_uf10_cd character varying(20) not null,
  moto_uf10_name_ryakushiki character varying(20) not null,
  moto_project_cd character varying(12) not null,
  moto_project_name character varying(20) not null,
  moto_segment_cd character varying(8) not null,
  moto_segment_name_ryakushiki character varying(20) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  moto_jigyousha_kbn character varying(1) not null default '0',
  moto_bunri_kbn character varying(1),
  moto_shiire_kbn character varying(1) not null default '',
  moto_zeigaku_houshiki character varying(1) not null default '0',
  invoice_denpyou character varying(1) not null default '1',
  constraint tsukekae_PKEY primary key (denpyou_id)
);

create table tsukekae_meisai (
  denpyou_id character varying(19) not null,
  denpyou_edano integer not null,
  tekiyou character varying(120) not null,
  kingaku numeric(15) not null,
  hontai_kingaku numeric(15),
  shouhizeigaku numeric(15),
  bikou character varying(40) not null,
  saki_kamoku_cd character varying(6) not null,
  saki_kamoku_name character varying(22) not null,
  saki_kamoku_edaban_cd character varying(12) not null,
  saki_kamoku_edaban_name character varying(20) not null,
  saki_futan_bumon_cd character varying(8) not null,
  saki_futan_bumon_name character varying(20) not null,
  saki_torihikisaki_cd character varying(12) not null,
  saki_torihikisaki_name_ryakushiki character varying(20) not null,
  saki_kazei_kbn character varying(3) not null,
  saki_uf1_cd character varying(20) not null,
  saki_uf1_name_ryakushiki character varying(20) not null,
  saki_uf2_cd character varying(20) not null,
  saki_uf2_name_ryakushiki character varying(20) not null,
  saki_uf3_cd character varying(20) not null,
  saki_uf3_name_ryakushiki character varying(20) not null,
  saki_uf4_cd character varying(20) not null,
  saki_uf4_name_ryakushiki character varying(20) not null,
  saki_uf5_cd character varying(20) not null,
  saki_uf5_name_ryakushiki character varying(20) not null,
  saki_uf6_cd character varying(20) not null,
  saki_uf6_name_ryakushiki character varying(20) not null,
  saki_uf7_cd character varying(20) not null,
  saki_uf7_name_ryakushiki character varying(20) not null,
  saki_uf8_cd character varying(20) not null,
  saki_uf8_name_ryakushiki character varying(20) not null,
  saki_uf9_cd character varying(20) not null,
  saki_uf9_name_ryakushiki character varying(20) not null,
  saki_uf10_cd character varying(20) not null,
  saki_uf10_name_ryakushiki character varying(20) not null,
  saki_project_cd character varying(12) not null,
  saki_project_name character varying(20) not null,
  saki_segment_cd character varying(8) not null,
  saki_segment_name_ryakushiki character varying(20) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  saki_jigyousha_kbn character varying(1) not null default '0',
  saki_bunri_kbn character varying(1),
  saki_shiire_kbn character varying(1) not null default '',
  saki_zeigaku_houshiki character varying(1) not null default '0',
  constraint tsukekae_meisai_PKEY primary key (denpyou_id,denpyou_edano)
);

create table tsuuchi (
  serial_no bigserial not null
  , user_id character varying(30) not null
  , denpyou_id character varying(19) not null
  , edano integer not null
  , kidoku_flg character varying(1) not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , constraint tsuuchi_PKEY primary key (serial_no)
);

create index tsuuchi_idx1
  on tsuuchi(user_id);

create table tsuukinteiki (
  denpyou_id character varying(19) not null,
  shiyou_kikan_kbn character varying(2) not null,
  shiyou_kaishibi date not null,
  shiyou_shuuryoubi date not null,
  jyousha_kukan character varying not null,
  teiki_serialize_data character varying not null,
  shiharaibi date,
  tekiyou character varying(120) not null,
  zeiritsu numeric(3) not null,
  keigen_zeiritsu_kbn character(1) default 0 not null,
  kingaku numeric(15) not null,
  tenyuuryoku_flg character varying(1) not null,
  hf1_cd character varying(20) not null,
  hf1_name_ryakushiki character varying(20) not null,
  hf2_cd character varying(20) not null,
  hf2_name_ryakushiki character varying(20) not null,
  hf3_cd character varying(20) not null,
  hf3_name_ryakushiki character varying(20) not null,
  hf4_cd character varying(20) not null,
  hf4_name_ryakushiki character varying(20) not null,
  hf5_cd character varying(20) not null,
  hf5_name_ryakushiki character varying(20) not null,
  hf6_cd character varying(20) not null,
  hf6_name_ryakushiki character varying(20) not null,
  hf7_cd character varying(20) not null,
  hf7_name_ryakushiki character varying(20) not null,
  hf8_cd character varying(20) not null,
  hf8_name_ryakushiki character varying(20) not null,
  hf9_cd character varying(20) not null,
  hf9_name_ryakushiki character varying(20) not null,
  hf10_cd character varying(20) not null,
  hf10_name_ryakushiki character varying(20) not null,
  shiwake_edano integer not null,
  torihiki_name character varying(20) not null,
  kari_futan_bumon_cd character varying(8) not null,
  kari_futan_bumon_name character varying(20) not null,
  torihikisaki_cd character varying(12) not null,
  torihikisaki_name_ryakushiki character varying(20) not null,
  kari_kamoku_cd character varying(6) not null,
  kari_kamoku_name character varying(22) not null,
  kari_kamoku_edaban_cd character varying(12) not null,
  kari_kamoku_edaban_name character varying(20) not null,
  kari_kazei_kbn character varying(3) not null,
  kashi_futan_bumon_cd character varying(8) not null,
  kashi_futan_bumon_name character varying(20) not null,
  kashi_kamoku_cd character varying(6) not null,
  kashi_kamoku_name character varying(22) not null,
  kashi_kamoku_edaban_cd character varying(12) not null,
  kashi_kamoku_edaban_name character varying(20) not null,
  kashi_kazei_kbn character varying(3) not null,
  uf1_cd character varying(20) not null,
  uf1_name_ryakushiki character varying(20) not null,
  uf2_cd character varying(20) not null,
  uf2_name_ryakushiki character varying(20) not null,
  uf3_cd character varying(20) not null,
  uf3_name_ryakushiki character varying(20) not null,
  uf4_cd character varying(20) not null,
  uf4_name_ryakushiki character varying(20) not null,
  uf5_cd character varying(20) not null,
  uf5_name_ryakushiki character varying(20) not null,
  uf6_cd character varying(20) not null,
  uf6_name_ryakushiki character varying(20) not null,
  uf7_cd character varying(20) not null,
  uf7_name_ryakushiki character varying(20) not null,
  uf8_cd character varying(20) not null,
  uf8_name_ryakushiki character varying(20) not null,
  uf9_cd character varying(20) not null,
  uf9_name_ryakushiki character varying(20) not null,
  uf10_cd character varying(20) not null,
  uf10_name_ryakushiki character varying(20) not null,
  project_cd character varying(12) not null,
  project_name character varying(20) not null,
  segment_cd character varying(8) not null,
  segment_name_ryakushiki character varying(20) not null,
  tekiyou_cd character varying(4) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp without time zone not null,
  koushin_user_id character varying(30) not null,
  koushin_time timestamp without time zone not null,
  zeinuki_kingaku numeric(15) not null default 0,
  shouhizeigaku numeric(15) not null default 0,
  shiharaisaki_name character varying(60),
  jigyousha_kbn character varying(1) not null default '0',
  bunri_kbn character varying(1),
  kari_shiire_kbn character varying(1) not null default '',
  kashi_shiire_kbn character varying(1) not null default '',
  invoice_denpyou character varying(1) not null default '1',
  constraint tsuukinteiki_PKEY primary key (denpyou_id)
);

create table uf_kotei1_ichiran (
  uf_kotei1_cd character varying(20) not null
  , uf_kotei1_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei1_ichiran_PKEY primary key (uf_kotei1_cd)
);

create table uf_kotei1_zandaka (
  uf_kotei1_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei1_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei1_zandaka_PKEY primary key (uf_kotei1_cd,kamoku_gaibu_cd)
);

create table uf_kotei10_ichiran (
  uf_kotei10_cd character varying(20) not null
  , uf_kotei10_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei10_ichiran_PKEY primary key (uf_kotei10_cd)
);

create table uf_kotei10_zandaka (
  uf_kotei10_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei10_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei10_zandaka_PKEY primary key (uf_kotei10_cd,kamoku_gaibu_cd)
);

create table uf_kotei2_ichiran (
  uf_kotei2_cd character varying(20) not null
  , uf_kotei2_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei2_ichiran_PKEY primary key (uf_kotei2_cd)
);

create table uf_kotei2_zandaka (
  uf_kotei2_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei2_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei2_zandaka_PKEY primary key (uf_kotei2_cd,kamoku_gaibu_cd)
);

create table uf_kotei3_ichiran (
  uf_kotei3_cd character varying(20) not null
  , uf_kotei3_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei3_ichiran_PKEY primary key (uf_kotei3_cd)
);

create table uf_kotei3_zandaka (
  uf_kotei3_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei3_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei3_zandaka_PKEY primary key (uf_kotei3_cd,kamoku_gaibu_cd)
);

create table uf_kotei4_ichiran (
  uf_kotei4_cd character varying(20) not null
  , uf_kotei4_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei4_ichiran_PKEY primary key (uf_kotei4_cd)
);

create table uf_kotei4_zandaka (
  uf_kotei4_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei4_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei4_zandaka_PKEY primary key (uf_kotei4_cd,kamoku_gaibu_cd)
);

create table uf_kotei5_ichiran (
  uf_kotei5_cd character varying(20) not null
  , uf_kotei5_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei5_ichiran_PKEY primary key (uf_kotei5_cd)
);

create table uf_kotei5_zandaka (
  uf_kotei5_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei5_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei5_zandaka_PKEY primary key (uf_kotei5_cd,kamoku_gaibu_cd)
);

create table uf_kotei6_ichiran (
  uf_kotei6_cd character varying(20) not null
  , uf_kotei6_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei6_ichiran_PKEY primary key (uf_kotei6_cd)
);

create table uf_kotei6_zandaka (
  uf_kotei6_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei6_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei6_zandaka_PKEY primary key (uf_kotei6_cd,kamoku_gaibu_cd)
);

create table uf_kotei7_ichiran (
  uf_kotei7_cd character varying(20) not null
  , uf_kotei7_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei7_ichiran_PKEY primary key (uf_kotei7_cd)
);

create table uf_kotei7_zandaka (
  uf_kotei7_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei7_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei7_zandaka_PKEY primary key (uf_kotei7_cd,kamoku_gaibu_cd)
);

create table uf_kotei8_ichiran (
  uf_kotei8_cd character varying(20) not null
  , uf_kotei8_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei8_ichiran_PKEY primary key (uf_kotei8_cd)
);

create table uf_kotei8_zandaka (
  uf_kotei8_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei8_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei8_zandaka_PKEY primary key (uf_kotei8_cd,kamoku_gaibu_cd)
);

create table uf_kotei9_ichiran (
  uf_kotei9_cd character varying(20) not null
  , uf_kotei9_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf_kotei9_ichiran_PKEY primary key (uf_kotei9_cd)
);

create table uf_kotei9_zandaka (
  uf_kotei9_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf_kotei9_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf_kotei9_zandaka_PKEY primary key (uf_kotei9_cd,kamoku_gaibu_cd)
);

create table uf1_ichiran (
  uf1_cd character varying(20) not null
  , uf1_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf1_ichiran_PKEY primary key (uf1_cd)
);

create table uf1_zandaka (
  uf1_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf1_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf1_zandaka_PKEY primary key (uf1_cd,kamoku_gaibu_cd)
);

create table uf10_ichiran (
  uf10_cd character varying(20) not null
  , uf10_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf10_ichiran_PKEY primary key (uf10_cd)
);

create table uf10_zandaka (
  uf10_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf10_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf10_zandaka_PKEY primary key (uf10_cd,kamoku_gaibu_cd)
);

create table uf2_ichiran (
  uf2_cd character varying(20) not null
  , uf2_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf2_ichiran_PKEY primary key (uf2_cd)
);

create table uf2_zandaka (
  uf2_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf2_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf2_zandaka_PKEY primary key (uf2_cd,kamoku_gaibu_cd)
);

create table uf3_ichiran (
  uf3_cd character varying(20) not null
  , uf3_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf3_ichiran_PKEY primary key (uf3_cd)
);

create table uf3_zandaka (
  uf3_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf3_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22) not null
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf3_zandaka_PKEY primary key (uf3_cd,kamoku_gaibu_cd)
);

create table uf4_ichiran (
  uf4_cd character varying(20) not null
  , uf4_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf4_ichiran_PKEY primary key (uf4_cd)
);

create table uf4_zandaka (
  uf4_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf4_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf4_zandaka_PKEY primary key (uf4_cd,kamoku_gaibu_cd)
);

create table uf5_ichiran (
  uf5_cd character varying(20) not null
  , uf5_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf5_ichiran_PKEY primary key (uf5_cd)
);

create table uf5_zandaka (
  uf5_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf5_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf5_zandaka_PKEY primary key (uf5_cd,kamoku_gaibu_cd)
);

create table uf6_ichiran (
  uf6_cd character varying(20) not null
  , uf6_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf6_ichiran_PKEY primary key (uf6_cd)
);

create table uf6_zandaka (
  uf6_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf6_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf6_zandaka_PKEY primary key (uf6_cd,kamoku_gaibu_cd)
);

create table uf7_ichiran (
  uf7_cd character varying(20) not null
  , uf7_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf7_ichiran_PKEY primary key (uf7_cd)
);

create table uf7_zandaka (
  uf7_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf7_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf7_zandaka_PKEY primary key (uf7_cd,kamoku_gaibu_cd)
);

create table uf8_ichiran (
  uf8_cd character varying(20) not null
  , uf8_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf8_ichiran_PKEY primary key (uf8_cd)
);

create table uf8_zandaka (
  uf8_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf8_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf8_zandaka_PKEY primary key (uf8_cd,kamoku_gaibu_cd)
);

create table uf9_ichiran (
  uf9_cd character varying(20) not null
  , uf9_name_ryakushiki character varying(20) not null
  , kessanki_bangou smallint
  , constraint uf9_ichiran_PKEY primary key (uf9_cd)
);

create table uf9_zandaka (
  uf9_cd character varying(20) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kessanki_bangou smallint
  , kamoku_naibu_cd character varying(15) not null
  , uf9_name_ryakushiki character varying(20) not null
  , chouhyou_shaturyoku_no smallint
  , kamoku_name_ryakushiki character varying(22)
  , kamoku_name_seishiki character varying(40) not null
  , taishaku_zokusei smallint not null
  , kishu_zandaka numeric(19) not null
  , constraint uf9_zandaka_PKEY primary key (uf9_cd,kamoku_gaibu_cd)
);

create table user_default_value (
  kbn character varying(50) not null
  , user_id character varying(30) not null
  , default_value character varying(1) not null
  , constraint user_default_value_PKEY primary key (kbn,user_id)
);

create table user_info (
  user_id character varying(30) not null
  , shain_no character varying(15) not null
  , user_sei character varying(10) not null
  , user_mei character varying(10) not null
  , mail_address character varying(50) not null
  , yuukou_kigen_from date not null
  , yuukou_kigen_to date not null
  , touroku_user_id character varying(30) not null
  , touroku_time timestamp without time zone not null
  , koushin_user_id character varying(30) not null
  , koushin_time timestamp without time zone not null
  , pass_koushin_date date
  , pass_failure_count integer default 0 not null
  , pass_failure_time timestamp without time zone
  , tmp_lock_flg character varying(1) not null
  , tmp_lock_time timestamp without time zone
  , lock_flg character varying(1) default '0' not null
  , lock_time timestamp without time zone
  , dairikihyou_flg character varying(1) not null
  , houjin_card_riyou_flag character varying(1) not null
  , houjin_card_shikibetsuyou_num character varying(20) not null
  , security_pattern character varying(4) not null
  , security_wfonly_flg character varying(1) not null
  , shounin_route_henkou_level character varying(1) not null
  , maruhi_kengen_flg character varying(1) not null
  , maruhi_kaijyo_flg character varying(1) not null
  , zaimu_kyoten_nyuryoku_only_flg character varying(1) default '0' not null
  , constraint user_info_PKEY primary key (user_id)
);

create table version (
  version character varying not null
);

create table yosan_kiankingaku_check (
  denpyou_id character varying(19) not null
  , syuukei_bumon_cd character varying(8) not null
  , kamoku_gaibu_cd character varying(8) not null
  , kamoku_edaban_cd character varying(12) not null
  , futan_bumon_cd character varying(8) not null
  , syuukei_bumon_name character varying(20) not null
  , kamoku_name_ryakushiki character varying(22) not null
  , edaban_name character varying(20) not null
  , futan_bumon_name character varying(20) not null
  , kijun_kingaku numeric(15)
  , jissekigaku numeric(15)
  , shinsei_kingaku numeric(15)
  , constraint yosan_kiankingaku_check_PKEY primary key (denpyou_id,syuukei_bumon_cd,kamoku_gaibu_cd,kamoku_edaban_cd,futan_bumon_cd)
);

create table yosan_kiankingaku_check_comment (
  denpyou_id character varying(19) not null
  , comment character varying(220) not null
  , constraint yosan_kiankingaku_check_comment_PKEY primary key (denpyou_id)
);

create table yosan_shikkou_shori_nengetsu (
  from_nengetsu character varying(6) not null
  , to_nengetsu character varying(6) not null
  , constraint yosan_shikkou_shori_nengetsu_PKEY primary key (from_nengetsu,to_nengetsu)
);

create table ebunsho_kensaku (
  doc_type smallint not null
  , item_no smallint not null
  , nini_flg smallint DEFAULT 0 not null
  , constraint ebunsho_kensaku_PKEY primary key (doc_type,item_no)
);

-- �C���{�C�X�J�n�e�[�u���̍쐬
create table if not exists invoice_start (
  invoice_flg character varying(1) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp(6) without time zone not null
);

comment on table ebunsho_kensaku is 'e���������ݒ�';
comment on column ebunsho_kensaku.doc_type is '���ގ��';
comment on column ebunsho_kensaku.item_no is '����';
comment on column ebunsho_kensaku.nini_flg is '�C�Ӄt���O';

comment on table batch_haita_seigyo is '�o�b�`�r������';
comment on column batch_haita_seigyo.dummy is '�_�~�[';

comment on table batch_log is '�o�b�`���O';
comment on column batch_log.serial_no is '�V���A���ԍ�';
comment on column batch_log.start_time is '�J�n����';
comment on column batch_log.end_time is '�I������';
comment on column batch_log.batch_name is '�o�b�`��';
comment on column batch_log.batch_status is '�o�b�`�X�e�[�^�X';
comment on column batch_log.count_name is '������';
comment on column batch_log.count is '����';
comment on column batch_log.batch_kbn is '�o�b�`�敪';

comment on table batch_log_invalid_denpyou_log is '�s�Ǔ`�[���O';
comment on column batch_log_invalid_denpyou_log.file_name is '�t�@�C����';
comment on column batch_log_invalid_denpyou_log.denpyou_start_gyou is '�`�[�J�n�s';
comment on column batch_log_invalid_denpyou_log.denpyou_end_gyou is '�`�[�I���s';
comment on column batch_log_invalid_denpyou_log.denpyou_date is '�`�[���t';
comment on column batch_log_invalid_denpyou_log.denpyou_bangou is '�`�[�ԍ�';
comment on column batch_log_invalid_denpyou_log.taishaku_sagaku_kingaku is '�ݎ؍��z���z';
comment on column batch_log_invalid_denpyou_log.gaiyou is '�T�v';
comment on column batch_log_invalid_denpyou_log.naiyou is '���e';

comment on table batch_log_invalid_file_log is '�s�ǃf�[�^���O';
comment on column batch_log_invalid_file_log.file_name is '�t�@�C����';
comment on column batch_log_invalid_file_log.gyou_no is '�sNo';
comment on column batch_log_invalid_file_log.koumoku_no is '����No';
comment on column batch_log_invalid_file_log.koumoku_name is '���ږ���';
comment on column batch_log_invalid_file_log.invalid_value is '�s���Ȓl';
comment on column batch_log_invalid_file_log.error_naiyou is '�G���[���e';

comment on table batch_log_invalid_log_himoduke is '�o�b�`���O�s�ǃ��O�R�Â�';
comment on column batch_log_invalid_log_himoduke.serial_no is '�V���A���ԍ�';
comment on column batch_log_invalid_log_himoduke.edaban is '�}��';
comment on column batch_log_invalid_log_himoduke.file_name is '�t�@�C����';

comment on table bookmark is '�u�b�N�}�[�N';
comment on column bookmark.user_id is '���[�U�[ID';
comment on column bookmark.denpyou_kbn is '�`�[�敪';
comment on column bookmark.hyouji_jun is '�\����';
comment on column bookmark.memo is '����';
comment on column bookmark.touroku_user_id is '�o�^���[�U�[ID';
comment on column bookmark.touroku_time is '�o�^����';
comment on column bookmark.koushin_user_id is '�X�V���[�U�[ID';
comment on column bookmark.koushin_time is '�X�V����';

comment on table bumon_kamoku_edaban_yosan is '���S����Ȗڎ}�ԗ\�Z';
comment on column bumon_kamoku_edaban_yosan.futan_bumon_cd is '���S����R�[�h';
comment on column bumon_kamoku_edaban_yosan.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column bumon_kamoku_edaban_yosan.kessanki_bangou is '���Z���ԍ�';
comment on column bumon_kamoku_edaban_yosan.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column bumon_kamoku_edaban_yosan.edaban_code is '�}�ԃR�[�h';
comment on column bumon_kamoku_edaban_yosan.futan_bumon_name is '���S���喼';
comment on column bumon_kamoku_edaban_yosan.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column bumon_kamoku_edaban_yosan.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_edaban_yosan.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_edaban_yosan.taishaku_zokusei is '�ݎؑ���';
comment on column bumon_kamoku_edaban_yosan.yosan_01 is '1����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_02 is '2����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_03 is '3����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_03_shu is '3����ڏC���\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_04 is '4����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_05 is '5����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_06 is '6����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_06_shu is '6����ڏC���\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_07 is '7����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_08 is '8����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_09 is '9����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_09_shu is '9����ڏC���\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_10 is '10����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_11 is '11����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_12 is '12����ڗ\�Z';
comment on column bumon_kamoku_edaban_yosan.yosan_12_shu is '12����ڏC���\�Z';

comment on table bumon_kamoku_edaban_zandaka is '���S����Ȗڎ}�Ԏc��';
comment on column bumon_kamoku_edaban_zandaka.futan_bumon_cd is '���S����R�[�h';
comment on column bumon_kamoku_edaban_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column bumon_kamoku_edaban_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column bumon_kamoku_edaban_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column bumon_kamoku_edaban_zandaka.edaban_code is '�}�ԃR�[�h';
comment on column bumon_kamoku_edaban_zandaka.futan_bumon_name is '���S���喼';
comment on column bumon_kamoku_edaban_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column bumon_kamoku_edaban_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_edaban_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_edaban_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari00 is '����c��(�ؕ�)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi00 is '����c��(�ݕ�)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari01 is '1�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi01 is '1�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari02 is '2�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi02 is '2�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari03 is '3�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi03 is '3�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari03_shu is '3����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi03_shu is '3����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari04 is '4�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi04 is '4�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari05 is '5�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi05 is '5�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari06 is '6�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi06 is '6�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari06_shu is '6����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi06_shu is '6����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari07 is '7�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi07 is '7�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari08 is '8�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi08 is '8�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari09 is '9�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi09 is '9�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari09_shu is '9����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi09_shu is '9����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari10 is '10�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi10 is '10�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari11 is '11�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi11 is '11�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari12 is '12�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi12 is '12�����(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari12_shu is '12����ڏC��(��)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi12_shu is '12����ڏC��(��)';
comment on column kamoku_edaban_zandaka.kazei_kbn is '�ېŋ敪';
comment on column kamoku_edaban_zandaka.bunri_kbn is '�����敪';


comment on table bumon_kamoku_yosan is '���S����Ȗڗ\�Z';
comment on column bumon_kamoku_yosan.futan_bumon_cd is '���S����R�[�h';
comment on column bumon_kamoku_yosan.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column bumon_kamoku_yosan.kessanki_bangou is '���Z���ԍ�';
comment on column bumon_kamoku_yosan.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column bumon_kamoku_yosan.futan_bumon_name is '���S���喼';
comment on column bumon_kamoku_yosan.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column bumon_kamoku_yosan.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_yosan.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_yosan.taishaku_zokusei is '�ݎؑ���';
comment on column bumon_kamoku_yosan.yosan_01 is '1����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_02 is '2����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_03 is '3����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_03_shu is '3����ڏC���\�Z';
comment on column bumon_kamoku_yosan.yosan_04 is '4����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_05 is '5����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_06 is '6����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_06_shu is '6����ڏC���\�Z';
comment on column bumon_kamoku_yosan.yosan_07 is '7����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_08 is '8����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_09 is '9����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_09_shu is '9����ڏC���\�Z';
comment on column bumon_kamoku_yosan.yosan_10 is '10����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_11 is '11����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_12 is '12����ڗ\�Z';
comment on column bumon_kamoku_yosan.yosan_12_shu is '12����ڏC���\�Z';

comment on table bumon_kamoku_zandaka is '���S����Ȗڎc��';
comment on column bumon_kamoku_zandaka.futan_bumon_cd is '���S����R�[�h';
comment on column bumon_kamoku_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column bumon_kamoku_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column bumon_kamoku_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column bumon_kamoku_zandaka.futan_bumon_name is '���S���喼';
comment on column bumon_kamoku_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column bumon_kamoku_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column bumon_kamoku_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column bumon_kamoku_zandaka.zandaka_kari00 is '����c��(�ؕ�)';
comment on column bumon_kamoku_zandaka.zandaka_kashi00 is '����c��(�ݕ�)';
comment on column bumon_kamoku_zandaka.zandaka_kari01 is '1�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi01 is '1�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari02 is '2�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi02 is '2�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari03 is '3�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi03 is '3�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari03_shu is '3����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi03_shu is '3����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari04 is '4�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi04 is '4�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari05 is '5�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi05 is '5�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari06 is '6�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi06 is '6�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari06_shu is '6����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi06_shu is '6����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari07 is '7�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi07 is '7�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari08 is '8�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi08 is '8�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari09 is '9�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi09 is '9�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari09_shu is '9����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi09_shu is '9����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari10 is '10�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi10 is '10�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari11 is '11�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi11 is '11�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari12 is '12�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi12 is '12�����(��)';
comment on column bumon_kamoku_zandaka.zandaka_kari12_shu is '12����ڏC��(��)';
comment on column bumon_kamoku_zandaka.zandaka_kashi12_shu is '12����ڏC��(��)';

comment on table bumon_master is '���S����';
comment on column bumon_master.futan_bumon_cd is '���S����R�[�h';
comment on column bumon_master.futan_bumon_name is '���S���喼';
comment on column bumon_master.kessanki_bangou is '���Z���ԍ�';
comment on column bumon_master.shaukei_bumon_flg is '�W�v����t���O';
comment on column bumon_master.shiire_kbn is '�d���敪';
comment on column bumon_master.nyuryoku_from_date is '���͊J�n��';
comment on column bumon_master.nyuryoku_to_date is '���͏I����';

comment on table bumon_role is '���働�[��';
comment on column bumon_role.bumon_role_id is '���働�[��ID';
comment on column bumon_role.bumon_role_name is '���働�[����';
comment on column bumon_role.hyouji_jun is '�\����';
comment on column bumon_role.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_role.touroku_time is '�o�^����';
comment on column bumon_role.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_role.koushin_time is '�X�V����';

comment on table bumon_suishou_route_ko is '���各�����[�g�q';
comment on column bumon_suishou_route_ko.denpyou_kbn is '�`�[�敪';
comment on column bumon_suishou_route_ko.bumon_cd is '����R�[�h';
comment on column bumon_suishou_route_ko.edano is '�}�ԍ�';
comment on column bumon_suishou_route_ko.edaedano is '�}�}�ԍ�';
comment on column bumon_suishou_route_ko.bumon_role_id is '���働�[��ID';
comment on column bumon_suishou_route_ko.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column bumon_suishou_route_ko.gougi_pattern_no is '���c�p�^�[���ԍ�';
comment on column bumon_suishou_route_ko.gougi_edano is '���c�}��';
comment on column bumon_suishou_route_ko.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_suishou_route_ko.touroku_time is '�o�^����';
comment on column bumon_suishou_route_ko.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_suishou_route_ko.koushin_time is '�X�V����';

comment on table bumon_suishou_route_oya is '���各�����[�g�e';
comment on column bumon_suishou_route_oya.denpyou_kbn is '�`�[�敪';
comment on column bumon_suishou_route_oya.bumon_cd is '����R�[�h';
comment on column bumon_suishou_route_oya.edano is '�}�ԍ�';
comment on column bumon_suishou_route_oya.default_flg is '�f�t�H���g�t���O';
comment on column bumon_suishou_route_oya.shiwake_edano is '�d��}�ԍ�';
comment on column bumon_suishou_route_oya.kingaku_from is '���z�J�n';
comment on column bumon_suishou_route_oya.kingaku_to is '���z�I��';
comment on column bumon_suishou_route_oya.yuukou_kigen_from is '�L�������J�n��';
comment on column bumon_suishou_route_oya.yuukou_kigen_to is '�L�������I����';
comment on column bumon_suishou_route_oya.touroku_user_id is '�o�^���[�U�[ID';
comment on column bumon_suishou_route_oya.touroku_time is '�o�^����';
comment on column bumon_suishou_route_oya.koushin_user_id is '�X�V���[�U�[ID';
comment on column bumon_suishou_route_oya.koushin_time is '�X�V����';

comment on table bus_line_master is '�o�X�H�����}�X�^�[';
comment on column bus_line_master.line_cd is '�o�X�H���R�[�h';
comment on column bus_line_master.line_name is '�H����';

comment on table daikou_shitei is '��s�w��';
comment on column daikou_shitei.daikou_user_id is '��s���[�U�[ID';
comment on column daikou_shitei.hi_daikou_user_id is '���s���[�U�[ID';
comment on column daikou_shitei.touroku_user_id is '�o�^���[�U�[ID';
comment on column daikou_shitei.touroku_time is '�o�^����';
comment on column daikou_shitei.koushin_user_id is '�X�V���[�U�[ID';
comment on column daikou_shitei.koushin_time is '�X�V����';

comment on table denpyou is '�`�[';
comment on column denpyou.denpyou_id is '�`�[ID';
comment on column denpyou.denpyou_kbn is '�`�[�敪';
comment on column denpyou.denpyou_joutai is '�`�[���';
comment on column denpyou.sanshou_denpyou_id is '�Q�Ɠ`�[ID';
comment on column denpyou.daihyou_futan_bumon_cd is '��\���S����R�[�h';
comment on column denpyou.touroku_user_id is '�o�^���[�U�[ID';
comment on column denpyou.touroku_time is '�o�^����';
comment on column denpyou.koushin_user_id is '�X�V���[�U�[ID';
comment on column denpyou.koushin_time is '�X�V����';
comment on column denpyou.serial_no is '�V���A���ԍ�';
comment on column denpyou.chuushutsu_zumi_flg is '���o�σt���O';
comment on column denpyou.shounin_route_henkou_flg is '���F���[�g�ύX�t���O';
comment on column denpyou.maruhi_flg is '�}���镶���t���O';
comment on column denpyou.yosan_check_nengetsu is '�\�Z���s�Ώی�';

comment on table denpyou_ichiran is '�`�[�ꗗ';
comment on column denpyou_ichiran.denpyou_id is '�`�[ID';
comment on column denpyou_ichiran.name is '�X�e�[�^�X';
comment on column denpyou_ichiran.denpyou_kbn is '�`�[�敪';
comment on column denpyou_ichiran.jisshi_kian_bangou is '���{�N�Ĕԍ�';
comment on column denpyou_ichiran.shishutsu_kian_bangou is '�x�o�N�Ĕԍ�';
comment on column denpyou_ichiran.yosan_shikkou_taishou is '�\�Z���s�Ώ�';
comment on column denpyou_ichiran.yosan_check_nengetsu is '�\�Z���s�Ώی�';
comment on column denpyou_ichiran.serial_no is '�V���A���ԍ�';
comment on column denpyou_ichiran.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_ichiran.touroku_time is '�o�^����';
comment on column denpyou_ichiran.bumon_full_name is '����t����';
comment on column denpyou_ichiran.user_full_name is '���[�U�[�t����';
comment on column denpyou_ichiran.user_id is '���[�U�[ID';
comment on column denpyou_ichiran.denpyou_joutai is '�`�[���';
comment on column denpyou_ichiran.koushin_time is '�X�V����';
comment on column denpyou_ichiran.shouninbi is '���F��';
comment on column denpyou_ichiran.maruhi_flg is '�}���镶���t���O';
comment on column denpyou_ichiran.all_cnt is '�S���F�l���J�E���g';
comment on column denpyou_ichiran.cur_cnt is '���F�ϐl���J�E���g';
comment on column denpyou_ichiran.zan_cnt is '�c�菳�F�l���J�E���g';
comment on column denpyou_ichiran.gen_bumon_full_name is '���ݏ��F�ҕ���t����';
comment on column denpyou_ichiran.gen_user_full_name is '���ݏ��F�҃��[�U�[�t����';
comment on column denpyou_ichiran.gen_gyoumu_role_name is '���ݏ��F�ҋƖ����[����';
comment on column denpyou_ichiran.gen_name is '���ݏ��F�Җ���';
comment on column denpyou_ichiran.version is '�o�[�W����';
comment on column denpyou_ichiran.kingaku is '���z';
comment on column denpyou_ichiran.gaika is '�O��';
comment on column denpyou_ichiran.houjin_kingaku is '�@�l�J�[�h�����z';
comment on column denpyou_ichiran.tehai_kingaku is '��Ў�z���z';
comment on column denpyou_ichiran.torihikisaki1 is '�����1';
comment on column denpyou_ichiran.shiharaibi is '�x����';
comment on column denpyou_ichiran.shiharaikiboubi is '�x����]��';
comment on column denpyou_ichiran.shiharaihouhou is '�x�����@';
comment on column denpyou_ichiran.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column denpyou_ichiran.keijoubi is '�v���';
comment on column denpyou_ichiran.shiwakekeijoubi is '�d��v���';
comment on column denpyou_ichiran.seisan_yoteibi is '���Z�\���';
comment on column denpyou_ichiran.karibarai_denpyou_id is '�����`�[ID';
comment on column denpyou_ichiran.houmonsaki is '�K���';
comment on column denpyou_ichiran.mokuteki is '�ړI';
comment on column denpyou_ichiran.kenmei is '����';
comment on column denpyou_ichiran.naiyou is '���e';
comment on column denpyou_ichiran.user_sei is '���[�U�[��';
comment on column denpyou_ichiran.user_mei is '���[�U�[��';
comment on column denpyou_ichiran.seisankikan_from is '���Z���ԊJ�n��';
comment on column denpyou_ichiran.seisankikan_to is '���Z���ԏI����';
comment on column denpyou_ichiran.gen_user_id is '���ݏ��F�҃��[�U�[ID���X�g';
comment on column denpyou_ichiran.gen_gyoumu_role_id is '���ݏ��F�ҋƖ����[��ID���X�g';
comment on column denpyou_ichiran.kian_bangou_unyou_flg is '�N�Ĕԍ��^�p�t���O';
comment on column denpyou_ichiran.yosan_shikkou_taishou_cd is '�\�Z���s�ΏۃR�[�h';
comment on column denpyou_ichiran.kian_syuryou_flg is '�N�ďI���t���O';
comment on column denpyou_ichiran.futan_bumon_cd is '���S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_futan_bumon_cd is '�ؕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_kamoku_cd is '�ؕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kari_torihikisaki_cd is '�ؕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_futan_bumon_cd is '�ݕ����S����R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.kashi_torihikisaki_cd is '�ݕ������R�[�h(�ꗗ�����p)';
comment on column denpyou_ichiran.meisai_kingaku is '���׋��z(�ꗗ�����p)';
comment on column denpyou_ichiran.tekiyou is '�E�v(�ꗗ�����p)';
comment on column denpyou_ichiran.houjin_card_use is '�@�l�J�[�h�g�p�t���O';
comment on column denpyou_ichiran.kaisha_tehai_use is '��Ў�z�g�p�t���O';
comment on column denpyou_ichiran.ryoushuusho_exist is '�̎����t���O';
comment on column denpyou_ichiran.miseisan_karibarai_exist is '�����Z�����`�[�t���O';
comment on column denpyou_ichiran.miseisan_ukagai_exist is '�����Z�f���`�[�t���O';
comment on column denpyou_ichiran.shiwake_status is '�d��f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran.fb_status is 'FB�f�[�^�쐬�X�e�[�^�X';
comment on column denpyou_ichiran.jisshi_nendo is '���{�N�x';
comment on column denpyou_ichiran.shishutsu_nendo is '�x�o�N�x';
comment on column denpyou_ichiran.bumon_cd is '����R�[�h';
comment on column denpyou_ichiran.kian_bangou_input is '�N�Ĕԍ��̔ԃt���O';
comment on column denpyou_ichiran.jigyousha_kbn is '���Ǝҋ敪';
comment on column denpyou_ichiran.shain_no is '�Ј��ԍ��i�N�[�ҁj';
comment on column denpyou_ichiran.shiharai_name is '�x���於(�ꗗ�����p)';
comment on column denpyou_ichiran.zeinuki_meisai_kingaku is '�Ŕ����׋��z(�ꗗ�����p)';
comment on column denpyou_ichiran.zeinuki_kingaku is '�Ŕ����z';

comment on table denpyou_id_saiban is '�`�[ID�̔�';
comment on column denpyou_id_saiban.touroku_date is '�o�^��';
comment on column denpyou_id_saiban.denpyou_kbn is '�`�[�敪';
comment on column denpyou_id_saiban.sequence_val is '�V�[�P���X�l';

comment on table denpyou_kian_himozuke is '�`�[�N�ĕR�t';
comment on column denpyou_kian_himozuke.denpyou_id is '�`�[ID';
comment on column denpyou_kian_himozuke.bumon_cd is '����R�[�h';
comment on column denpyou_kian_himozuke.nendo is '�N�x';
comment on column denpyou_kian_himozuke.ryakugou is '����';
comment on column denpyou_kian_himozuke.kian_bangou_from is '�J�n�N�Ĕԍ�';
comment on column denpyou_kian_himozuke.kian_bangou is '�N�Ĕԍ�';
comment on column denpyou_kian_himozuke.kian_syuryo_flg is '�N�ďI���t���O';
comment on column denpyou_kian_himozuke.kian_syuryo_bi is '�N�ďI����';
comment on column denpyou_kian_himozuke.kian_denpyou is '�N�ē`�[';
comment on column denpyou_kian_himozuke.kian_denpyou_kbn is '�N�ē`�[�敪';
comment on column denpyou_kian_himozuke.jisshi_nendo is '���{�N�x';
comment on column denpyou_kian_himozuke.jisshi_kian_bangou is '���{�N�Ĕԍ�';
comment on column denpyou_kian_himozuke.shishutsu_nendo is '�x�o�N�x';
comment on column denpyou_kian_himozuke.shishutsu_kian_bangou is '�x�o�N�Ĕԍ�';
comment on column denpyou_kian_himozuke.ringi_kingaku is '�g�c���z';
comment on column denpyou_kian_himozuke.ringi_kingaku_hikitsugimoto_denpyou is '�g�c���z���p�����`�[';
comment on column denpyou_kian_himozuke.ringi_kingaku_chouka_comment is '�g�c���z���߃R�����g';

comment on table denpyou_serial_no_saiban is '�`�[�ԍ��̔�';
comment on column denpyou_serial_no_saiban.sequence_val is '�V�[�P���X�l';
comment on column denpyou_serial_no_saiban.max_value is '�ő�l';
comment on column denpyou_serial_no_saiban.min_value is '�ŏ��l';

comment on table denpyou_shubetsu_ichiran is '�`�[��ʈꗗ';
comment on column denpyou_shubetsu_ichiran.denpyou_kbn is '�`�[�敪';
comment on column denpyou_shubetsu_ichiran.version is '�o�[�W����';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu is '�`�[���';
comment on column denpyou_shubetsu_ichiran.denpyou_karibarai_nashi_shubetsu is '�`�[��ʁi�����Ȃ��j';
comment on column denpyou_shubetsu_ichiran.denpyou_print_shubetsu is '�`�[��ʁi���[�j';
comment on column denpyou_shubetsu_ichiran.denpyou_print_karibarai_nashi_shubetsu is '�`�[��ʁi���[�E�����Ȃ��j';
comment on column denpyou_shubetsu_ichiran.hyouji_jun is '�\����';
comment on column denpyou_shubetsu_ichiran.gyoumu_shubetsu is '�Ɩ����';
comment on column denpyou_shubetsu_ichiran.naiyou is '���e�i�`�[�j';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu_url is '�`�[���URL';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_from is '�L�������J�n��';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_to is '�L�������I����';
comment on column denpyou_shubetsu_ichiran.kanren_sentaku_flg is '�֘A�`�[�I���t���O';
comment on column denpyou_shubetsu_ichiran.kanren_hyouji_flg is '�֘A�`�[���͗��\���t���O';
comment on column denpyou_shubetsu_ichiran.denpyou_print_flg is '�\�������[�o�̓t���O';
comment on column denpyou_shubetsu_ichiran.kianbangou_unyou_flg is '�N�Ĕԍ��^�p�t���O';
comment on column denpyou_shubetsu_ichiran.yosan_shikkou_taishou is '�\�Z���s�Ώ�';
comment on column denpyou_shubetsu_ichiran.route_hantei_kingaku is '���[�g������z';
comment on column denpyou_shubetsu_ichiran.route_torihiki_flg is '���[�g������ݒ�t���O';
comment on column denpyou_shubetsu_ichiran.shounin_jyoukyou_print_flg is '���F�󋵗�����t���O';
comment on column denpyou_shubetsu_ichiran.shinsei_shori_kengen_name is '�\������������';
comment on column denpyou_shubetsu_ichiran.shiiresaki_flg is '�d����t���O';
comment on column denpyou_shubetsu_ichiran.touroku_user_id is '�o�^���[�U�[ID';
comment on column denpyou_shubetsu_ichiran.touroku_time is '�o�^����';
comment on column denpyou_shubetsu_ichiran.koushin_user_id is '�X�V���[�U�[ID';
comment on column denpyou_shubetsu_ichiran.koushin_time is '�X�V����';

comment on table ebunsho_data is 'e�����f�[�^';
comment on column ebunsho_data.ebunsho_no is 'e�����ԍ�';
comment on column ebunsho_data.ebunsho_edano is 'e�����}�ԍ�';
comment on column ebunsho_data.ebunsho_shubetsu is 'e�������';
comment on column ebunsho_data.ebunsho_nengappi is 'e�����N����';
comment on column ebunsho_data.ebunsho_kingaku is 'e�������z';
comment on column ebunsho_data.ebunsho_hakkousha is 'e�������s��';
comment on column ebunsho_data.ebunsho_hinmei is 'e�����i��';

comment on table ebunsho_file is 'e�����t�@�C��';
comment on column ebunsho_file.denpyou_id is '�`�[ID';
comment on column ebunsho_file.edano is '�}�ԍ�';
comment on column ebunsho_file.ebunsho_no is 'e�����ԍ�';
comment on column ebunsho_file.binary_data is '�o�C�i���[�f�[�^';
comment on column ebunsho_file.denshitorihiki_flg is '�d�q����t���O';
comment on column ebunsho_file.tsfuyo_flg is '�^�C���X�^���v�t�^�t���O';
comment on column ebunsho_file.touroku_user_id is '�o�^���[�U�[ID';
comment on column ebunsho_file.touroku_time is '�o�^����';

comment on table eki_master is '�w�}�X�^�[';
comment on column eki_master.region_cd is '�n��R�[�h';
comment on column eki_master.line_cd is '�H���R�[�h';
comment on column eki_master.eki_cd is '�w�R�[�h';
comment on column eki_master.line_name is '�H����';
comment on column eki_master.eki_name is '�w��';

comment on table event_log is '�C�x���g���O';
comment on column event_log.serial_no is '�V���A���ԍ�';
comment on column event_log.start_time is '�J�n����';
comment on column event_log.end_time is '�I������';
comment on column event_log.user_id is '���[�U�[ID';
comment on column event_log.gamen_id is '���ID';
comment on column event_log.event_id is '�C�x���gID';
comment on column event_log.result is '��������';

comment on table extension_setting is '�g���q�ݒ�';
comment on column extension_setting.extension_cd is '�g���q�R�[�h';
comment on column extension_setting.extension_flg is '�L���t���O';

comment on table fb is 'FB���o';
comment on column fb.serial_no is '�V���A���ԍ�';
comment on column fb.denpyou_id is '�`�[ID';
comment on column fb.user_id is '���[�U�[ID';
comment on column fb.fb_status is 'FB���o���';
comment on column fb.touroku_time is '�o�^����';
comment on column fb.koushin_time is '�X�V����';
comment on column fb.shubetsu_cd is '��ʃR�[�h';
comment on column fb.cd_kbn is '�R�[�h�敪';
comment on column fb.kaisha_cd is '��ЃR�[�h';
comment on column fb.kaisha_name_hankana is '��Ж��i���p�J�i�j';
comment on column fb.furikomi_date is '�U����';
comment on column fb.moto_kinyuukikan_cd is '�U�������Z�@�փR�[�h';
comment on column fb.moto_kinyuukikan_name_hankana is '�U�������Z�@�֖��i���p�J�i�j';
comment on column fb.moto_kinyuukikan_shiten_cd is '�U�������Z�@�֎x�X�R�[�h';
comment on column fb.moto_kinyuukikan_shiten_name_hankana is '�U�������Z�@�֎x�X���i���p�J�i�j';
comment on column fb.moto_yokin_shumoku_cd is '�U�����a����ڃR�[�h';
comment on column fb.moto_kouza_bangou is '�U���������ԍ�';
comment on column fb.saki_kinyuukikan_cd is '�U������Z�@�֋�s�R�[�h';
comment on column fb.saki_kinyuukikan_name_hankana is '�U������Z�@�֖��i���p�J�i�j';
comment on column fb.saki_kinyuukikan_shiten_cd is '�U������Z�@�֎x�X�R�[�h';
comment on column fb.saki_kinyuukikan_shiten_name_hankana is '�U������Z�@�֎x�X���i���p�J�i�j';
comment on column fb.saki_yokin_shumoku_cd is '�U����a����ڃR�[�h';
comment on column fb.saki_kouza_bangou is '�U��������ԍ�';
comment on column fb.saki_kouza_meigi_kana is '�U����������`�i���p�J�i�j';
comment on column fb.kingaku is '���z';
comment on column fb.shinki_cd is '�V�K�R�[�h';
comment on column fb.kokyaku_cd1 is '�ڋq�R�[�h�P';
comment on column fb.furikomi_kbn is '�U���敪';

comment on table furikae is '�U��';
comment on column furikae.denpyou_id is '�`�[ID';
comment on column furikae.denpyou_date is '�`�[���t';
comment on column furikae.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column furikae.kingaku is '���z';
comment on column furikae.hontai_kingaku is '�{�̋��z';
comment on column furikae.shouhizeigaku is '����Ŋz';
comment on column furikae.kari_zeiritsu is '�ؕ��ŗ�';
comment on column furikae.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪';
comment on column furikae.tekiyou is '�E�v';
comment on column furikae.hf1_cd is 'HF1�R�[�h';
comment on column furikae.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column furikae.hf2_cd is 'HF2�R�[�h';
comment on column furikae.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column furikae.hf3_cd is 'HF3�R�[�h';
comment on column furikae.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column furikae.hf4_cd is 'HF4�R�[�h';
comment on column furikae.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column furikae.hf5_cd is 'HF5�R�[�h';
comment on column furikae.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column furikae.hf6_cd is 'HF6�R�[�h';
comment on column furikae.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column furikae.hf7_cd is 'HF7�R�[�h';
comment on column furikae.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column furikae.hf8_cd is 'HF8�R�[�h';
comment on column furikae.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column furikae.hf9_cd is 'HF9�R�[�h';
comment on column furikae.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column furikae.hf10_cd is 'HF10�R�[�h';
comment on column furikae.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column furikae.bikou is '���l�i��v�`�[�j';
comment on column furikae.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column furikae.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column furikae.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column furikae.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column furikae.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column furikae.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column furikae.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column furikae.kari_torihikisaki_cd is '�ؕ������R�[�h';
comment on column furikae.kari_torihikisaki_name_ryakushiki is '�ؕ�����於�i�����j';
comment on column furikae.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column furikae.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column furikae.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column furikae.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column furikae.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column furikae.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column furikae.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column furikae.kashi_torihikisaki_cd is '�ݕ������R�[�h';
comment on column furikae.kashi_torihikisaki_name_ryakushiki is '�ݕ�����於�i�����j';
comment on column furikae.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column furikae.kari_uf1_name_ryakushiki is '�ؕ�UF1���i�����j';
comment on column furikae.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column furikae.kari_uf2_name_ryakushiki is '�ؕ�UF2���i�����j';
comment on column furikae.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column furikae.kari_uf3_name_ryakushiki is '�ؕ�UF3���i�����j';
comment on column furikae.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column furikae.kari_uf4_name_ryakushiki is '�ؕ�UF4���i�����j';
comment on column furikae.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column furikae.kari_uf5_name_ryakushiki is '�ؕ�UF5���i�����j';
comment on column furikae.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column furikae.kari_uf6_name_ryakushiki is '�ؕ�UF6���i�����j';
comment on column furikae.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column furikae.kari_uf7_name_ryakushiki is '�ؕ�UF7���i�����j';
comment on column furikae.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column furikae.kari_uf8_name_ryakushiki is '�ؕ�UF8���i�����j';
comment on column furikae.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column furikae.kari_uf9_name_ryakushiki is '�ؕ�UF9���i�����j';
comment on column furikae.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column furikae.kari_uf10_name_ryakushiki is '�ؕ�UF10���i�����j';
comment on column furikae.kashi_uf1_cd is '�ݕ�UF1�R�[�h';
comment on column furikae.kashi_uf1_name_ryakushiki is '�ݕ�UF1���i�����j';
comment on column furikae.kashi_uf2_cd is '�ݕ�UF2�R�[�h';
comment on column furikae.kashi_uf2_name_ryakushiki is '�ݕ�UF2���i�����j';
comment on column furikae.kashi_uf3_cd is '�ݕ�UF3�R�[�h';
comment on column furikae.kashi_uf3_name_ryakushiki is '�ݕ�UF3���i�����j';
comment on column furikae.kashi_uf4_cd is '�ݕ�UF4�R�[�h';
comment on column furikae.kashi_uf4_name_ryakushiki is '�ݕ�UF4���i�����j';
comment on column furikae.kashi_uf5_cd is '�ݕ�UF5�R�[�h';
comment on column furikae.kashi_uf5_name_ryakushiki is '�ݕ�UF5���i�����j';
comment on column furikae.kashi_uf6_cd is '�ݕ�UF6�R�[�h';
comment on column furikae.kashi_uf6_name_ryakushiki is '�ݕ�UF6���i�����j';
comment on column furikae.kashi_uf7_cd is '�ݕ�UF7�R�[�h';
comment on column furikae.kashi_uf7_name_ryakushiki is '�ݕ�UF7���i�����j';
comment on column furikae.kashi_uf8_cd is '�ݕ�UF8�R�[�h';
comment on column furikae.kashi_uf8_name_ryakushiki is '�ݕ�UF8���i�����j';
comment on column furikae.kashi_uf9_cd is '�ݕ�UF9�R�[�h';
comment on column furikae.kashi_uf9_name_ryakushiki is '�ݕ�UF9���i�����j';
comment on column furikae.kashi_uf10_cd is '�ݕ�UF10�R�[�h';
comment on column furikae.kashi_uf10_name_ryakushiki is '�ݕ�UF10���i�����j';
comment on column furikae.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h';
comment on column furikae.kari_project_name is '�ؕ��v���W�F�N�g��';
comment on column furikae.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column furikae.kari_segment_name_ryakushiki is '�ؕ��Z�O�����g���i�����j';
comment on column furikae.kashi_project_cd is '�ݕ��v���W�F�N�g�R�[�h';
comment on column furikae.kashi_project_name is '�ݕ��v���W�F�N�g��';
comment on column furikae.kashi_segment_cd is '�ݕ��Z�O�����g�R�[�h';
comment on column furikae.kashi_segment_name_ryakushiki is '�ݕ��Z�O�����g���i�����j';
comment on column furikae.touroku_user_id is '�o�^���[�U�[ID';
comment on column furikae.touroku_time is '�o�^����';
comment on column furikae.koushin_user_id is '�X�V���[�U�[ID';
comment on column furikae.koushin_time is '�X�V����';
comment on column furikae.kari_bunri_kbn is '�ؕ������敪';
comment on column furikae.kashi_bunri_kbn is '�ݕ������敪';
comment on column furikae.kari_jigyousha_kbn is '�ؕ����Ǝҋ敪';
comment on column furikae.kari_shiire_kbn is '�ؕ��d���敪';
comment on column furikae.kari_zeigaku_houshiki is '�ؕ��Ŋz�v�Z����';
comment on column furikae.kashi_jigyousha_kbn is '�ݕ����Ǝҋ敪';
comment on column furikae.kashi_shiire_kbn is '�ݕ��d���敪';
comment on column furikae.kashi_zeigaku_houshiki is '�ݕ��Ŋz�v�Z����';
comment on column furikae.invoice_denpyou is '�C���{�C�X�Ή��`�[';
comment on column furikae.kashi_zeiritsu is '�ݕ��ŗ�';
comment on column furikae.kashi_keigen_zeiritsu_kbn is '�ݕ��y���ŗ��敪';

comment on table furikomi_bi_rule_hi is '�U�������[��(���t)';
comment on column furikomi_bi_rule_hi.kijun_date is '���';
comment on column furikomi_bi_rule_hi.furikomi_date is '�U����';

comment on table furikomi_bi_rule_youbi is '�U�������[��(�j��)';
comment on column furikomi_bi_rule_youbi.kijun_weekday is '��j��';
comment on column furikomi_bi_rule_youbi.furikomi_weekday is '�U���j��';

comment on table gamen_kengen_seigyo is '��ʌ�������';
comment on column gamen_kengen_seigyo.gamen_id is '���ID';
comment on column gamen_kengen_seigyo.gamen_name is '��ʖ�';
comment on column gamen_kengen_seigyo.bumon_shozoku_riyoukanou_flg is '���及�����[�U�[���p�\�t���O';
comment on column gamen_kengen_seigyo.system_kanri_riyoukanou_flg is '�V�X�e���Ǘ����p�\�t���O';
comment on column gamen_kengen_seigyo.workflow_riyoukanou_flg is '���[�N�t���[���p�\�t���O';
comment on column gamen_kengen_seigyo.kaishasettei_riyoukanou_flg is '��Аݒ藘�p�\�t���O';
comment on column gamen_kengen_seigyo.keirishori_riyoukanou_flg is '�o�������������p�\�t���O';
comment on column gamen_kengen_seigyo.kinou_seigyo_cd is '�@�\����R�[�h';

comment on table gamen_koumoku_seigyo is '��ʍ��ڐ���';
comment on column gamen_koumoku_seigyo.denpyou_kbn is '�`�[�敪';
comment on column gamen_koumoku_seigyo.koumoku_id is '����ID';
comment on column gamen_koumoku_seigyo.default_koumoku_name is '�f�t�H���g���ږ�';
comment on column gamen_koumoku_seigyo.koumoku_name is '���ږ�';
comment on column gamen_koumoku_seigyo.hyouji_flg is '�\���t���O';
comment on column gamen_koumoku_seigyo.hissu_flg is '�K�{�t���O';
comment on column gamen_koumoku_seigyo.hyouji_seigyo_flg is '�\������t���O';
comment on column gamen_koumoku_seigyo.pdf_hyouji_flg is '���[�\���t���O';
comment on column gamen_koumoku_seigyo.pdf_hyouji_seigyo_flg is '���[�\������t���O';
comment on column gamen_koumoku_seigyo.code_output_flg is '���[�R�[�h�󎚃t���O';
comment on column gamen_koumoku_seigyo.code_output_seigyo_flg is '���[�R�[�h�󎚐���t���O';
comment on column gamen_koumoku_seigyo.hyouji_jun is '�\����';

comment on table gazou_file is '�摜�t�@�C��';
comment on column gazou_file.serial_no is '�V���A���ԍ�';
comment on column gazou_file.file_name is '�t�@�C����';
comment on column gazou_file.file_size is '�t�@�C���T�C�Y';
comment on column gazou_file.content_type is '�R���e���c�^�C�v';
comment on column gazou_file.binary_data is '�o�C�i���[�f�[�^';
comment on column gazou_file.touroku_user_id is '�o�^���[�U�[ID';
comment on column gazou_file.touroku_time is '�o�^����';
comment on column gazou_file.koushin_user_id is '�X�V���[�U�[ID';
comment on column gazou_file.koushin_time is '�X�V����';

comment on table gougi_pattern_ko is '���c�p�^�[���q';
comment on column gougi_pattern_ko.gougi_pattern_no is '���c�p�^�[���ԍ�';
comment on column gougi_pattern_ko.edano is '�}�ԍ�';
comment on column gougi_pattern_ko.bumon_cd is '����R�[�h';
comment on column gougi_pattern_ko.bumon_role_id is '���働�[��ID';
comment on column gougi_pattern_ko.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column gougi_pattern_ko.shounin_ninzuu_cd is '���F�l���R�[�h';
comment on column gougi_pattern_ko.shounin_ninzuu_hiritsu is '���F�l���䗦';
comment on column gougi_pattern_ko.touroku_user_id is '�o�^���[�U�[ID';
comment on column gougi_pattern_ko.touroku_time is '�o�^����';
comment on column gougi_pattern_ko.koushin_user_id is '�X�V���[�U�[ID';
comment on column gougi_pattern_ko.koushin_time is '�X�V����';

comment on table gougi_pattern_oya is '���c�p�^�[���e';
comment on column gougi_pattern_oya.gougi_pattern_no is '���c�p�^�[���ԍ�';
comment on column gougi_pattern_oya.gougi_name is '���c��';
comment on column gougi_pattern_oya.hyouji_jun is '�\����';
comment on column gougi_pattern_oya.touroku_user_id is '�o�^���[�U�[ID';
comment on column gougi_pattern_oya.touroku_time is '�o�^����';
comment on column gougi_pattern_oya.koushin_user_id is '�X�V���[�U�[ID';
comment on column gougi_pattern_oya.koushin_time is '�X�V����';

comment on table gyoumu_role is '�Ɩ����[��';
comment on column gyoumu_role.gyoumu_role_id is '�Ɩ����[��ID';
comment on column gyoumu_role.gyoumu_role_name is '�Ɩ����[����';
comment on column gyoumu_role.hyouji_jun is '�\����';
comment on column gyoumu_role.touroku_user_id is '�o�^���[�U�[ID';
comment on column gyoumu_role.touroku_time is '�o�^����';
comment on column gyoumu_role.koushin_user_id is '�X�V���[�U�[ID';
comment on column gyoumu_role.koushin_time is '�X�V����';

comment on table gyoumu_role_kinou_seigyo is '�Ɩ����[���@�\����';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_id is '�Ɩ����[��ID';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_kinou_seigyo_cd is '�Ɩ����[���@�\����R�[�h';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_kinou_seigyo_kbn is '�Ɩ����[���@�\����敪';
comment on column gyoumu_role_kinou_seigyo.touroku_user_id is '�o�^���[�U�[ID';
comment on column gyoumu_role_kinou_seigyo.touroku_time is '�o�^����';
comment on column gyoumu_role_kinou_seigyo.koushin_user_id is '�X�V���[�U�[ID';
comment on column gyoumu_role_kinou_seigyo.koushin_time is '�X�V����';

comment on table gyoumu_role_wariate is '�Ɩ����[�����蓖��';
comment on column gyoumu_role_wariate.user_id is '���[�U�[ID';
comment on column gyoumu_role_wariate.gyoumu_role_id is '�Ɩ����[��ID';
comment on column gyoumu_role_wariate.yuukou_kigen_from is '�L�������J�n��';
comment on column gyoumu_role_wariate.yuukou_kigen_to is '�L�������I����';
comment on column gyoumu_role_wariate.shori_bumon_cd is '��������R�[�h';
comment on column gyoumu_role_wariate.touroku_user_id is '�o�^���[�U�[ID';
comment on column gyoumu_role_wariate.touroku_time is '�o�^����';
comment on column gyoumu_role_wariate.koushin_user_id is '�X�V���[�U�[ID';
comment on column gyoumu_role_wariate.koushin_time is '�X�V����';

comment on table heishu_master is '����}�X�^�[';
comment on column heishu_master.heishu_cd is '����R�[�h';
comment on column heishu_master.currency_unit is '�ʉݒP��';
comment on column heishu_master.country_name is '���܂��͒n��';
comment on column heishu_master.conversion_unit is '���Z�P��';
comment on column heishu_master.decimal_position is '����������';
comment on column heishu_master.availability_flg is '�g�p��';
comment on column heishu_master.display_order is '�\����';

comment on table help is '�w���v';
comment on column help.gamen_id is '���ID';
comment on column help.help_rich_text is '�w���v���b�`�e�L�X�g';
comment on column help.touroku_user_id is '�o�^���[�U�[ID';
comment on column help.touroku_time is '�o�^����';
comment on column help.koushin_user_id is '�X�V���[�U�[ID';
comment on column help.koushin_time is '�X�V����';

comment on table hf1_ichiran is '�w�b�_�t�B�[���h�P�ꗗ';
comment on column hf1_ichiran.hf1_cd is 'HF1�R�[�h';
comment on column hf1_ichiran.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column hf1_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf10_ichiran is '�w�b�_�t�B�[���h�P�O�ꗗ';
comment on column hf10_ichiran.hf10_cd is 'HF10�R�[�h';
comment on column hf10_ichiran.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column hf10_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf2_ichiran is '�w�b�_�t�B�[���h�Q�ꗗ';
comment on column hf2_ichiran.hf2_cd is 'HF2�R�[�h';
comment on column hf2_ichiran.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column hf2_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf3_ichiran is '�w�b�_�t�B�[���h�R�ꗗ';
comment on column hf3_ichiran.hf3_cd is 'HF3�R�[�h';
comment on column hf3_ichiran.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column hf3_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf4_ichiran is '�w�b�_�t�B�[���h�S�ꗗ';
comment on column hf4_ichiran.hf4_cd is 'HF4�R�[�h';
comment on column hf4_ichiran.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column hf4_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf5_ichiran is '�w�b�_�t�B�[���h�T�ꗗ';
comment on column hf5_ichiran.hf5_cd is 'HF5�R�[�h';
comment on column hf5_ichiran.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column hf5_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf6_ichiran is '�w�b�_�t�B�[���h�U�ꗗ';
comment on column hf6_ichiran.hf6_cd is 'HF6�R�[�h';
comment on column hf6_ichiran.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column hf6_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf7_ichiran is '�w�b�_�t�B�[���h�V�ꗗ';
comment on column hf7_ichiran.hf7_cd is 'HF7�R�[�h';
comment on column hf7_ichiran.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column hf7_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf8_ichiran is '�w�b�_�t�B�[���h�W�ꗗ';
comment on column hf8_ichiran.hf8_cd is 'HF8�R�[�h';
comment on column hf8_ichiran.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column hf8_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hf9_ichiran is '�w�b�_�t�B�[���h�X�ꗗ';
comment on column hf9_ichiran.hf9_cd is 'HF9�R�[�h';
comment on column hf9_ichiran.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column hf9_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table hizuke_control is '���t�R���g���[��';
comment on column hizuke_control.system_kanri_date is '�V�X�e���Ǘ����t';
comment on column hizuke_control.fb_data_sakuseibi_flg is 'FB�f�[�^�쐬���t���O';
comment on column hizuke_control.kojinseisan_shiharaibi is '�l���Z�x����';
comment on column hizuke_control.kinyuukikan_eigyoubi_flg is '���Z�@�։c�Ɠ��t���O';
comment on column hizuke_control.toujitsu_kbn_flg is '�����敪�t���O';
comment on column hizuke_control.keijoubi_flg is '�v����t���O';

comment on table houjin_card_import is '�@�l�J�[�h�C���|�[�g';
comment on column houjin_card_import.user_id is '���[�U�[ID';
comment on column houjin_card_import.houjin_card_shubetsu is '�@�l�J�[�h���';

comment on table houjin_card_jouhou is '�@�l�J�[�h�g�p�������';
comment on column houjin_card_jouhou.card_jouhou_id is '�J�[�h���ID';
comment on column houjin_card_jouhou.card_shubetsu is '�J�[�h��ʃR�[�h';
comment on column houjin_card_jouhou.torikomi_denpyou_id is '�捞��`�[ID';
comment on column houjin_card_jouhou.busho_cd is '�����R�[�h';
comment on column houjin_card_jouhou.shain_bangou is '�Ј��ԍ�';
comment on column houjin_card_jouhou.shiyousha is '�g�p��';
comment on column houjin_card_jouhou.riyoubi is '���p��';
comment on column houjin_card_jouhou.kingaku is '���z';
comment on column houjin_card_jouhou.card_bangou is '�J�[�h�ԍ�';
comment on column houjin_card_jouhou.kameiten is '�����X';
comment on column houjin_card_jouhou.gyoushu_cd is '�Ǝ�R�[�h';
comment on column houjin_card_jouhou.jyogai_flg is '���O�t���O';
comment on column houjin_card_jouhou.jyogai_riyuu is '���O���R';
comment on column houjin_card_jouhou.touroku_user_id is '�o�^���[�U�[ID';
comment on column houjin_card_jouhou.touroku_time is '�o�^����';
comment on column houjin_card_jouhou.koushin_user_id is '�X�V���[�U�[ID';
comment on column houjin_card_jouhou.koushin_time is '�X�V����';

comment on table ic_card is 'IC�J�[�h';
comment on column ic_card.ic_card_no is 'IC�J�[�h�ԍ�';
comment on column ic_card.token is '�g�[�N��';
comment on column ic_card.user_id is '���[�U�[ID';

comment on table ic_card_rireki is 'IC�J�[�h���p����';
comment on column ic_card_rireki.ic_card_no is 'IC�J�[�h�ԍ�';
comment on column ic_card_rireki.ic_card_sequence_no is 'IC�J�[�h�V�[�P���X�ԍ�';
comment on column ic_card_rireki.ic_card_riyoubi is 'IC�J�[�h���p��';
comment on column ic_card_rireki.tanmatu_cd is '�[�����';
comment on column ic_card_rireki.line_cd_from is '�H���R�[�h�iFROM�j';
comment on column ic_card_rireki.line_name_from is '�H�����iFROM�j';
comment on column ic_card_rireki.eki_cd_from is '�w�R�[�h�iFROM�j';
comment on column ic_card_rireki.eki_name_from is '�w���iFROM�j';
comment on column ic_card_rireki.line_cd_to is '�H���R�[�h�iTO�j';
comment on column ic_card_rireki.line_name_to is '�H�����iTO�j';
comment on column ic_card_rireki.eki_cd_to is '�w�R�[�h�iTO�j';
comment on column ic_card_rireki.eki_name_to is '�w���iTO�j';
comment on column ic_card_rireki.kingaku is '���z';
comment on column ic_card_rireki.user_id is '���[�U�[ID';
comment on column ic_card_rireki.jyogai_flg is '���O�t���O';
comment on column ic_card_rireki.shori_cd is '�������e';
comment on column ic_card_rireki.balance is '�c��';
comment on column ic_card_rireki.all_byte is '�S�o�C�g�z��';

comment on table info_id_saiban is '�C���t�H���[�V����ID�̔�';
comment on column info_id_saiban.touroku_date is '�o�^��';
comment on column info_id_saiban.sequence_val is '�V�[�P���X�l';

comment on table information is '�C���t�H���[�V����';
comment on column information.info_id is '�C���t�H���[�V����ID';
comment on column information.tsuuchi_kikan_from is '�ʒm���ԊJ�n��';
comment on column information.tsuuchi_kikan_to is '�ʒm���ԏI����';
comment on column information.tsuuchi_naiyou is '�ʒm���e';
comment on column information.touroku_user_id is '�o�^���[�U�[ID';
comment on column information.touroku_time is '�o�^����';
comment on column information.koushin_user_id is '�X�V���[�U�[ID';
comment on column information.koushin_time is '�X�V����';

comment on table information_sort is '�C���t�H���[�V��������';
comment on column information_sort.info_id is '�C���t�H���[�V����ID';
comment on column information_sort.hyouji_jun is '�\����';

comment on table jidouhikiotoshi is '��������';
comment on column jidouhikiotoshi.denpyou_id is '�`�[ID';
comment on column jidouhikiotoshi.keijoubi is '�v���';
comment on column jidouhikiotoshi.hikiotoshibi is '������';
comment on column jidouhikiotoshi.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column jidouhikiotoshi.hontai_kingaku_goukei is '�{�̋��z���v';
comment on column jidouhikiotoshi.shouhizeigaku_goukei is '����Ŋz���v';
comment on column jidouhikiotoshi.shiharai_kingaku_goukei is '�x�����z���v';
comment on column jidouhikiotoshi.hf1_cd is 'HF1�R�[�h';
comment on column jidouhikiotoshi.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column jidouhikiotoshi.hf2_cd is 'HF2�R�[�h';
comment on column jidouhikiotoshi.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column jidouhikiotoshi.hf3_cd is 'HF3�R�[�h';
comment on column jidouhikiotoshi.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column jidouhikiotoshi.hf4_cd is 'HF4�R�[�h';
comment on column jidouhikiotoshi.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column jidouhikiotoshi.hf5_cd is 'HF5�R�[�h';
comment on column jidouhikiotoshi.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column jidouhikiotoshi.hf6_cd is 'HF6�R�[�h';
comment on column jidouhikiotoshi.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column jidouhikiotoshi.hf7_cd is 'HF7�R�[�h';
comment on column jidouhikiotoshi.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column jidouhikiotoshi.hf8_cd is 'HF8�R�[�h';
comment on column jidouhikiotoshi.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column jidouhikiotoshi.hf9_cd is 'HF9�R�[�h';
comment on column jidouhikiotoshi.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column jidouhikiotoshi.hf10_cd is 'HF10�R�[�h';
comment on column jidouhikiotoshi.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column jidouhikiotoshi.hosoku is '�⑫';
comment on column jidouhikiotoshi.touroku_user_id is '�o�^���[�U�[ID';
comment on column jidouhikiotoshi.touroku_time is '�o�^����';
comment on column jidouhikiotoshi.koushin_user_id is '�X�V���[�U�[ID';
comment on column jidouhikiotoshi.koushin_time is '�X�V����';
comment on column jidouhikiotoshi.nyuryoku_houshiki is '���͕���';
comment on column jidouhikiotoshi.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table jidouhikiotoshi_meisai is '������������';
comment on column jidouhikiotoshi_meisai.denpyou_id is '�`�[ID';
comment on column jidouhikiotoshi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column jidouhikiotoshi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column jidouhikiotoshi_meisai.torihiki_name is '�����';
comment on column jidouhikiotoshi_meisai.tekiyou is '�E�v';
comment on column jidouhikiotoshi_meisai.zeiritsu is '�ŗ�';
comment on column jidouhikiotoshi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column jidouhikiotoshi_meisai.shiharai_kingaku is '�x�����z';
comment on column jidouhikiotoshi_meisai.hontai_kingaku is '�{�̋��z';
comment on column jidouhikiotoshi_meisai.shouhizeigaku is '����Ŋz';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column jidouhikiotoshi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column jidouhikiotoshi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column jidouhikiotoshi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column jidouhikiotoshi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column jidouhikiotoshi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column jidouhikiotoshi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column jidouhikiotoshi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column jidouhikiotoshi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column jidouhikiotoshi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column jidouhikiotoshi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column jidouhikiotoshi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column jidouhikiotoshi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column jidouhikiotoshi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column jidouhikiotoshi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column jidouhikiotoshi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column jidouhikiotoshi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column jidouhikiotoshi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column jidouhikiotoshi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column jidouhikiotoshi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column jidouhikiotoshi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column jidouhikiotoshi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column jidouhikiotoshi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column jidouhikiotoshi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column jidouhikiotoshi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column jidouhikiotoshi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column jidouhikiotoshi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column jidouhikiotoshi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column jidouhikiotoshi_meisai.project_name is '�v���W�F�N�g��';
comment on column jidouhikiotoshi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column jidouhikiotoshi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column jidouhikiotoshi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column jidouhikiotoshi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column jidouhikiotoshi_meisai.touroku_time is '�o�^����';
comment on column jidouhikiotoshi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column jidouhikiotoshi_meisai.koushin_time is '�X�V����';
comment on column jidouhikiotoshi_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column jidouhikiotoshi_meisai.bunri_kbn is '�����敪';
comment on column jidouhikiotoshi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column jidouhikiotoshi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

comment on table jishou is '����';
comment on column jishou.jishou_id is '����ID';
comment on column jishou.midashi_id is '���o��ID';
comment on column jishou.jishou_name is '���ۖ�';
comment on column jishou.hyouji_jun is '�\����';
comment on column jishou.touroku_user_id is '�o�^���[�U�[ID';
comment on column jishou.touroku_time is '�o�^����';
comment on column jishou.koushin_user_id is '�X�V���[�U�[ID';
comment on column jishou.koushin_time is '�X�V����';

comment on table jishou_dpkbn_kanren is '���ۓ`�[�敪�֘A';
comment on column jishou_dpkbn_kanren.jishou_id is '����ID';
comment on column jishou_dpkbn_kanren.denpyou_kbn is '�`�[�敪';
comment on column jishou_dpkbn_kanren.hyouji_jun is '�\����';
comment on column jishou_dpkbn_kanren.touroku_user_id is '�o�^���[�U�[ID';
comment on column jishou_dpkbn_kanren.touroku_time is '�o�^����';
comment on column jishou_dpkbn_kanren.koushin_user_id is '�X�V���[�U�[ID';
comment on column jishou_dpkbn_kanren.koushin_time is '�X�V����';

comment on table kaigai_koutsuu_shudan_master is '�C�O�p��ʎ�i�}�X�^�[';
comment on column kaigai_koutsuu_shudan_master.sort_jun is '���я�';
comment on column kaigai_koutsuu_shudan_master.koutsuu_shudan is '��ʎ�i';
comment on column kaigai_koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_koutsuu_shudan_master.zei_kubun is '�ŋ敪';
comment on column kaigai_koutsuu_shudan_master.edaban is '�}�ԃR�[�h';

comment on table kaigai_nittou_nado_master is '�C�O�p�������}�X�^�[';
comment on column kaigai_nittou_nado_master.shubetsu1 is '��ʂP';
comment on column kaigai_nittou_nado_master.shubetsu2 is '��ʂQ';
comment on column kaigai_nittou_nado_master.yakushoku_cd is '��E�R�[�h';
comment on column kaigai_nittou_nado_master.tanka is '�P���i�M�݁j';
comment on column kaigai_nittou_nado_master.heishu_cd is '����R�[�h';
comment on column kaigai_nittou_nado_master.currency_unit is '�ʉݒP��';
comment on column kaigai_nittou_nado_master.tanka_gaika is '�P���i�O�݁j';
comment on column kaigai_nittou_nado_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_nittou_nado_master.nittou_shukuhakuhi_flg is '�����E�h����t���O';
comment on column kaigai_nittou_nado_master.zei_kubun is '�ŋ敪';
comment on column kaigai_nittou_nado_master.edaban is '�}�ԃR�[�h';

comment on table kaigai_ryohi_karibarai is '�C�O�����';
comment on column kaigai_ryohi_karibarai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohi_karibarai.karibarai_on is '�����\���t���O';
comment on column kaigai_ryohi_karibarai.dairiflg is '�㗝�t���O';
comment on column kaigai_ryohi_karibarai.user_id is '���[�U�[ID';
comment on column kaigai_ryohi_karibarai.shain_no is '�Ј��ԍ�';
comment on column kaigai_ryohi_karibarai.user_sei is '���[�U�[��';
comment on column kaigai_ryohi_karibarai.user_mei is '���[�U�[��';
comment on column kaigai_ryohi_karibarai.houmonsaki is '�K���';
comment on column kaigai_ryohi_karibarai.mokuteki is '�ړI';
comment on column kaigai_ryohi_karibarai.seisankikan_from is '���Z���ԊJ�n��';
comment on column kaigai_ryohi_karibarai.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_to is '���Z���ԏI����';
comment on column kaigai_ryohi_karibarai.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column kaigai_ryohi_karibarai.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column kaigai_ryohi_karibarai.shiharaibi is '�x����';
comment on column kaigai_ryohi_karibarai.shiharaikiboubi is '�x����]��';
comment on column kaigai_ryohi_karibarai.shiharaihouhou is '�x�����@';
comment on column kaigai_ryohi_karibarai.tekiyou is '�E�v';
comment on column kaigai_ryohi_karibarai.kingaku is '���z';
comment on column kaigai_ryohi_karibarai.karibarai_kingaku is '�������z';
comment on column kaigai_ryohi_karibarai.sashihiki_num is '������';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka is '�����P��';
comment on column kaigai_ryohi_karibarai.sashihiki_num_kaigai is '�����񐔁i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai is '�����P���i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_heishu_cd_kaigai is '��������R�[�h�i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_rate_kaigai is '�������[�g�i�C�O�j';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai_gaika is '�����P���i�C�O�j�O��';
comment on column kaigai_ryohi_karibarai.hf1_cd is 'HF1�R�[�h';
comment on column kaigai_ryohi_karibarai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column kaigai_ryohi_karibarai.hf2_cd is 'HF2�R�[�h';
comment on column kaigai_ryohi_karibarai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column kaigai_ryohi_karibarai.hf3_cd is 'HF3�R�[�h';
comment on column kaigai_ryohi_karibarai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column kaigai_ryohi_karibarai.hf4_cd is 'HF4�R�[�h';
comment on column kaigai_ryohi_karibarai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column kaigai_ryohi_karibarai.hf5_cd is 'HF5�R�[�h';
comment on column kaigai_ryohi_karibarai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column kaigai_ryohi_karibarai.hf6_cd is 'HF6�R�[�h';
comment on column kaigai_ryohi_karibarai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column kaigai_ryohi_karibarai.hf7_cd is 'HF7�R�[�h';
comment on column kaigai_ryohi_karibarai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column kaigai_ryohi_karibarai.hf8_cd is 'HF8�R�[�h';
comment on column kaigai_ryohi_karibarai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column kaigai_ryohi_karibarai.hf9_cd is 'HF9�R�[�h';
comment on column kaigai_ryohi_karibarai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column kaigai_ryohi_karibarai.hf10_cd is 'HF10�R�[�h';
comment on column kaigai_ryohi_karibarai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column kaigai_ryohi_karibarai.hosoku is '�⑫';
comment on column kaigai_ryohi_karibarai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohi_karibarai.torihiki_name is '�����';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohi_karibarai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohi_karibarai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohi_karibarai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohi_karibarai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohi_karibarai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohi_karibarai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohi_karibarai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohi_karibarai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohi_karibarai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohi_karibarai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohi_karibarai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohi_karibarai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohi_karibarai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohi_karibarai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohi_karibarai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohi_karibarai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohi_karibarai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohi_karibarai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohi_karibarai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohi_karibarai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohi_karibarai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohi_karibarai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohi_karibarai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohi_karibarai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohi_karibarai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohi_karibarai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohi_karibarai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohi_karibarai.seisan_kanryoubi is '���Z������';
comment on column kaigai_ryohi_karibarai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohi_karibarai.touroku_time is '�o�^����';
comment on column kaigai_ryohi_karibarai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohi_karibarai.koushin_time is '�X�V����';

comment on table kaigai_ryohi_karibarai_keihi_meisai is '�C�O������o���';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiyoubi is '�g�p��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihiki_name is '�����';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou is '�E�v';
comment on column kaigai_ryohi_karibarai_keihi_meisai.zeiritsu is '�ŗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kazei_flg is '�ېŃt���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.rate is '���[�g';
comment on column kaigai_ryohi_karibarai_keihi_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohi_karibarai_keihi_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_time is '�X�V����';

comment on table kaigai_ryohi_karibarai_meisai is '�C�O���������';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohi_karibarai_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohi_karibarai_meisai.kikan_from is '���ԊJ�n��';
comment on column kaigai_ryohi_karibarai_meisai.kikan_to is '���ԏI����';
comment on column kaigai_ryohi_karibarai_meisai.kyuujitsu_nissuu is '�x������';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu_cd is '��ʃR�[�h';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu1 is '��ʂP';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu2 is '��ʂQ';
comment on column kaigai_ryohi_karibarai_meisai.haya_flg is '���t���O';
comment on column kaigai_ryohi_karibarai_meisai.yasu_flg is '���t���O';
comment on column kaigai_ryohi_karibarai_meisai.raku_flg is '�y�t���O';
comment on column kaigai_ryohi_karibarai_meisai.koutsuu_shudan is '��ʎ�i';
comment on column kaigai_ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_ryohi_karibarai_meisai.naiyou is '���e�i����Z�j';
comment on column kaigai_ryohi_karibarai_meisai.bikou is '���l�i����Z�j';
comment on column kaigai_ryohi_karibarai_meisai.oufuku_flg is '�����t���O';
comment on column kaigai_ryohi_karibarai_meisai.jidounyuuryoku_flg is '�������̓t���O';
comment on column kaigai_ryohi_karibarai_meisai.nissuu is '����';
comment on column kaigai_ryohi_karibarai_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohi_karibarai_meisai.rate is '���[�g';
comment on column kaigai_ryohi_karibarai_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohi_karibarai_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohi_karibarai_meisai.tanka is '�P��';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column kaigai_ryohi_karibarai_meisai.suuryou is '����';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_kigou is '���ʋL��';
comment on column kaigai_ryohi_karibarai_meisai.meisai_kingaku is '���׋��z';
comment on column kaigai_ryohi_karibarai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohi_karibarai_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohi_karibarai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohi_karibarai_meisai.koushin_time is '�X�V����';

comment on table kaigai_ryohiseisan is '�C�O����Z';
comment on column kaigai_ryohiseisan.denpyou_id is '�`�[ID';
comment on column kaigai_ryohiseisan.karibarai_denpyou_id is '�����`�[ID';
comment on column kaigai_ryohiseisan.karibarai_on is '�����\���t���O';
comment on column kaigai_ryohiseisan.karibarai_mishiyou_flg is '���������g�p�t���O';
comment on column kaigai_ryohiseisan.shucchou_chuushi_flg is '�o�����~�t���O';
comment on column kaigai_ryohiseisan.dairiflg is '�㗝�t���O';
comment on column kaigai_ryohiseisan.user_id is '���[�U�[ID';
comment on column kaigai_ryohiseisan.shain_no is '�Ј��ԍ�';
comment on column kaigai_ryohiseisan.user_sei is '���[�U�[��';
comment on column kaigai_ryohiseisan.user_mei is '���[�U�[��';
comment on column kaigai_ryohiseisan.houmonsaki is '�K���';
comment on column kaigai_ryohiseisan.mokuteki is '�ړI';
comment on column kaigai_ryohiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column kaigai_ryohiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column kaigai_ryohiseisan.seisankikan_to is '���Z���ԏI����';
comment on column kaigai_ryohiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column kaigai_ryohiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column kaigai_ryohiseisan.keijoubi is '�v���';
comment on column kaigai_ryohiseisan.shiharaibi is '�x����';
comment on column kaigai_ryohiseisan.shiharaikiboubi is '�x����]��';
comment on column kaigai_ryohiseisan.shiharaihouhou is '�x�����@';
comment on column kaigai_ryohiseisan.tekiyou is '�E�v';
comment on column kaigai_ryohiseisan.kaigai_tekiyou is '�E�v�i�C�O�j';
comment on column kaigai_ryohiseisan.zeiritsu is '�ŗ�';
comment on column kaigai_ryohiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohiseisan.goukei_kingaku is '���v���z';
comment on column kaigai_ryohiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column kaigai_ryohiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column kaigai_ryohiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column kaigai_ryohiseisan.sashihiki_num is '������';
comment on column kaigai_ryohiseisan.sashihiki_tanka is '�����P��';
comment on column kaigai_ryohiseisan.sashihiki_num_kaigai is '�����񐔁i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai is '�����P���i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_heishu_cd_kaigai is '��������R�[�h�i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_rate_kaigai is '�������[�g�i�C�O�j';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai_gaika is '�����P���i�C�O�j�O��';
comment on column kaigai_ryohiseisan.hf1_cd is 'HF1�R�[�h';
comment on column kaigai_ryohiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column kaigai_ryohiseisan.hf2_cd is 'HF2�R�[�h';
comment on column kaigai_ryohiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column kaigai_ryohiseisan.hf3_cd is 'HF3�R�[�h';
comment on column kaigai_ryohiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column kaigai_ryohiseisan.hf4_cd is 'HF4�R�[�h';
comment on column kaigai_ryohiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column kaigai_ryohiseisan.hf5_cd is 'HF5�R�[�h';
comment on column kaigai_ryohiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column kaigai_ryohiseisan.hf6_cd is 'HF6�R�[�h';
comment on column kaigai_ryohiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column kaigai_ryohiseisan.hf7_cd is 'HF7�R�[�h';
comment on column kaigai_ryohiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column kaigai_ryohiseisan.hf8_cd is 'HF8�R�[�h';
comment on column kaigai_ryohiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column kaigai_ryohiseisan.hf9_cd is 'HF9�R�[�h';
comment on column kaigai_ryohiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column kaigai_ryohiseisan.hf10_cd is 'HF10�R�[�h';
comment on column kaigai_ryohiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column kaigai_ryohiseisan.hosoku is '�⑫';
comment on column kaigai_ryohiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohiseisan.torihiki_name is '�����';
comment on column kaigai_ryohiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohiseisan.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan.ryohi_kazei_flg is '����ېŃt���O';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohiseisan.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohiseisan.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohiseisan.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohiseisan.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohiseisan.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohiseisan.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohiseisan.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohiseisan.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohiseisan.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_shiwake_edano is '�C�O�d��}�ԍ�';
comment on column kaigai_ryohiseisan.kaigai_torihiki_name is '�C�O�����';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_cd is '�C�O�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_name is '�C�O�ؕ����S���喼';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_cd is '�C�O�����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_name_ryakushiki is '�C�O����於�i�����j';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_cd is '�C�O�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_name is '�C�O�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_cd is '�C�O�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_name is '�C�O�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kaigai_kari_kazei_kbn is '�C�O�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan.kaigai_kazei_flg is '�C�O�ېŃt���O';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_cd is '�C�O�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_name is '�C�O�ݕ����S���喼';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_cd is '�C�O�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_name is '�C�O�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_cd is '�C�O�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_name is '�C�O�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan.kaigai_kashi_kazei_kbn is '�C�O�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan.kaigai_uf1_cd is '�C�OUF1�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf1_name_ryakushiki is '�C�OUF1���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf2_cd is '�C�OUF2�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf2_name_ryakushiki is '�C�OUF2���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf3_cd is '�C�OUF3�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf3_name_ryakushiki is '�C�OUF3���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf4_cd is '�C�OUF4�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf4_name_ryakushiki is '�C�OUF4���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf5_cd is '�C�OUF5�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf5_name_ryakushiki is '�C�OUF5���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf6_cd is '�C�OUF6�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf6_name_ryakushiki is '�C�OUF6���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf7_cd is '�C�OUF7�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf7_name_ryakushiki is '�C�OUF7���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf8_cd is '�C�OUF8�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf8_name_ryakushiki is '�C�OUF8���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf9_cd is '�C�OUF9�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf9_name_ryakushiki is '�C�OUF9���i�����j';
comment on column kaigai_ryohiseisan.kaigai_uf10_cd is '�C�OUF10�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_uf10_name_ryakushiki is '�C�OUF10���i�����j';
comment on column kaigai_ryohiseisan.kaigai_project_cd is '�C�O�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_project_name is '�C�O�v���W�F�N�g��';
comment on column kaigai_ryohiseisan.kaigai_segment_cd is '�C�O�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan.kaigai_segment_name_ryakushiki is '�C�O�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan.kaigai_tekiyou_cd is '�C�O�E�v�R�[�h';
comment on column kaigai_ryohiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohiseisan.touroku_time is '�o�^����';
comment on column kaigai_ryohiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohiseisan.koushin_time is '�X�V����';
comment on column kaigai_ryohiseisan.bunri_kbn is '�����敪';
comment on column kaigai_ryohiseisan.kari_shiire_kbn is '�ؕ��d���敪';
comment on column kaigai_ryohiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
comment on column kaigai_ryohiseisan.kaigai_bunri_kbn is '�C�O�����敪';
comment on column kaigai_ryohiseisan.kaigai_kari_shiire_kbn is '�C�O�ؕ��d���敪';
comment on column kaigai_ryohiseisan.kaigai_kashi_shiire_kbn is '�C�O�ݕ��d���敪';
comment on column kaigai_ryohiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table kaigai_ryohiseisan_keihi_meisai is '�C�O����Z�o���';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.shiyoubi is '�g�p��';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column kaigai_ryohiseisan_keihi_meisai.torihiki_name is '�����';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou is '�E�v';
comment on column kaigai_ryohiseisan_keihi_meisai.zeiritsu is '�ŗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kazei_flg is '�ېŃt���O';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column kaigai_ryohiseisan_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column kaigai_ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column kaigai_ryohiseisan_keihi_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.rate is '���[�g';
comment on column kaigai_ryohiseisan_keihi_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohiseisan_keihi_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column kaigai_ryohiseisan_keihi_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_time is '�X�V����';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharaisaki_name is '�x���於';
comment on column kaigai_ryohiseisan_keihi_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column kaigai_ryohiseisan_keihi_meisai.bunri_kbn is '�����敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

comment on table kaigai_ryohiseisan_meisai is '�C�O����Z����';
comment on column kaigai_ryohiseisan_meisai.denpyou_id is '�`�[ID';
comment on column kaigai_ryohiseisan_meisai.kaigai_flg is '�C�O�t���O';
comment on column kaigai_ryohiseisan_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kaigai_ryohiseisan_meisai.kikan_from is '���ԊJ�n��';
comment on column kaigai_ryohiseisan_meisai.kikan_to is '���ԏI����';
comment on column kaigai_ryohiseisan_meisai.kyuujitsu_nissuu is '�x������';
comment on column kaigai_ryohiseisan_meisai.shubetsu_cd is '��ʃR�[�h';
comment on column kaigai_ryohiseisan_meisai.shubetsu1 is '��ʂP';
comment on column kaigai_ryohiseisan_meisai.shubetsu2 is '��ʂQ';
comment on column kaigai_ryohiseisan_meisai.haya_flg is '���t���O';
comment on column kaigai_ryohiseisan_meisai.yasu_flg is '���t���O';
comment on column kaigai_ryohiseisan_meisai.raku_flg is '�y�t���O';
comment on column kaigai_ryohiseisan_meisai.koutsuu_shudan is '��ʎ�i';
comment on column kaigai_ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column kaigai_ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '�̎����E���������t���O';
comment on column kaigai_ryohiseisan_meisai.naiyou is '���e�i����Z�j';
comment on column kaigai_ryohiseisan_meisai.bikou is '���l�i����Z�j';
comment on column kaigai_ryohiseisan_meisai.oufuku_flg is '�����t���O';
comment on column kaigai_ryohiseisan_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column kaigai_ryohiseisan_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column kaigai_ryohiseisan_meisai.jidounyuuryoku_flg is '�������̓t���O';
comment on column kaigai_ryohiseisan_meisai.nissuu is '����';
comment on column kaigai_ryohiseisan_meisai.heishu_cd is '����R�[�h';
comment on column kaigai_ryohiseisan_meisai.rate is '���[�g';
comment on column kaigai_ryohiseisan_meisai.gaika is '�O�݋��z';
comment on column kaigai_ryohiseisan_meisai.currency_unit is '�ʉݒP��';
comment on column kaigai_ryohiseisan_meisai.tanka is '�P��';
comment on column kaigai_ryohiseisan_meisai.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column kaigai_ryohiseisan_meisai.suuryou is '����';
comment on column kaigai_ryohiseisan_meisai.suuryou_kigou is '���ʋL��';
comment on column kaigai_ryohiseisan_meisai.meisai_kingaku is '���׋��z';
comment on column kaigai_ryohiseisan_meisai.zei_kubun is '�ŋ敪';
comment on column kaigai_ryohiseisan_meisai.kazei_flg is '�ېŃt���O';
comment on column kaigai_ryohiseisan_meisai.ic_card_no is 'IC�J�[�h�ԍ�';
comment on column kaigai_ryohiseisan_meisai.ic_card_sequence_no is 'IC�J�[�h�V�[�P���X�ԍ�';
comment on column kaigai_ryohiseisan_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column kaigai_ryohiseisan_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kaigai_ryohiseisan_meisai.touroku_time is '�o�^����';
comment on column kaigai_ryohiseisan_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kaigai_ryohiseisan_meisai.koushin_time is '�X�V����';
comment on column kaigai_ryohiseisan_meisai.shiharaisaki_name is '�x���於';
comment on column kaigai_ryohiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column kaigai_ryohiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
comment on column kaigai_ryohiseisan_meisai.shouhizeigaku is '����Ŋz';
comment on column kaigai_ryohiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';

comment on table kaisha_info is '��Џ��';
comment on column kaisha_info.kessanki_bangou is '���Z���ԍ�';
comment on column kaisha_info.hf1_shiyou_flg is 'HF1�g�p�t���O';
comment on column kaisha_info.hf1_hissu_flg is 'HF1�K�{�t���O';
comment on column kaisha_info.hf1_name is 'HF1��';
comment on column kaisha_info.hf2_shiyou_flg is 'HF2�g�p�t���O';
comment on column kaisha_info.hf2_hissu_flg is 'HF2�K�{�t���O';
comment on column kaisha_info.hf2_name is 'HF2��';
comment on column kaisha_info.hf3_shiyou_flg is 'HF3�g�p�t���O';
comment on column kaisha_info.hf3_hissu_flg is 'HF3�K�{�t���O';
comment on column kaisha_info.hf3_name is 'HF3��';
comment on column kaisha_info.hf4_shiyou_flg is 'HF4�g�p�t���O';
comment on column kaisha_info.hf4_hissu_flg is 'HF4�K�{�t���O';
comment on column kaisha_info.hf4_name is 'HF4��';
comment on column kaisha_info.hf5_shiyou_flg is 'HF5�g�p�t���O';
comment on column kaisha_info.hf5_hissu_flg is 'HF5�K�{�t���O';
comment on column kaisha_info.hf5_name is 'HF5��';
comment on column kaisha_info.hf6_shiyou_flg is 'HF6�g�p�t���O';
comment on column kaisha_info.hf6_hissu_flg is 'HF6�K�{�t���O';
comment on column kaisha_info.hf6_name is 'HF6��';
comment on column kaisha_info.hf7_shiyou_flg is 'HF7�g�p�t���O';
comment on column kaisha_info.hf7_hissu_flg is 'HF7�K�{�t���O';
comment on column kaisha_info.hf7_name is 'HF7��';
comment on column kaisha_info.hf8_shiyou_flg is 'HF8�g�p�t���O';
comment on column kaisha_info.hf8_hissu_flg is 'HF8�K�{�t���O';
comment on column kaisha_info.hf8_name is 'HF8��';
comment on column kaisha_info.hf9_shiyou_flg is 'HF9�g�p�t���O';
comment on column kaisha_info.hf9_hissu_flg is 'HF9�K�{�t���O';
comment on column kaisha_info.hf9_name is 'HF9��';
comment on column kaisha_info.hf10_shiyou_flg is 'HF10�g�p�t���O';
comment on column kaisha_info.hf10_hissu_flg is 'HF10�K�{�t���O';
comment on column kaisha_info.hf10_name is 'HF10��';
comment on column kaisha_info.uf1_shiyou_flg is 'UF1�g�p�t���O';
comment on column kaisha_info.uf1_name is 'UF1��';
comment on column kaisha_info.uf2_shiyou_flg is 'UF2�g�p�t���O';
comment on column kaisha_info.uf2_name is 'UF2��';
comment on column kaisha_info.uf3_shiyou_flg is 'UF3�g�p�t���O';
comment on column kaisha_info.uf3_name is 'UF3��';
comment on column kaisha_info.uf4_shiyou_flg is 'UF4�g�p�t���O';
comment on column kaisha_info.uf4_name is 'UF4��';
comment on column kaisha_info.uf5_shiyou_flg is 'UF5�g�p�t���O';
comment on column kaisha_info.uf5_name is 'UF5��';
comment on column kaisha_info.uf6_shiyou_flg is 'UF6�g�p�t���O';
comment on column kaisha_info.uf6_name is 'UF6��';
comment on column kaisha_info.uf7_shiyou_flg is 'UF7�g�p�t���O';
comment on column kaisha_info.uf7_name is 'UF7��';
comment on column kaisha_info.uf8_shiyou_flg is 'UF8�g�p�t���O';
comment on column kaisha_info.uf8_name is 'UF8��';
comment on column kaisha_info.uf9_shiyou_flg is 'UF9�g�p�t���O';
comment on column kaisha_info.uf9_name is 'UF9��';
comment on column kaisha_info.uf10_shiyou_flg is 'UF10�g�p�t���O';
comment on column kaisha_info.uf10_name is 'UF10��';
comment on column kaisha_info.uf_kotei1_shiyou_flg is 'UF1�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei1_name is 'UF1��(�Œ�l)';
comment on column kaisha_info.uf_kotei2_shiyou_flg is 'UF2�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei2_name is 'UF2��(�Œ�l)';
comment on column kaisha_info.uf_kotei3_shiyou_flg is 'UF3�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei3_name is 'UF3��(�Œ�l)';
comment on column kaisha_info.uf_kotei4_shiyou_flg is 'UF4�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei4_name is 'UF4��(�Œ�l)';
comment on column kaisha_info.uf_kotei5_shiyou_flg is 'UF5�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei5_name is 'UF5��(�Œ�l)';
comment on column kaisha_info.uf_kotei6_shiyou_flg is 'UF6�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei6_name is 'UF6��(�Œ�l)';
comment on column kaisha_info.uf_kotei7_shiyou_flg is 'UF7�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei7_name is 'UF7��(�Œ�l)';
comment on column kaisha_info.uf_kotei8_shiyou_flg is 'UF8�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei8_name is 'UF8��(�Œ�l)';
comment on column kaisha_info.uf_kotei9_shiyou_flg is 'UF9�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei9_name is 'UF9��(�Œ�l)';
comment on column kaisha_info.uf_kotei10_shiyou_flg is 'UF10�g�p�t���O(�Œ�l)';
comment on column kaisha_info.uf_kotei10_name is 'UF10��(�Œ�l)';
comment on column kaisha_info.pjcd_shiyou_flg is '�v���W�F�N�g�R�[�h�g�p�t���O';
comment on column kaisha_info.sgcd_shiyou_flg is '�Z�O�����g�R�[�h�g�p�t���O';
comment on column kaisha_info.saimu_shiyou_flg is '���g�p�t���O';
comment on column kaisha_info.kamoku_cd_type is '�ȖڃR�[�h�^�C�v';
comment on column kaisha_info.kamoku_edaban_cd_type is '�Ȗڎ}�ԃR�[�h�^�C�v';
comment on column kaisha_info.futan_bumon_cd_type is '���S����R�[�h�^�C�v';
comment on column kaisha_info.torihikisaki_cd_type is '�����R�[�h�^�C�v';
comment on column kaisha_info.segment_cd_type is '�Z�O�����g�R�[�h�^�C�v';
comment on column kaisha_info.houjin_bangou is '�@�l�ԍ�';
comment on column kaisha_info.keigen_umu_flg is '�y���L���t���O';

comment on table kamoku_edaban_zandaka is '�Ȗڎ}�Ԏc��';
comment on column kamoku_edaban_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column kamoku_edaban_zandaka.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column kamoku_edaban_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column kamoku_edaban_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column kamoku_edaban_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column kamoku_edaban_zandaka.edaban_name is '�}�Ԗ�';
comment on column kamoku_edaban_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column kamoku_edaban_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column kamoku_edaban_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column kamoku_edaban_zandaka.kishu_zandaka is '����c��';

comment on table kamoku_master is '�Ȗڃ}�X�^�[';
comment on column kamoku_master.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column kamoku_master.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column kamoku_master.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column kamoku_master.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column kamoku_master.kessanki_bangou is '���Z���ԍ�';
comment on column kamoku_master.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column kamoku_master.taishaku_zokusei is '�ݎؑ���';
comment on column kamoku_master.kamoku_group_kbn is '�ȖڃO���[�v�敪';
comment on column kamoku_master.kamoku_group_bangou is '�ȖڃO���[�v�ԍ�';
comment on column kamoku_master.shori_group is '�����O���[�v';
comment on column kamoku_master.taikakingaku_nyuuryoku_flg is '�Ή����z���̓t���O';
comment on column kamoku_master.kazei_kbn is '�ېŋ敪';
comment on column kamoku_master.bunri_kbn is '�����敪';
comment on column kamoku_master.shiire_kbn is '�d���敪';
comment on column kamoku_master.gyousha_kbn is '�Ǝ�敪';
comment on column kamoku_master.zeiritsu_kbn is '�ŗ��敪';
comment on column kamoku_master.tokusha_hyouji_kbn is '����\���敪';
comment on column kamoku_master.edaban_minyuuryoku_check is '�}�Ԗ����̓`�F�b�N';
comment on column kamoku_master.torihikisaki_minyuuryoku_check is '����斢���̓`�F�b�N';
comment on column kamoku_master.bumon_minyuuryoku_check is '���喢���̓`�F�b�N';
comment on column kamoku_master.bumon_edaban_flg is '����Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.segment_minyuuryoku_check is '�Z�O�����g�����̓`�F�b�N';
comment on column kamoku_master.project_minyuuryoku_check is '�v���W�F�N�g�����̓`�F�b�N';
comment on column kamoku_master.uf1_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N';
comment on column kamoku_master.uf2_minyuuryoku_check is '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N';
comment on column kamoku_master.uf3_minyuuryoku_check is '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N';
comment on column kamoku_master.uf4_minyuuryoku_check is '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N';
comment on column kamoku_master.uf5_minyuuryoku_check is '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N';
comment on column kamoku_master.uf6_minyuuryoku_check is '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N';
comment on column kamoku_master.uf7_minyuuryoku_check is '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N';
comment on column kamoku_master.uf8_minyuuryoku_check is '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N';
comment on column kamoku_master.uf9_minyuuryoku_check is '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N';
comment on column kamoku_master.uf10_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N';
comment on column kamoku_master.uf_kotei1_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei2_minyuuryoku_check is '���j�o�[�T���t�B�[���h�Q�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei3_minyuuryoku_check is '���j�o�[�T���t�B�[���h�R�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei4_minyuuryoku_check is '���j�o�[�T���t�B�[���h�S�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei5_minyuuryoku_check is '���j�o�[�T���t�B�[���h�T�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei6_minyuuryoku_check is '���j�o�[�T���t�B�[���h�U�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei7_minyuuryoku_check is '���j�o�[�T���t�B�[���h�V�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei8_minyuuryoku_check is '���j�o�[�T���t�B�[���h�W�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei9_minyuuryoku_check is '���j�o�[�T���t�B�[���h�X�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.uf_kotei10_minyuuryoku_check is '���j�o�[�T���t�B�[���h�P�O�����̓`�F�b�N(�Œ�l)';
comment on column kamoku_master.kouji_minyuuryoku_check is '�H�������̓`�F�b�N';
comment on column kamoku_master.kousha_minyuuryoku_check is '�H�햢���̓`�F�b�N';
comment on column kamoku_master.tekiyou_cd_minyuuryoku_check is '�E�v�R�[�h�����̓`�F�b�N';
comment on column kamoku_master.bumon_torhikisaki_kamoku_flg is '��������Ȗڎg�p�t���O';
comment on column kamoku_master.bumon_torihikisaki_edaban_shiyou_flg is '��������Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.torihikisaki_kamoku_edaban_flg is '�����Ȗڎ}�Ԏg�p�t���O';
comment on column kamoku_master.segment_torihikisaki_kamoku_flg is '�Z�O�����g�����Ȗڎg�p�t���O';
comment on column kamoku_master.karikanjou_keshikomi_no_flg is '���������No�g�p�t���O';
comment on column kamoku_master.gaika_flg is '�O�ݎg�p�t���O';

comment on table kani_todoke is '�͏o�W�F�l���[�^';
comment on column kani_todoke.denpyou_id is '�`�[ID';
comment on column kani_todoke.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke.version is '�o�[�W����';
comment on column kani_todoke.item_name is '���ږ�';
comment on column kani_todoke.value1 is '�l�P';
comment on column kani_todoke.value2 is '�l�Q';
comment on column kani_todoke.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke.touroku_time is '�o�^����';
comment on column kani_todoke.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke.koushin_time is '�X�V����';

comment on table kani_todoke_checkbox is '�͏o�W�F�l���[�^���ڃ`�F�b�N�{�b�N�X';
comment on column kani_todoke_checkbox.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_checkbox.version is '�o�[�W����';
comment on column kani_todoke_checkbox.area_kbn is '�G���A�敪';
comment on column kani_todoke_checkbox.item_name is '���ږ�';
comment on column kani_todoke_checkbox.label_name is '���x����';
comment on column kani_todoke_checkbox.hissu_flg is '�K�{�t���O';
comment on column kani_todoke_checkbox.checkbox_label_name is '�`�F�b�N�{�b�N�X���x����';
comment on column kani_todoke_checkbox.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_checkbox.touroku_time is '�o�^����';
comment on column kani_todoke_checkbox.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_checkbox.koushin_time is '�X�V����';

comment on table kani_todoke_ichiran is '�͏o�W�F�l���[�^�ꗗ';
comment on column kani_todoke_ichiran.denpyou_id is '�`�[ID';
comment on column kani_todoke_ichiran.shinsei01 is '�\�����e01';
comment on column kani_todoke_ichiran.shinsei02 is '�\�����e02';
comment on column kani_todoke_ichiran.shinsei03 is '�\�����e03';
comment on column kani_todoke_ichiran.shinsei04 is '�\�����e04';
comment on column kani_todoke_ichiran.shinsei05 is '�\�����e05';
comment on column kani_todoke_ichiran.shinsei06 is '�\�����e06';
comment on column kani_todoke_ichiran.shinsei07 is '�\�����e07';
comment on column kani_todoke_ichiran.shinsei08 is '�\�����e08';
comment on column kani_todoke_ichiran.shinsei09 is '�\�����e09';
comment on column kani_todoke_ichiran.shinsei10 is '�\�����e10';
comment on column kani_todoke_ichiran.shinsei11 is '�\�����e11';
comment on column kani_todoke_ichiran.shinsei12 is '�\�����e12';
comment on column kani_todoke_ichiran.shinsei13 is '�\�����e13';
comment on column kani_todoke_ichiran.shinsei14 is '�\�����e14';
comment on column kani_todoke_ichiran.shinsei15 is '�\�����e15';
comment on column kani_todoke_ichiran.shinsei16 is '�\�����e16';
comment on column kani_todoke_ichiran.shinsei17 is '�\�����e17';
comment on column kani_todoke_ichiran.shinsei18 is '�\�����e18';
comment on column kani_todoke_ichiran.shinsei19 is '�\�����e19';
comment on column kani_todoke_ichiran.shinsei20 is '�\�����e20';
comment on column kani_todoke_ichiran.shinsei21 is '�\�����e21';
comment on column kani_todoke_ichiran.shinsei22 is '�\�����e22';
comment on column kani_todoke_ichiran.shinsei23 is '�\�����e23';
comment on column kani_todoke_ichiran.shinsei24 is '�\�����e24';
comment on column kani_todoke_ichiran.shinsei25 is '�\�����e25';
comment on column kani_todoke_ichiran.shinsei26 is '�\�����e26';
comment on column kani_todoke_ichiran.shinsei27 is '�\�����e27';
comment on column kani_todoke_ichiran.shinsei28 is '�\�����e28';
comment on column kani_todoke_ichiran.shinsei29 is '�\�����e29';
comment on column kani_todoke_ichiran.shinsei30 is '�\�����e30';

comment on table kani_todoke_koumoku is '�͏o�W�F�l���[�^���ڒ�`';
comment on column kani_todoke_koumoku.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_koumoku.version is '�o�[�W����';
comment on column kani_todoke_koumoku.area_kbn is '�G���A�敪';
comment on column kani_todoke_koumoku.item_name is '���ږ�';
comment on column kani_todoke_koumoku.hyouji_jun is '�\����';
comment on column kani_todoke_koumoku.buhin_type is '���i�^�C�v';
comment on column kani_todoke_koumoku.default_value1 is '�f�t�H���g�l�P';
comment on column kani_todoke_koumoku.default_value2 is '�f�t�H���g�l�Q';
comment on column kani_todoke_koumoku.yosan_shikkou_koumoku_id is '�\�Z���s����ID';
comment on column kani_todoke_koumoku.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_koumoku.touroku_time is '�o�^����';
comment on column kani_todoke_koumoku.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_koumoku.koushin_time is '�X�V����';

comment on table kani_todoke_list_ko is '�͏o�W�F�l���[�^���ڃ��X�g�q';
comment on column kani_todoke_list_ko.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_list_ko.version is '�o�[�W����';
comment on column kani_todoke_list_ko.area_kbn is '�G���A�敪';
comment on column kani_todoke_list_ko.item_name is '���ږ�';
comment on column kani_todoke_list_ko.hyouji_jun is '�\����';
comment on column kani_todoke_list_ko.text is '�e�L�X�g';
comment on column kani_todoke_list_ko.value is '�l';
comment on column kani_todoke_list_ko.select_item is '�I������';
comment on column kani_todoke_list_ko.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_list_ko.touroku_time is '�o�^����';
comment on column kani_todoke_list_ko.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_list_ko.koushin_time is '�X�V����';

comment on table kani_todoke_list_oya is '�͏o�W�F�l���[�^���ڃ��X�g�e';
comment on column kani_todoke_list_oya.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_list_oya.version is '�o�[�W����';
comment on column kani_todoke_list_oya.area_kbn is '�G���A�敪';
comment on column kani_todoke_list_oya.item_name is '���ږ�';
comment on column kani_todoke_list_oya.label_name is '���x����';
comment on column kani_todoke_list_oya.hissu_flg is '�K�{�t���O';
comment on column kani_todoke_list_oya.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_list_oya.touroku_time is '�o�^����';
comment on column kani_todoke_list_oya.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_list_oya.koushin_time is '�X�V����';

comment on table kani_todoke_master is '�͏o�W�F�l���[�^���ڃ}�X�^�[';
comment on column kani_todoke_master.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_master.version is '�o�[�W����';
comment on column kani_todoke_master.area_kbn is '�G���A�敪';
comment on column kani_todoke_master.item_name is '���ږ�';
comment on column kani_todoke_master.label_name is '���x����';
comment on column kani_todoke_master.hissu_flg is '�K�{�t���O';
comment on column kani_todoke_master.master_kbn is '�}�X�^�[�敪';
comment on column kani_todoke_master.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_master.touroku_time is '�o�^����';
comment on column kani_todoke_master.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_master.koushin_time is '�X�V����';

comment on table kani_todoke_meisai is '�͏o�W�F�l���[�^����';
comment on column kani_todoke_meisai.denpyou_id is '�`�[ID';
comment on column kani_todoke_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column kani_todoke_meisai.item_name is '���ږ�';
comment on column kani_todoke_meisai.value1 is '�l�P';
comment on column kani_todoke_meisai.value2 is '�l�Q';
comment on column kani_todoke_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_meisai.touroku_time is '�o�^����';
comment on column kani_todoke_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_meisai.koushin_time is '�X�V����';

comment on table kani_todoke_meisai_ichiran is '�͏o�W�F�l���[�^���׈ꗗ';
comment on column kani_todoke_meisai_ichiran.denpyou_id is '�`�[ID';
comment on column kani_todoke_meisai_ichiran.denpyou_edano is '�`�[�}�ԍ�';
comment on column kani_todoke_meisai_ichiran.meisai01 is '����01';
comment on column kani_todoke_meisai_ichiran.meisai02 is '����02';
comment on column kani_todoke_meisai_ichiran.meisai03 is '����03';
comment on column kani_todoke_meisai_ichiran.meisai04 is '����04';
comment on column kani_todoke_meisai_ichiran.meisai05 is '����05';
comment on column kani_todoke_meisai_ichiran.meisai06 is '����06';
comment on column kani_todoke_meisai_ichiran.meisai07 is '����07';
comment on column kani_todoke_meisai_ichiran.meisai08 is '����08';
comment on column kani_todoke_meisai_ichiran.meisai09 is '����09';
comment on column kani_todoke_meisai_ichiran.meisai10 is '����10';
comment on column kani_todoke_meisai_ichiran.meisai11 is '����11';
comment on column kani_todoke_meisai_ichiran.meisai12 is '����12';
comment on column kani_todoke_meisai_ichiran.meisai13 is '����13';
comment on column kani_todoke_meisai_ichiran.meisai14 is '����14';
comment on column kani_todoke_meisai_ichiran.meisai15 is '����15';
comment on column kani_todoke_meisai_ichiran.meisai16 is '����16';
comment on column kani_todoke_meisai_ichiran.meisai17 is '����17';
comment on column kani_todoke_meisai_ichiran.meisai18 is '����18';
comment on column kani_todoke_meisai_ichiran.meisai19 is '����19';
comment on column kani_todoke_meisai_ichiran.meisai20 is '����20';
comment on column kani_todoke_meisai_ichiran.meisai21 is '����21';
comment on column kani_todoke_meisai_ichiran.meisai22 is '����22';
comment on column kani_todoke_meisai_ichiran.meisai23 is '����23';
comment on column kani_todoke_meisai_ichiran.meisai24 is '����24';
comment on column kani_todoke_meisai_ichiran.meisai25 is '����25';
comment on column kani_todoke_meisai_ichiran.meisai26 is '����26';
comment on column kani_todoke_meisai_ichiran.meisai27 is '����27';
comment on column kani_todoke_meisai_ichiran.meisai28 is '����28';
comment on column kani_todoke_meisai_ichiran.meisai29 is '����29';
comment on column kani_todoke_meisai_ichiran.meisai30 is '����30';

comment on table kani_todoke_meta is '�͏o�W�F�l���[�^���^';
comment on column kani_todoke_meta.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_meta.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_meta.touroku_time is '�o�^����';
comment on column kani_todoke_meta.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_meta.koushin_time is '�X�V����';

comment on table kani_todoke_select_item is '�͏o�W�F�l���[�^�I������';
comment on column kani_todoke_select_item.select_item is '�I������';
comment on column kani_todoke_select_item.cd is '�R�[�h';
comment on column kani_todoke_select_item.name is '����';

comment on table kani_todoke_summary is '�͏o�W�F�l���[�^�T�}��';
comment on column kani_todoke_summary.denpyou_id is '�`�[ID';
comment on column kani_todoke_summary.ringi_kingaku is '�g�c���z';
comment on column kani_todoke_summary.shishutsu_kingaku_goukei is '�x�o���z���v';
comment on column kani_todoke_summary.shuunyuu_kingaku_goukei is '�������z���v';
comment on column kani_todoke_summary.kenmei is '����';
comment on column kani_todoke_summary.naiyou is '���e';

comment on table kani_todoke_text is '�͏o�W�F�l���[�^���ڃe�L�X�g';
comment on column kani_todoke_text.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_text.version is '�o�[�W����';
comment on column kani_todoke_text.area_kbn is '�G���A�敪';
comment on column kani_todoke_text.item_name is '���ږ�';
comment on column kani_todoke_text.label_name is '���x����';
comment on column kani_todoke_text.hissu_flg is '�K�{�t���O';
comment on column kani_todoke_text.buhin_format is '���i�`��';
comment on column kani_todoke_text.buhin_width is '���i��';
comment on column kani_todoke_text.max_length is '�ő包��';
comment on column kani_todoke_text.decimal_point is '�����_�ȉ�����';
comment on column kani_todoke_text.kotei_hyouji is '�Œ�\���t���O';
comment on column kani_todoke_text.min_value is '�ŏ��l';
comment on column kani_todoke_text.max_value is '�ő�l';
comment on column kani_todoke_text.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_text.touroku_time is '�o�^����';
comment on column kani_todoke_text.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_text.koushin_time is '�X�V����';

comment on table kani_todoke_textarea is '�͏o�W�F�l���[�^���ڃe�L�X�g�G���A';
comment on column kani_todoke_textarea.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_textarea.version is '�o�[�W����';
comment on column kani_todoke_textarea.area_kbn is '�G���A�敪';
comment on column kani_todoke_textarea.item_name is '���ږ�';
comment on column kani_todoke_textarea.label_name is '���x����';
comment on column kani_todoke_textarea.hissu_flg is '�K�{�t���O';
comment on column kani_todoke_textarea.buhin_width is '���i��';
comment on column kani_todoke_textarea.buhin_height is '���i����';
comment on column kani_todoke_textarea.max_length is '�ő包��';
comment on column kani_todoke_textarea.kotei_hyouji is '�Œ�\���t���O';
comment on column kani_todoke_textarea.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_textarea.touroku_time is '�o�^����';
comment on column kani_todoke_textarea.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_textarea.koushin_time is '�X�V����';

comment on table kani_todoke_version is '�͏o�W�F�l���[�^�o�[�W����';
comment on column kani_todoke_version.denpyou_kbn is '�`�[�敪';
comment on column kani_todoke_version.version is '�o�[�W����';
comment on column kani_todoke_version.denpyou_shubetsu is '�`�[���';
comment on column kani_todoke_version.naiyou is '���e�i�`�[�j';
comment on column kani_todoke_version.touroku_user_id is '�o�^���[�U�[ID';
comment on column kani_todoke_version.touroku_time is '�o�^����';
comment on column kani_todoke_version.koushin_user_id is '�X�V���[�U�[ID';
comment on column kani_todoke_version.koushin_time is '�X�V����';

comment on table kanren_denpyou is '�֘A�`�[';
comment on column kanren_denpyou.denpyou_id is '�`�[ID';
comment on column kanren_denpyou.kanren_denpyou is '�֘A�`�[';
comment on column kanren_denpyou.kanren_denpyou_kbn is '�֘A�`�[�敪';
comment on column kanren_denpyou.kanren_denpyou_kihyoubi is '�֘A�`�[�N�[��';
comment on column kanren_denpyou.kanren_denpyou_shouninbi is '�֘A�`�[���F��';

comment on table karibarai is '����';
comment on column karibarai.denpyou_id is '�`�[ID';
comment on column karibarai.seisan_yoteibi is '���Z�\���';
comment on column karibarai.seisan_kanryoubi is '���Z������';
comment on column karibarai.shiharaibi is '�x����';
comment on column karibarai.karibarai_on is '�����\���t���O';
comment on column karibarai.shiharaikiboubi is '�x����]��';
comment on column karibarai.shiharaihouhou is '�x�����@';
comment on column karibarai.tekiyou is '�E�v';
comment on column karibarai.kingaku is '���z';
comment on column karibarai.karibarai_kingaku is '�������z';
comment on column karibarai.hf1_cd is 'HF1�R�[�h';
comment on column karibarai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column karibarai.hf2_cd is 'HF2�R�[�h';
comment on column karibarai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column karibarai.hf3_cd is 'HF3�R�[�h';
comment on column karibarai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column karibarai.hf4_cd is 'HF4�R�[�h';
comment on column karibarai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column karibarai.hf5_cd is 'HF5�R�[�h';
comment on column karibarai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column karibarai.hf6_cd is 'HF6�R�[�h';
comment on column karibarai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column karibarai.hf7_cd is 'HF7�R�[�h';
comment on column karibarai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column karibarai.hf8_cd is 'HF8�R�[�h';
comment on column karibarai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column karibarai.hf9_cd is 'HF9�R�[�h';
comment on column karibarai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column karibarai.hf10_cd is 'HF10�R�[�h';
comment on column karibarai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column karibarai.hosoku is '�⑫';
comment on column karibarai.shiwake_edano is '�d��}�ԍ�';
comment on column karibarai.torihiki_name is '�����';
comment on column karibarai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column karibarai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column karibarai.torihikisaki_cd is '�����R�[�h';
comment on column karibarai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column karibarai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column karibarai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column karibarai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column karibarai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column karibarai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column karibarai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column karibarai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column karibarai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column karibarai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column karibarai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column karibarai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column karibarai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column karibarai.uf1_cd is 'UF1�R�[�h';
comment on column karibarai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column karibarai.uf2_cd is 'UF2�R�[�h';
comment on column karibarai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column karibarai.uf3_cd is 'UF3�R�[�h';
comment on column karibarai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column karibarai.uf4_cd is 'UF4�R�[�h';
comment on column karibarai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column karibarai.uf5_cd is 'UF5�R�[�h';
comment on column karibarai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column karibarai.uf6_cd is 'UF6�R�[�h';
comment on column karibarai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column karibarai.uf7_cd is 'UF7�R�[�h';
comment on column karibarai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column karibarai.uf8_cd is 'UF8�R�[�h';
comment on column karibarai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column karibarai.uf9_cd is 'UF9�R�[�h';
comment on column karibarai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column karibarai.uf10_cd is 'UF10�R�[�h';
comment on column karibarai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column karibarai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column karibarai.project_name is '�v���W�F�N�g��';
comment on column karibarai.segment_cd is '�Z�O�����g�R�[�h';
comment on column karibarai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column karibarai.tekiyou_cd is '�E�v�R�[�h';
comment on column karibarai.touroku_user_id is '�o�^���[�U�[ID';
comment on column karibarai.touroku_time is '�o�^����';
comment on column karibarai.koushin_user_id is '�X�V���[�U�[ID';
comment on column karibarai.koushin_time is '�X�V����';

comment on table keihiseisan is '�o��Z';
comment on column keihiseisan.denpyou_id is '�`�[ID';
comment on column keihiseisan.karibarai_denpyou_id is '�����`�[ID';
comment on column keihiseisan.karibarai_on is '�����\���t���O';
comment on column keihiseisan.karibarai_mishiyou_flg is '���������g�p�t���O';
comment on column keihiseisan.dairiflg is '�㗝�t���O';
comment on column keihiseisan.keijoubi is '�v���';
comment on column keihiseisan.shiharaibi is '�x����';
comment on column keihiseisan.shiharaikiboubi is '�x����]��';
comment on column keihiseisan.shiharaihouhou is '�x�����@';
comment on column keihiseisan.hontai_kingaku_goukei is '�{�̋��z���v';
comment on column keihiseisan.shouhizeigaku_goukei is '����Ŋz���v';
comment on column keihiseisan.shiharai_kingaku_goukei is '�x�����z���v';
comment on column keihiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column keihiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column keihiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column keihiseisan.hf1_cd is 'HF1�R�[�h';
comment on column keihiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column keihiseisan.hf2_cd is 'HF2�R�[�h';
comment on column keihiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column keihiseisan.hf3_cd is 'HF3�R�[�h';
comment on column keihiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column keihiseisan.hf4_cd is 'HF4�R�[�h';
comment on column keihiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column keihiseisan.hf5_cd is 'HF5�R�[�h';
comment on column keihiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column keihiseisan.hf6_cd is 'HF6�R�[�h';
comment on column keihiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column keihiseisan.hf7_cd is 'HF7�R�[�h';
comment on column keihiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column keihiseisan.hf8_cd is 'HF8�R�[�h';
comment on column keihiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column keihiseisan.hf9_cd is 'HF9�R�[�h';
comment on column keihiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column keihiseisan.hf10_cd is 'HF10�R�[�h';
comment on column keihiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column keihiseisan.hosoku is '�⑫';
comment on column keihiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column keihiseisan.touroku_time is '�o�^����';
comment on column keihiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column keihiseisan.koushin_time is '�X�V����';
comment on column keihiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table keihiseisan_meisai is '�o��Z����';
comment on column keihiseisan_meisai.denpyou_id is '�`�[ID';
comment on column keihiseisan_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column keihiseisan_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column keihiseisan_meisai.user_id is '���[�U�[ID';
comment on column keihiseisan_meisai.shain_no is '�Ј��ԍ�';
comment on column keihiseisan_meisai.user_sei is '���[�U�[��';
comment on column keihiseisan_meisai.user_mei is '���[�U�[��';
comment on column keihiseisan_meisai.shiyoubi is '�g�p��';
comment on column keihiseisan_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column keihiseisan_meisai.torihiki_name is '�����';
comment on column keihiseisan_meisai.tekiyou is '�E�v';
comment on column keihiseisan_meisai.zeiritsu is '�ŗ�';
comment on column keihiseisan_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column keihiseisan_meisai.shiharai_kingaku is '�x�����z';
comment on column keihiseisan_meisai.hontai_kingaku is '�{�̋��z';
comment on column keihiseisan_meisai.shouhizeigaku is '����Ŋz';
comment on column keihiseisan_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column keihiseisan_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column keihiseisan_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column keihiseisan_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column keihiseisan_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column keihiseisan_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column keihiseisan_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column keihiseisan_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column keihiseisan_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column keihiseisan_meisai.torihikisaki_cd is '�����R�[�h';
comment on column keihiseisan_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column keihiseisan_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column keihiseisan_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column keihiseisan_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column keihiseisan_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column keihiseisan_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column keihiseisan_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column keihiseisan_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column keihiseisan_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column keihiseisan_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column keihiseisan_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column keihiseisan_meisai.uf1_cd is 'UF1�R�[�h';
comment on column keihiseisan_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column keihiseisan_meisai.uf2_cd is 'UF2�R�[�h';
comment on column keihiseisan_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column keihiseisan_meisai.uf3_cd is 'UF3�R�[�h';
comment on column keihiseisan_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column keihiseisan_meisai.uf4_cd is 'UF4�R�[�h';
comment on column keihiseisan_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column keihiseisan_meisai.uf5_cd is 'UF5�R�[�h';
comment on column keihiseisan_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column keihiseisan_meisai.uf6_cd is 'UF6�R�[�h';
comment on column keihiseisan_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column keihiseisan_meisai.uf7_cd is 'UF7�R�[�h';
comment on column keihiseisan_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column keihiseisan_meisai.uf8_cd is 'UF8�R�[�h';
comment on column keihiseisan_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column keihiseisan_meisai.uf9_cd is 'UF9�R�[�h';
comment on column keihiseisan_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column keihiseisan_meisai.uf10_cd is 'UF10�R�[�h';
comment on column keihiseisan_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column keihiseisan_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column keihiseisan_meisai.project_name is '�v���W�F�N�g��';
comment on column keihiseisan_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column keihiseisan_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column keihiseisan_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column keihiseisan_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column keihiseisan_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column keihiseisan_meisai.touroku_time is '�o�^����';
comment on column keihiseisan_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column keihiseisan_meisai.koushin_time is '�X�V����';
comment on column keihiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column keihiseisan_meisai.shiharaisaki_name is '�x���於';
comment on column keihiseisan_meisai.bunri_kbn is '�����敪';
comment on column keihiseisan_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column keihiseisan_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

comment on table keijoubi_shimebi is '�v�������';
comment on column keijoubi_shimebi.denpyou_kbn is '�`�[�敪';
comment on column keijoubi_shimebi.shimebi is '����';
comment on column keijoubi_shimebi.touroku_user_id is '�o�^���[�U�[ID';
comment on column keijoubi_shimebi.touroku_time is '�o�^����';
comment on column keijoubi_shimebi.koushin_user_id is '�X�V���[�U�[ID';
comment on column keijoubi_shimebi.koushin_time is '�X�V����';

comment on table ki_bumon is '�i���ʁj����';
comment on column ki_bumon.kesn is '�������Z��';
comment on column ki_bumon.futan_bumon_cd is '���S����R�[�h';
comment on column ki_bumon.futan_bumon_name is '���S���喼';
comment on column ki_bumon.oya_syuukei_bumon_cd is '�e�W�v����R�[�h';
comment on column ki_bumon.shiire_kbn is '�d���敪';
comment on column ki_bumon.nyuryoku_from_date is '���͊J�n��';
comment on column ki_bumon.nyuryoku_to_date is '���͏I����';

comment on table ki_bumon_security is '�i���ʁj����Z�L�����e�B';
comment on column ki_bumon_security.kesn is '�������Z��';
comment on column ki_bumon_security.sptn is '�Z�L�����e�B�p�^�[��';
comment on column ki_bumon_security.futan_bumon_cd is '���S����R�[�h';

comment on table ki_busho_shiwake is '�i���ʁj�������o�͎d��';
comment on column ki_busho_shiwake.kesn is '�������Z��';
comment on column ki_busho_shiwake.dkei is '�o�ߌ�';
comment on column ki_busho_shiwake.dseq is '�`�[SEQ';
comment on column ki_busho_shiwake.sseq is '�d��SEQ';
comment on column ki_busho_shiwake.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column ki_busho_shiwake.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column ki_busho_shiwake.valu is '�i�I�[�v���Q�P�j���z';
comment on column ki_busho_shiwake.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�Ȗړ����R�[�h';
comment on column ki_busho_shiwake.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column ki_busho_shiwake.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column ki_busho_shiwake.skmk is '�i�I�[�v���Q�P�j�ݕ��@�Ȗړ����R�[�h';
comment on column ki_busho_shiwake.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column ki_busho_shiwake.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';

comment on table ki_kamoku is '�i���ʁj�Ȗ�';
comment on column ki_kamoku.kesn is '�������Z��';
comment on column ki_kamoku.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column ki_kamoku.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column ki_kamoku.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column ki_kamoku.taishaku_zokusei is '�ݎؑ���';

comment on table ki_kamoku_edaban is '�i���ʁj�Ȗڎ}��';
comment on column ki_kamoku_edaban.kesn is '�������Z��';
comment on column ki_kamoku_edaban.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column ki_kamoku_edaban.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column ki_kamoku_edaban.edaban_name is '�}�Ԗ�';

comment on table ki_kamoku_security is '�i���ʁj�ȖڃZ�L�����e�B';
comment on column ki_kamoku_security.kesn is '�������Z��';
comment on column ki_kamoku_security.sptn is '�Z�L�����e�B�p�^�[��';
comment on column ki_kamoku_security.kamoku_naibu_cd is '�Ȗړ����R�[�h';

comment on table ki_kesn is '�i���ʁj���Z��';
comment on column ki_kesn.kesn is '�������Z��';
comment on column ki_kesn.kessanki_bangou is '���Z���ԍ�';
comment on column ki_kesn.from_date is '�J�n��';
comment on column ki_kesn.to_date is '�I����';

comment on table ki_shiwake is '�i���ʁj�����d��';
comment on column ki_shiwake.kesn is '�������Z��';
comment on column ki_shiwake.dkei is '�o�ߌ�';
comment on column ki_shiwake.dseq is '�`�[SEQ';
comment on column ki_shiwake.sseq is '�d��SEQ';
comment on column ki_shiwake.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column ki_shiwake.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column ki_shiwake.valu is '�i�I�[�v���Q�P�j���z';
comment on column ki_shiwake.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�Ȗړ����R�[�h';
comment on column ki_shiwake.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column ki_shiwake.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column ki_shiwake.rtky is '�i�I�[�v���Q�P�j�ؕ��@�E�v';
comment on column ki_shiwake.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column ki_shiwake.skmk is '�i�I�[�v���Q�P�j�ݕ��@�Ȗړ����R�[�h';
comment on column ki_shiwake.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column ki_shiwake.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column ki_shiwake.stky is '�i�I�[�v���Q�P�j�ݕ��@�E�v';
comment on column ki_shiwake.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column ki_shiwake.fway is '���͎�i';

comment on table ki_shouhizei_setting is '�i���ʁj����Őݒ�';
comment on column ki_shouhizei_setting.kesn is '�������Z��';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '�d���Ŋz���t���O';
comment on column ki_shouhizei_setting.shouhizei_kbn is '����ŋ敪';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '�[�������t���O';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '�Ŋz�v�Z�t���O';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '����őΏۉȖږ����̓t���O';
comment on column ki_shouhizei_setting.shisan is '���Y';
comment on column ki_shouhizei_setting.uriage is '����';
comment on column ki_shouhizei_setting.shiire is '�d��';
comment on column ki_shouhizei_setting.keihi is '�o��';
comment on column ki_shouhizei_setting.bumonbetsu_shori is '����ʏ���';
comment on column ki_shouhizei_setting.tokuteishiire is '����d�������';
comment on column ki_shouhizei_setting.zero_shouhizei is '0�~����ō쐬';
comment on column ki_shouhizei_setting.shouhizei_bumon is '�����E�������Ł@����';
comment on column ki_shouhizei_setting.shouhizei_torihikisaki is '�����';
comment on column ki_shouhizei_setting.shouhizei_edaban is '�}��';
comment on column ki_shouhizei_setting.shouhizei_project is '�v���W�F�N�g';
comment on column ki_shouhizei_setting.shouhizei_segment is '�Z�O�����g';
comment on column ki_shouhizei_setting.shouhizei_uf1 is '���j�o�[�T���P';
comment on column ki_shouhizei_setting.shouhizei_uf2 is '���j�o�[�T���Q';
comment on column ki_shouhizei_setting.shouhizei_uf3 is '���j�o�[�T���R';
comment on column ki_shouhizei_setting.shouhizei_kouji is '�H��';
comment on column ki_shouhizei_setting.shouhizei_koushu is '�H��';
comment on column ki_shouhizei_setting.ukeshouhizei_uriage is '�������Ł@����';
comment on column ki_shouhizei_setting.ukeshouhizei_shisan is '���Y';
comment on column ki_shouhizei_setting.haraishouhizei_shiire is '��������Ł@�d��';
comment on column ki_shouhizei_setting.haraishouhizei_keihi is '�o��';
comment on column ki_shouhizei_setting.haraisyouhizei_shisan is '���Y';
comment on column ki_shouhizei_setting.uriagezeigaku_keisan is '����Ŋz�v�Z����';
comment on column ki_shouhizei_setting.shiirezeigaku_keisan is '�d���Ŋz�v�Z����';
comment on column ki_shouhizei_setting.shiirezeigaku_keikasothi is '�d���Ŋz�T���o�ߑ[�u�K�p';

comment on table ki_syuukei_bumon is '�i���ʁj�W�v����';
comment on column ki_syuukei_bumon.kesn is '�������Z��';
comment on column ki_syuukei_bumon.syuukei_bumon_cd is '�W�v����R�[�h';
comment on column ki_syuukei_bumon.futan_bumon_cd is '���S����R�[�h';
comment on column ki_syuukei_bumon.syuukei_bumon_name is '�W�v���喼';
comment on column ki_syuukei_bumon.futan_bumon_name is '���S���喼';

comment on table kian_bangou_bo is '�N�Ĕԍ���';
comment on column kian_bangou_bo.bumon_cd is '����R�[�h';
comment on column kian_bangou_bo.nendo is '�N�x';
comment on column kian_bangou_bo.ryakugou is '����';
comment on column kian_bangou_bo.kian_bangou_from is '�J�n�N�Ĕԍ�';
comment on column kian_bangou_bo.kian_bangou_to is '�I���N�Ĕԍ�';
comment on column kian_bangou_bo.kbn_naiyou is '�敪���e';
comment on column kian_bangou_bo.kianbangou_bo_sentaku_hyouji_flg is '�N�Ĕԍ���I��\���t���O';
comment on column kian_bangou_bo.denpyou_kensaku_hyouji_flg is '�`�[�����\���t���O';
comment on column kian_bangou_bo.yuukou_kigen_from is '�L�������J�n��';
comment on column kian_bangou_bo.yuukou_kigen_to is '�L�������I����';
comment on column kian_bangou_bo.hyouji_jun is '�\����';

comment on table kian_bangou_saiban is '�N�Ĕԍ��̔�';
comment on column kian_bangou_saiban.bumon_cd is '����R�[�h';
comment on column kian_bangou_saiban.nendo is '�N�x';
comment on column kian_bangou_saiban.ryakugou is '����';
comment on column kian_bangou_saiban.kian_bangou_from is '�J�n�N�Ĕԍ�';
comment on column kian_bangou_saiban.kian_bangou_last is '�ŏI�N�Ĕԍ�';

comment on table kinou_seigyo is '�@�\����';
comment on column kinou_seigyo.kinou_seigyo_cd is '�@�\����R�[�h';
comment on column kinou_seigyo.kinou_seigyo_kbn is '�@�\����敪';

comment on table kinyuukikan is '���Z�@��';
comment on column kinyuukikan.kinyuukikan_cd is '���Z�@�փR�[�h';
comment on column kinyuukikan.kinyuukikan_shiten_cd is '���Z�@�֎x�X�R�[�h';
comment on column kinyuukikan.kinyuukikan_name_hankana is '���Z�@�֖��i���p�J�i�j';
comment on column kinyuukikan.kinyuukikan_name_kana is '���Z�@�֖�';
comment on column kinyuukikan.shiten_name_hankana is '�x�X���i���p�J�i�j';
comment on column kinyuukikan.shiten_name_kana is '�x�X��';

comment on table koutsuu_shudan_master is '�����p��ʎ�i�}�X�^�[';
comment on column koutsuu_shudan_master.sort_jun is '���я�';
comment on column koutsuu_shudan_master.koutsuu_shudan is '��ʎ�i';
comment on column koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column koutsuu_shudan_master.zei_kubun is '�ŋ敪';
comment on column koutsuu_shudan_master.edaban is '�}�ԃR�[�h';
comment on column koutsuu_shudan_master.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column koutsuu_shudan_master.tanka is '�P��';
comment on column koutsuu_shudan_master.suuryou_kigou is '���ʋL��';

comment on table koutsuuhiseisan is '��ʔ�Z';
comment on column koutsuuhiseisan.denpyou_id is '�`�[ID';
comment on column koutsuuhiseisan.mokuteki is '�ړI';
comment on column koutsuuhiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column koutsuuhiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column koutsuuhiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column koutsuuhiseisan.seisankikan_to is '���Z���ԏI����';
comment on column koutsuuhiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column koutsuuhiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column koutsuuhiseisan.keijoubi is '�v���';
comment on column koutsuuhiseisan.shiharaibi is '�x����';
comment on column koutsuuhiseisan.shiharaikiboubi is '�x����]��';
comment on column koutsuuhiseisan.shiharaihouhou is '�x�����@';
comment on column koutsuuhiseisan.tekiyou is '�E�v';
comment on column koutsuuhiseisan.zeiritsu is '�ŗ�';
comment on column koutsuuhiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column koutsuuhiseisan.goukei_kingaku is '���v���z';
comment on column koutsuuhiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column koutsuuhiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column koutsuuhiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column koutsuuhiseisan.hf1_cd is 'HF1�R�[�h';
comment on column koutsuuhiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column koutsuuhiseisan.hf2_cd is 'HF2�R�[�h';
comment on column koutsuuhiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column koutsuuhiseisan.hf3_cd is 'HF3�R�[�h';
comment on column koutsuuhiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column koutsuuhiseisan.hf4_cd is 'HF4�R�[�h';
comment on column koutsuuhiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column koutsuuhiseisan.hf5_cd is 'HF5�R�[�h';
comment on column koutsuuhiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column koutsuuhiseisan.hf6_cd is 'HF6�R�[�h';
comment on column koutsuuhiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column koutsuuhiseisan.hf7_cd is 'HF7�R�[�h';
comment on column koutsuuhiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column koutsuuhiseisan.hf8_cd is 'HF8�R�[�h';
comment on column koutsuuhiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column koutsuuhiseisan.hf9_cd is 'HF9�R�[�h';
comment on column koutsuuhiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column koutsuuhiseisan.hf10_cd is 'HF10�R�[�h';
comment on column koutsuuhiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column koutsuuhiseisan.hosoku is '�⑫';
comment on column koutsuuhiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column koutsuuhiseisan.torihiki_name is '�����';
comment on column koutsuuhiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column koutsuuhiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column koutsuuhiseisan.torihikisaki_cd is '�����R�[�h';
comment on column koutsuuhiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column koutsuuhiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column koutsuuhiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column koutsuuhiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column koutsuuhiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column koutsuuhiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column koutsuuhiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column koutsuuhiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column koutsuuhiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column koutsuuhiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column koutsuuhiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column koutsuuhiseisan.uf1_cd is 'UF1�R�[�h';
comment on column koutsuuhiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column koutsuuhiseisan.uf2_cd is 'UF2�R�[�h';
comment on column koutsuuhiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column koutsuuhiseisan.uf3_cd is 'UF3�R�[�h';
comment on column koutsuuhiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column koutsuuhiseisan.uf4_cd is 'UF4�R�[�h';
comment on column koutsuuhiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column koutsuuhiseisan.uf5_cd is 'UF5�R�[�h';
comment on column koutsuuhiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column koutsuuhiseisan.uf6_cd is 'UF6�R�[�h';
comment on column koutsuuhiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column koutsuuhiseisan.uf7_cd is 'UF7�R�[�h';
comment on column koutsuuhiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column koutsuuhiseisan.uf8_cd is 'UF8�R�[�h';
comment on column koutsuuhiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column koutsuuhiseisan.uf9_cd is 'UF9�R�[�h';
comment on column koutsuuhiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column koutsuuhiseisan.uf10_cd is 'UF10�R�[�h';
comment on column koutsuuhiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column koutsuuhiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column koutsuuhiseisan.project_name is '�v���W�F�N�g��';
comment on column koutsuuhiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column koutsuuhiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column koutsuuhiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column koutsuuhiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column koutsuuhiseisan.touroku_time is '�o�^����';
comment on column koutsuuhiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column koutsuuhiseisan.koushin_time is '�X�V����';
comment on column koutsuuhiseisan.bunri_kbn is '�����敪';
comment on column koutsuuhiseisan.kari_shiire_kbn is '�ؕ��d���敪';
comment on column koutsuuhiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
comment on column koutsuuhiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table koutsuuhiseisan_meisai is '��ʔ�Z����';
comment on column koutsuuhiseisan_meisai.denpyou_id is '�`�[ID';
comment on column koutsuuhiseisan_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column koutsuuhiseisan_meisai.kikan_from is '���ԊJ�n��';
comment on column koutsuuhiseisan_meisai.shubetsu_cd is '��ʃR�[�h';
comment on column koutsuuhiseisan_meisai.shubetsu1 is '��ʂP';
comment on column koutsuuhiseisan_meisai.shubetsu2 is '��ʂQ';
comment on column koutsuuhiseisan_meisai.haya_flg is '���t���O';
comment on column koutsuuhiseisan_meisai.yasu_flg is '���t���O';
comment on column koutsuuhiseisan_meisai.raku_flg is '�y�t���O';
comment on column koutsuuhiseisan_meisai.koutsuu_shudan is '��ʎ�i';
comment on column koutsuuhiseisan_meisai.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column koutsuuhiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '�̎����E���������t���O';
comment on column koutsuuhiseisan_meisai.naiyou is '���e�i����Z�j';
comment on column koutsuuhiseisan_meisai.bikou is '���l�i����Z�j';
comment on column koutsuuhiseisan_meisai.oufuku_flg is '�����t���O';
comment on column koutsuuhiseisan_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column koutsuuhiseisan_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column koutsuuhiseisan_meisai.jidounyuuryoku_flg is '�������̓t���O';
comment on column koutsuuhiseisan_meisai.tanka is '�P��';
comment on column koutsuuhiseisan_meisai.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column koutsuuhiseisan_meisai.suuryou is '����';
comment on column koutsuuhiseisan_meisai.suuryou_kigou is '���ʋL��';
comment on column koutsuuhiseisan_meisai.meisai_kingaku is '���׋��z';
comment on column koutsuuhiseisan_meisai.ic_card_no is 'IC�J�[�h�ԍ�';
comment on column koutsuuhiseisan_meisai.ic_card_sequence_no is 'IC�J�[�h�V�[�P���X�ԍ�';
comment on column koutsuuhiseisan_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column koutsuuhiseisan_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column koutsuuhiseisan_meisai.touroku_time is '�o�^����';
comment on column koutsuuhiseisan_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column koutsuuhiseisan_meisai.koushin_time is '�X�V����';
comment on column koutsuuhiseisan_meisai.shiharaisaki_name is '�x���於';
comment on column koutsuuhiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column koutsuuhiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
comment on column koutsuuhiseisan_meisai.shouhizeigaku is '����Ŋz';
comment on column koutsuuhiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';

comment on table list_item_control is '�ꗗ�\�����ڐ���';
comment on column list_item_control.kbn is '�\�����ڋ敪';
comment on column list_item_control.user_id is '���[�U�[ID';
comment on column list_item_control.gyoumu_role_id is '�Ɩ����[��ID';
comment on column list_item_control.index is '����';
comment on column list_item_control.name is '���ږ�';
comment on column list_item_control.display_flg is '�\���t���O';

comment on table mail_settei is '���[���ݒ�';
comment on column mail_settei.smtp_server_name is 'SMTP�T�[�o�[��';
comment on column mail_settei.port_no is '�|�[�g�ԍ�';
comment on column mail_settei.ninshou_houhou is '�F�ؕ��@';
comment on column mail_settei.angouka_houhou is '�Í������@';
comment on column mail_settei.mail_address is '���[���A�h���X';
comment on column mail_settei.mail_id is '���[��ID';
comment on column mail_settei.mail_password is '���[���p�X���[�h';

comment on table mail_tsuuchi is '���[���ʒm�ݒ�';
comment on column mail_tsuuchi.user_id is '���[�U�[ID';
comment on column mail_tsuuchi.tsuuchi_kbn is '�ʒm�敪';
comment on column mail_tsuuchi.soushinumu is '���M�L��';

comment on table master_kanri_hansuu is '�}�X�^�[�Ǘ��Ő�';
comment on column master_kanri_hansuu.master_id is '�}�X�^�[ID';
comment on column master_kanri_hansuu.version is '���@�[�W����';
comment on column master_kanri_hansuu.delete_flg is '�폜�t���O';
comment on column master_kanri_hansuu.file_name is '�t�@�C����';
comment on column master_kanri_hansuu.file_size is '�t�@�C���T�C�Y';
comment on column master_kanri_hansuu.content_type is '�R���e���c�^�C�v';
comment on column master_kanri_hansuu.binary_data is '�o�C�i���[�f�[�^';
comment on column master_kanri_hansuu.touroku_user_id is '�o�^���[�U�[ID';
comment on column master_kanri_hansuu.touroku_time is '�o�^����';
comment on column master_kanri_hansuu.koushin_user_id is '�X�V���[�U�[ID';
comment on column master_kanri_hansuu.koushin_time is '�X�V����';

comment on table master_kanri_ichiran is '�}�X�^�[�Ǘ��ꗗ';
comment on column master_kanri_ichiran.master_id is '�}�X�^�[ID';
comment on column master_kanri_ichiran.master_name is '�}�X�^�[��';
comment on column master_kanri_ichiran.henkou_kahi_flg is '�ύX�ۃt���O';
comment on column master_kanri_ichiran.touroku_user_id is '�o�^���[�U�[ID';
comment on column master_kanri_ichiran.touroku_time is '�o�^����';
comment on column master_kanri_ichiran.koushin_user_id is '�X�V���[�U�[ID';
comment on column master_kanri_ichiran.koushin_time is '�X�V����';

comment on table master_torikomi_ichiran_de3 is '�}�X�^�[�捞�ꗗ(de3)';
comment on column master_torikomi_ichiran_de3.master_id is '�}�X�^�[ID';
comment on column master_torikomi_ichiran_de3.master_name is '�}�X�^�[��';
comment on column master_torikomi_ichiran_de3.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_ichiran_de3.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_ichiran_de3.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_ichiran_mk2 is '�}�X�^�[�捞�ꗗ(SIAS_mk2)';
comment on column master_torikomi_ichiran_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_ichiran_mk2.master_name is '�}�X�^�[��';
comment on column master_torikomi_ichiran_mk2.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_ichiran_mk2.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_ichiran_mk2.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_ichiran_sias is '�}�X�^�[�捞�ꗗ(SIAS)';
comment on column master_torikomi_ichiran_sias.master_id is '�}�X�^�[ID';
comment on column master_torikomi_ichiran_sias.master_name is '�}�X�^�[��';
comment on column master_torikomi_ichiran_sias.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_ichiran_sias.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_ichiran_sias.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_shousai_de3 is '�}�X�^�[�捞�ڍ�(de3)';
comment on column master_torikomi_shousai_de3.master_id is '�}�X�^�[ID';
comment on column master_torikomi_shousai_de3.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_shousai_de3.et_column_name is 'eTeam�J������';
comment on column master_torikomi_shousai_de3.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_shousai_de3.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_shousai_de3.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_shousai_de3.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_shousai_de3.entry_order is '�o�^��';
comment on column master_torikomi_shousai_de3.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_shousai_mk2 is '�}�X�^�[�捞�ڍ�(SIAS_mk2)';
comment on column master_torikomi_shousai_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_shousai_mk2.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_shousai_mk2.et_column_name is 'eTeam�J������';
comment on column master_torikomi_shousai_mk2.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_shousai_mk2.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_shousai_mk2.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_shousai_mk2.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_shousai_mk2.entry_order is '�o�^��';
comment on column master_torikomi_shousai_mk2.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_shousai_sias is '�}�X�^�[�捞�ڍ�(SIAS)';
comment on column master_torikomi_shousai_sias.master_id is '�}�X�^�[ID';
comment on column master_torikomi_shousai_sias.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_shousai_sias.et_column_name is 'eTeam�J������';
comment on column master_torikomi_shousai_sias.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_shousai_sias.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_shousai_sias.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_shousai_sias.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_shousai_sias.entry_order is '�o�^��';
comment on column master_torikomi_shousai_sias.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_term_ichiran_de3 is '�}�X�^�[�捞���Ԉꗗ(de3)';
comment on column master_torikomi_term_ichiran_de3.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_de3.master_name is '�}�X�^�[��';
comment on column master_torikomi_term_ichiran_de3.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_de3.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_term_ichiran_de3.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_term_ichiran_mk2 is '�}�X�^�[�捞���Ԉꗗ(SIAS_mk2)';
comment on column master_torikomi_term_ichiran_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_mk2.master_name is '�}�X�^�[��';
comment on column master_torikomi_term_ichiran_mk2.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_mk2.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_term_ichiran_mk2.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_term_ichiran_sias is '�}�X�^�[�捞���Ԉꗗ(SIAS)';
comment on column master_torikomi_term_ichiran_sias.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_sias.master_name is '�}�X�^�[��';
comment on column master_torikomi_term_ichiran_sias.op_master_id is 'OPEN21�}�X�^�[ID';
comment on column master_torikomi_term_ichiran_sias.op_master_name is 'OPEN21�}�X�^�[��';
comment on column master_torikomi_term_ichiran_sias.torikomi_kahi_flg is '�捞�ۃt���O';

comment on table master_torikomi_term_shousai_de3 is '�}�X�^�[�捞���ԏڍ�(de3)';
comment on column master_torikomi_term_shousai_de3.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_shousai_de3.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_term_shousai_de3.et_column_name is 'eTeam�J������';
comment on column master_torikomi_term_shousai_de3.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_term_shousai_de3.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_term_shousai_de3.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_term_shousai_de3.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_term_shousai_de3.entry_order is '�o�^��';
comment on column master_torikomi_term_shousai_de3.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_term_shousai_mk2 is '�}�X�^�[�捞���ԏڍ�(SIAS_mk2)';
comment on column master_torikomi_term_shousai_mk2.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_shousai_mk2.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_term_shousai_mk2.et_column_name is 'eTeam�J������';
comment on column master_torikomi_term_shousai_mk2.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_term_shousai_mk2.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_term_shousai_mk2.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_term_shousai_mk2.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_term_shousai_mk2.entry_order is '�o�^��';
comment on column master_torikomi_term_shousai_mk2.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table master_torikomi_term_shousai_sias is '�}�X�^�[�捞���ԏڍ�(SIAS)';
comment on column master_torikomi_term_shousai_sias.master_id is '�}�X�^�[ID';
comment on column master_torikomi_term_shousai_sias.et_column_id is 'eTeam�J����ID';
comment on column master_torikomi_term_shousai_sias.et_column_name is 'eTeam�J������';
comment on column master_torikomi_term_shousai_sias.et_data_type is 'eTeam�f�[�^�^';
comment on column master_torikomi_term_shousai_sias.op_colume_id is 'OPEN21�J����ID';
comment on column master_torikomi_term_shousai_sias.op_column_name is 'OPEN21�J������';
comment on column master_torikomi_term_shousai_sias.op_data_type is 'OPEN21�f�[�^�^';
comment on column master_torikomi_term_shousai_sias.entry_order is '�o�^��';
comment on column master_torikomi_term_shousai_sias.pk_flg is '�v���C�}���[�L�[�t���O';

comment on table midashi is '���o��';
comment on column midashi.midashi_id is '���o��ID';
comment on column midashi.midashi_name is '���o����';
comment on column midashi.hyouji_jun is '�\����';
comment on column midashi.touroku_user_id is '�o�^���[�U�[ID';
comment on column midashi.touroku_time is '�o�^����';
comment on column midashi.koushin_user_id is '�X�V���[�U�[ID';
comment on column midashi.koushin_time is '�X�V����';

comment on table moto_kouza is '�U��������';
comment on column moto_kouza.moto_kinyuukikan_cd is '�U�������Z�@�փR�[�h';
comment on column moto_kouza.moto_kinyuukikan_shiten_cd is '�U�������Z�@�֎x�X�R�[�h';
comment on column moto_kouza.moto_yokinshubetsu is '�U�����a�����';
comment on column moto_kouza.moto_kouza_bangou is '�U���������ԍ�';
comment on column moto_kouza.moto_kinyuukikan_name_hankana is '�U�������Z�@�֖��i���p�J�i�j';
comment on column moto_kouza.moto_kinyuukikan_shiten_name_hankana is '�U�������Z�@�֎x�X���i���p�J�i�j';
comment on column moto_kouza.shubetsu_cd is '��ʃR�[�h';
comment on column moto_kouza.cd_kbn is '�R�[�h�敪';
comment on column moto_kouza.kaisha_cd is '��ЃR�[�h';
comment on column moto_kouza.kaisha_name_hankana is '��Ж��i���p�J�i�j';
comment on column moto_kouza.shinki_cd is '�V�K�R�[�h';
comment on column moto_kouza.furikomi_kbn is '�U���敪';

comment on table moto_kouza_shiharaiirai is '�U���������i�x���˗��j';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_cd is '�U�������Z�@�փR�[�h';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_shiten_cd is '�U�������Z�@�֎x�X�R�[�h';
comment on column moto_kouza_shiharaiirai.moto_yokinshubetsu is '�U�����a�����';
comment on column moto_kouza_shiharaiirai.moto_kouza_bangou is '�U���������ԍ�';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_name_hankana is '�U�������Z�@�֖��i���p�J�i�j';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_shiten_name_hankana is '�U�������Z�@�֎x�X���i���p�J�i�j';
comment on column moto_kouza_shiharaiirai.shubetsu_cd is '��ʃR�[�h';
comment on column moto_kouza_shiharaiirai.cd_kbn is '�R�[�h�敪';
comment on column moto_kouza_shiharaiirai.kaisha_cd is '��ЃR�[�h';
comment on column moto_kouza_shiharaiirai.kaisha_name_hankana is '��Ж��i���p�J�i�j';
comment on column moto_kouza_shiharaiirai.shinki_cd is '�V�K�R�[�h';
comment on column moto_kouza_shiharaiirai.furikomi_kbn is '�U���敪';

comment on table naibu_cd_setting is '�����R�[�h�ݒ�';
comment on column naibu_cd_setting.naibu_cd_name is '�����R�[�h��';
comment on column naibu_cd_setting.naibu_cd is '�����R�[�h';
comment on column naibu_cd_setting.name is '����';
comment on column naibu_cd_setting.hyouji_jun is '�\����';
comment on column naibu_cd_setting.option1 is '�I�v�V�����P';
comment on column naibu_cd_setting.option2 is '�I�v�V�����Q';
comment on column naibu_cd_setting.option3 is '�I�v�V�����R';

comment on table nini_comment is '�C�Ӄ���';
comment on column nini_comment.denpyou_id is '�`�[ID';
comment on column nini_comment.edano is '�}�ԍ�';
comment on column nini_comment.user_id is '���[�U�[ID';
comment on column nini_comment.user_full_name is '���[�U�[�t����';
comment on column nini_comment.comment is '�R�����g';
comment on column nini_comment.touroku_user_id is '�o�^���[�U�[ID';
comment on column nini_comment.touroku_time is '�o�^����';
comment on column nini_comment.koushin_user_id is '�X�V���[�U�[ID';
comment on column nini_comment.koushin_time is '�X�V����';

comment on table nittou_nado_master is '�����p�������}�X�^�[';
comment on column nittou_nado_master.shubetsu1 is '��ʂP';
comment on column nittou_nado_master.shubetsu2 is '��ʂQ';
comment on column nittou_nado_master.yakushoku_cd is '��E�R�[�h';
comment on column nittou_nado_master.tanka is '�P��';
comment on column nittou_nado_master.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column nittou_nado_master.nittou_shukuhakuhi_flg is '�����E�h����t���O';
comment on column nittou_nado_master.zei_kubun is '�ŋ敪';
comment on column nittou_nado_master.edaban is '�}�ԃR�[�h';

comment on table open21_kinyuukikan is '���Z�@��';
comment on column open21_kinyuukikan.kinyuukikan_cd is '���Z�@�փR�[�h';
comment on column open21_kinyuukikan.kinyuukikan_shiten_cd is '���Z�@�֎x�X�R�[�h';
comment on column open21_kinyuukikan.kinyuukikan_name_hankana is '���Z�@�֖��i���p�J�i�j';
comment on column open21_kinyuukikan.kinyuukikan_name_kana is '���Z�@�֖�';
comment on column open21_kinyuukikan.shiten_name_hankana is '�x�X���i���p�J�i�j';
comment on column open21_kinyuukikan.shiten_name_kana is '�x�X��';

comment on table password is '�p�X���[�h';
comment on column password.user_id is '���[�U�[ID';
comment on column password.password is '�p�X���[�h';

comment on table project_master is '�v���W�F�N�g�}�X�^�[';
comment on column project_master.project_cd is '�v���W�F�N�g�R�[�h';
comment on column project_master.project_name is '�v���W�F�N�g��';
comment on column project_master.shuuryou_kbn is '�I���敪';

comment on table rate_master is '����ʃ��[�g�}�X�^�[';
comment on column rate_master.heishu_cd is '����R�[�h';
comment on column rate_master.start_date is '�K�p�J�n����';
comment on column rate_master.rate is '�K�p���[�g';
comment on column rate_master.rate1 is '�K�p���[�g(�\��)';
comment on column rate_master.rate2 is '�K�p���[�g(�\��)';
comment on column rate_master.rate3 is '�K�p���[�g(�\��)';
comment on column rate_master.availability_flg is '�����l�g�p��';

comment on table ryohi_karibarai is '�����';
comment on column ryohi_karibarai.denpyou_id is '�`�[ID';
comment on column ryohi_karibarai.karibarai_on is '�����\���t���O';
comment on column ryohi_karibarai.dairiflg is '�㗝�t���O';
comment on column ryohi_karibarai.user_id is '���[�U�[ID';
comment on column ryohi_karibarai.shain_no is '�Ј��ԍ�';
comment on column ryohi_karibarai.user_sei is '���[�U�[��';
comment on column ryohi_karibarai.user_mei is '���[�U�[��';
comment on column ryohi_karibarai.houmonsaki is '�K���';
comment on column ryohi_karibarai.mokuteki is '�ړI';
comment on column ryohi_karibarai.seisankikan_from is '���Z���ԊJ�n��';
comment on column ryohi_karibarai.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column ryohi_karibarai.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column ryohi_karibarai.seisankikan_to is '���Z���ԏI����';
comment on column ryohi_karibarai.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column ryohi_karibarai.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column ryohi_karibarai.shiharaibi is '�x����';
comment on column ryohi_karibarai.shiharaikiboubi is '�x����]��';
comment on column ryohi_karibarai.shiharaihouhou is '�x�����@';
comment on column ryohi_karibarai.tekiyou is '�E�v';
comment on column ryohi_karibarai.kingaku is '���z';
comment on column ryohi_karibarai.karibarai_kingaku is '�������z';
comment on column ryohi_karibarai.sashihiki_num is '������';
comment on column ryohi_karibarai.sashihiki_tanka is '�����P��';
comment on column ryohi_karibarai.hf1_cd is 'HF1�R�[�h';
comment on column ryohi_karibarai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column ryohi_karibarai.hf2_cd is 'HF2�R�[�h';
comment on column ryohi_karibarai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column ryohi_karibarai.hf3_cd is 'HF3�R�[�h';
comment on column ryohi_karibarai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column ryohi_karibarai.hf4_cd is 'HF4�R�[�h';
comment on column ryohi_karibarai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column ryohi_karibarai.hf5_cd is 'HF5�R�[�h';
comment on column ryohi_karibarai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column ryohi_karibarai.hf6_cd is 'HF6�R�[�h';
comment on column ryohi_karibarai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column ryohi_karibarai.hf7_cd is 'HF7�R�[�h';
comment on column ryohi_karibarai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column ryohi_karibarai.hf8_cd is 'HF8�R�[�h';
comment on column ryohi_karibarai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column ryohi_karibarai.hf9_cd is 'HF9�R�[�h';
comment on column ryohi_karibarai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column ryohi_karibarai.hf10_cd is 'HF10�R�[�h';
comment on column ryohi_karibarai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column ryohi_karibarai.hosoku is '�⑫';
comment on column ryohi_karibarai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohi_karibarai.torihiki_name is '�����';
comment on column ryohi_karibarai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohi_karibarai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohi_karibarai.torihikisaki_cd is '�����R�[�h';
comment on column ryohi_karibarai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohi_karibarai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohi_karibarai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohi_karibarai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohi_karibarai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohi_karibarai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohi_karibarai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohi_karibarai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohi_karibarai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohi_karibarai.uf1_cd is 'UF1�R�[�h';
comment on column ryohi_karibarai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohi_karibarai.uf2_cd is 'UF2�R�[�h';
comment on column ryohi_karibarai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohi_karibarai.uf3_cd is 'UF3�R�[�h';
comment on column ryohi_karibarai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohi_karibarai.uf4_cd is 'UF4�R�[�h';
comment on column ryohi_karibarai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohi_karibarai.uf5_cd is 'UF5�R�[�h';
comment on column ryohi_karibarai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohi_karibarai.uf6_cd is 'UF6�R�[�h';
comment on column ryohi_karibarai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohi_karibarai.uf7_cd is 'UF7�R�[�h';
comment on column ryohi_karibarai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohi_karibarai.uf8_cd is 'UF8�R�[�h';
comment on column ryohi_karibarai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohi_karibarai.uf9_cd is 'UF9�R�[�h';
comment on column ryohi_karibarai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohi_karibarai.uf10_cd is 'UF10�R�[�h';
comment on column ryohi_karibarai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohi_karibarai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohi_karibarai.project_name is '�v���W�F�N�g��';
comment on column ryohi_karibarai.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohi_karibarai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohi_karibarai.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohi_karibarai.seisan_kanryoubi is '���Z������';
comment on column ryohi_karibarai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohi_karibarai.touroku_time is '�o�^����';
comment on column ryohi_karibarai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohi_karibarai.koushin_time is '�X�V����';

comment on table ryohi_karibarai_keihi_meisai is '������o���';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '�`�[ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '�g�p��';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '�����';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '�E�v';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '�ŗ�';
comment on column ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohi_karibarai_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column ryohi_karibarai_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohi_karibarai_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohi_karibarai_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohi_karibarai_keihi_meisai.touroku_time is '�o�^����';
comment on column ryohi_karibarai_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohi_karibarai_keihi_meisai.koushin_time is '�X�V����';

comment on table ryohi_karibarai_meisai is '���������';
comment on column ryohi_karibarai_meisai.denpyou_id is '�`�[ID';
comment on column ryohi_karibarai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohi_karibarai_meisai.kikan_from is '���ԊJ�n��';
comment on column ryohi_karibarai_meisai.kikan_to is '���ԏI����';
comment on column ryohi_karibarai_meisai.kyuujitsu_nissuu is '�x������';
comment on column ryohi_karibarai_meisai.shubetsu_cd is '��ʃR�[�h';
comment on column ryohi_karibarai_meisai.shubetsu1 is '��ʂP';
comment on column ryohi_karibarai_meisai.shubetsu2 is '��ʂQ';
comment on column ryohi_karibarai_meisai.haya_flg is '���t���O';
comment on column ryohi_karibarai_meisai.yasu_flg is '���t���O';
comment on column ryohi_karibarai_meisai.raku_flg is '�y�t���O';
comment on column ryohi_karibarai_meisai.koutsuu_shudan is '��ʎ�i';
comment on column ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column ryohi_karibarai_meisai.naiyou is '���e�i����Z�j';
comment on column ryohi_karibarai_meisai.bikou is '���l�i����Z�j';
comment on column ryohi_karibarai_meisai.oufuku_flg is '�����t���O';
comment on column ryohi_karibarai_meisai.jidounyuuryoku_flg is '�������̓t���O';
comment on column ryohi_karibarai_meisai.nissuu is '����';
comment on column ryohi_karibarai_meisai.tanka is '�P��';
comment on column ryohi_karibarai_meisai.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column ryohi_karibarai_meisai.suuryou is '����';
comment on column ryohi_karibarai_meisai.suuryou_kigou is '���ʋL��';
comment on column ryohi_karibarai_meisai.meisai_kingaku is '���׋��z';
comment on column ryohi_karibarai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohi_karibarai_meisai.touroku_time is '�o�^����';
comment on column ryohi_karibarai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohi_karibarai_meisai.koushin_time is '�X�V����';

comment on table ryohiseisan is '����Z';
comment on column ryohiseisan.denpyou_id is '�`�[ID';
comment on column ryohiseisan.karibarai_denpyou_id is '�����`�[ID';
comment on column ryohiseisan.karibarai_on is '�����\���t���O';
comment on column ryohiseisan.karibarai_mishiyou_flg is '���������g�p�t���O';
comment on column ryohiseisan.shucchou_chuushi_flg is '�o�����~�t���O';
comment on column ryohiseisan.dairiflg is '�㗝�t���O';
comment on column ryohiseisan.user_id is '���[�U�[ID';
comment on column ryohiseisan.shain_no is '�Ј��ԍ�';
comment on column ryohiseisan.user_sei is '���[�U�[��';
comment on column ryohiseisan.user_mei is '���[�U�[��';
comment on column ryohiseisan.houmonsaki is '�K���';
comment on column ryohiseisan.mokuteki is '�ړI';
comment on column ryohiseisan.seisankikan_from is '���Z���ԊJ�n��';
comment on column ryohiseisan.seisankikan_from_hour is '���Z���ԊJ�n�����i���j';
comment on column ryohiseisan.seisankikan_from_min is '���Z���ԊJ�n�����i���j';
comment on column ryohiseisan.seisankikan_to is '���Z���ԏI����';
comment on column ryohiseisan.seisankikan_to_hour is '���Z���ԏI�������i���j';
comment on column ryohiseisan.seisankikan_to_min is '���Z���ԏI�������i���j';
comment on column ryohiseisan.keijoubi is '�v���';
comment on column ryohiseisan.shiharaibi is '�x����';
comment on column ryohiseisan.shiharaikiboubi is '�x����]��';
comment on column ryohiseisan.shiharaihouhou is '�x�����@';
comment on column ryohiseisan.tekiyou is '�E�v';
comment on column ryohiseisan.zeiritsu is '�ŗ�';
comment on column ryohiseisan.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohiseisan.goukei_kingaku is '���v���z';
comment on column ryohiseisan.houjin_card_riyou_kingaku is '���@�l�J�[�h���p���v';
comment on column ryohiseisan.kaisha_tehai_kingaku is '��Ў�z���v';
comment on column ryohiseisan.sashihiki_shikyuu_kingaku is '�����x�����z';
comment on column ryohiseisan.sashihiki_num is '������';
comment on column ryohiseisan.sashihiki_tanka is '�����P��';
comment on column ryohiseisan.hf1_cd is 'HF1�R�[�h';
comment on column ryohiseisan.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column ryohiseisan.hf2_cd is 'HF2�R�[�h';
comment on column ryohiseisan.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column ryohiseisan.hf3_cd is 'HF3�R�[�h';
comment on column ryohiseisan.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column ryohiseisan.hf4_cd is 'HF4�R�[�h';
comment on column ryohiseisan.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column ryohiseisan.hf5_cd is 'HF5�R�[�h';
comment on column ryohiseisan.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column ryohiseisan.hf6_cd is 'HF6�R�[�h';
comment on column ryohiseisan.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column ryohiseisan.hf7_cd is 'HF7�R�[�h';
comment on column ryohiseisan.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column ryohiseisan.hf8_cd is 'HF8�R�[�h';
comment on column ryohiseisan.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column ryohiseisan.hf9_cd is 'HF9�R�[�h';
comment on column ryohiseisan.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column ryohiseisan.hf10_cd is 'HF10�R�[�h';
comment on column ryohiseisan.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column ryohiseisan.hosoku is '�⑫';
comment on column ryohiseisan.shiwake_edano is '�d��}�ԍ�';
comment on column ryohiseisan.torihiki_name is '�����';
comment on column ryohiseisan.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohiseisan.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohiseisan.torihikisaki_cd is '�����R�[�h';
comment on column ryohiseisan.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohiseisan.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohiseisan.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohiseisan.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohiseisan.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohiseisan.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohiseisan.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohiseisan.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohiseisan.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohiseisan.uf1_cd is 'UF1�R�[�h';
comment on column ryohiseisan.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohiseisan.uf2_cd is 'UF2�R�[�h';
comment on column ryohiseisan.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohiseisan.uf3_cd is 'UF3�R�[�h';
comment on column ryohiseisan.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohiseisan.uf4_cd is 'UF4�R�[�h';
comment on column ryohiseisan.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohiseisan.uf5_cd is 'UF5�R�[�h';
comment on column ryohiseisan.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohiseisan.uf6_cd is 'UF6�R�[�h';
comment on column ryohiseisan.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohiseisan.uf7_cd is 'UF7�R�[�h';
comment on column ryohiseisan.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohiseisan.uf8_cd is 'UF8�R�[�h';
comment on column ryohiseisan.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohiseisan.uf9_cd is 'UF9�R�[�h';
comment on column ryohiseisan.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohiseisan.uf10_cd is 'UF10�R�[�h';
comment on column ryohiseisan.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohiseisan.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohiseisan.project_name is '�v���W�F�N�g��';
comment on column ryohiseisan.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohiseisan.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohiseisan.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohiseisan.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohiseisan.touroku_time is '�o�^����';
comment on column ryohiseisan.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohiseisan.koushin_time is '�X�V����';
comment on column ryohiseisan.bunri_kbn is '�����敪';
comment on column ryohiseisan.kari_shiire_kbn is '�ؕ��d���敪';
comment on column ryohiseisan.kashi_shiire_kbn is '�ݕ��d���敪';
comment on column ryohiseisan.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table ryohiseisan_keihi_meisai is '����Z�o���';
comment on column ryohiseisan_keihi_meisai.denpyou_id is '�`�[ID';
comment on column ryohiseisan_keihi_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohiseisan_keihi_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column ryohiseisan_keihi_meisai.shiyoubi is '�g�p��';
comment on column ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column ryohiseisan_keihi_meisai.torihiki_name is '�����';
comment on column ryohiseisan_keihi_meisai.tekiyou is '�E�v';
comment on column ryohiseisan_keihi_meisai.zeiritsu is '�ŗ�';
comment on column ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column ryohiseisan_keihi_meisai.shiharai_kingaku is '�x�����z';
comment on column ryohiseisan_keihi_meisai.hontai_kingaku is '�{�̋��z';
comment on column ryohiseisan_keihi_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column ryohiseisan_keihi_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column ryohiseisan_keihi_meisai.torihikisaki_cd is '�����R�[�h';
comment on column ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan_keihi_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column ryohiseisan_keihi_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column ryohiseisan_keihi_meisai.uf1_cd is 'UF1�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column ryohiseisan_keihi_meisai.uf2_cd is 'UF2�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column ryohiseisan_keihi_meisai.uf3_cd is 'UF3�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column ryohiseisan_keihi_meisai.uf4_cd is 'UF4�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column ryohiseisan_keihi_meisai.uf5_cd is 'UF5�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column ryohiseisan_keihi_meisai.uf6_cd is 'UF6�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column ryohiseisan_keihi_meisai.uf7_cd is 'UF7�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column ryohiseisan_keihi_meisai.uf8_cd is 'UF8�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column ryohiseisan_keihi_meisai.uf9_cd is 'UF9�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column ryohiseisan_keihi_meisai.uf10_cd is 'UF10�R�[�h';
comment on column ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column ryohiseisan_keihi_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column ryohiseisan_keihi_meisai.project_name is '�v���W�F�N�g��';
comment on column ryohiseisan_keihi_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column ryohiseisan_keihi_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column ryohiseisan_keihi_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column ryohiseisan_keihi_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column ryohiseisan_keihi_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohiseisan_keihi_meisai.touroku_time is '�o�^����';
comment on column ryohiseisan_keihi_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohiseisan_keihi_meisai.koushin_time is '�X�V����';
comment on column ryohiseisan_keihi_meisai.shiharaisaki_name is '�x���於';
comment on column ryohiseisan_keihi_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column ryohiseisan_keihi_meisai.bunri_kbn is '�����敪';
comment on column ryohiseisan_keihi_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column ryohiseisan_keihi_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

comment on table ryohiseisan_meisai is '����Z����';
comment on column ryohiseisan_meisai.denpyou_id is '�`�[ID';
comment on column ryohiseisan_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column ryohiseisan_meisai.kikan_from is '���ԊJ�n��';
comment on column ryohiseisan_meisai.kikan_to is '���ԏI����';
comment on column ryohiseisan_meisai.kyuujitsu_nissuu is '�x������';
comment on column ryohiseisan_meisai.shubetsu_cd is '��ʃR�[�h';
comment on column ryohiseisan_meisai.shubetsu1 is '��ʂP';
comment on column ryohiseisan_meisai.shubetsu2 is '��ʂQ';
comment on column ryohiseisan_meisai.haya_flg is '���t���O';
comment on column ryohiseisan_meisai.yasu_flg is '���t���O';
comment on column ryohiseisan_meisai.raku_flg is '�y�t���O';
comment on column ryohiseisan_meisai.koutsuu_shudan is '��ʎ�i';
comment on column ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '�؜ߏ��ޕK�{�t���O';
comment on column ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '�̎����E���������t���O';
comment on column ryohiseisan_meisai.naiyou is '���e�i����Z�j';
comment on column ryohiseisan_meisai.bikou is '���l�i����Z�j';
comment on column ryohiseisan_meisai.oufuku_flg is '�����t���O';
comment on column ryohiseisan_meisai.houjin_card_riyou_flg is '�@�l�J�[�h���p�t���O';
comment on column ryohiseisan_meisai.kaisha_tehai_flg is '��Ў�z�t���O';
comment on column ryohiseisan_meisai.jidounyuuryoku_flg is '�������̓t���O';
comment on column ryohiseisan_meisai.nissuu is '����';
comment on column ryohiseisan_meisai.tanka is '�P��';
comment on column ryohiseisan_meisai.suuryou_nyuryoku_type is '���ʓ��̓^�C�v';
comment on column ryohiseisan_meisai.suuryou is '����';
comment on column ryohiseisan_meisai.suuryou_kigou is '���ʋL��';
comment on column ryohiseisan_meisai.meisai_kingaku is '���׋��z';
comment on column ryohiseisan_meisai.ic_card_no is 'IC�J�[�h�ԍ�';
comment on column ryohiseisan_meisai.ic_card_sequence_no is 'IC�J�[�h�V�[�P���X�ԍ�';
comment on column ryohiseisan_meisai.himoduke_card_meisai is '�R�t���J�[�h����';
comment on column ryohiseisan_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column ryohiseisan_meisai.touroku_time is '�o�^����';
comment on column ryohiseisan_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column ryohiseisan_meisai.koushin_time is '�X�V����';
comment on column ryohiseisan_meisai.shiharaisaki_name is '�x���於';
comment on column ryohiseisan_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column ryohiseisan_meisai.zeinuki_kingaku is '�Ŕ����z';
comment on column ryohiseisan_meisai.shouhizeigaku is '����Ŋz';
comment on column ryohiseisan_meisai.zeigaku_fix_flg is '�Ŋz�C���t���O';

comment on table saiban_kanri is '�̔ԊǗ�';
comment on column saiban_kanri.saiban_kbn is '�̔ԋ敪';
comment on column saiban_kanri.sequence_val is '�V�[�P���X�l';

comment on table saishuu_syounin_route_ko is '�ŏI���F���[�g�q';
comment on column saishuu_syounin_route_ko.denpyou_kbn is '�`�[�敪';
comment on column saishuu_syounin_route_ko.edano is '�}�ԍ�';
comment on column saishuu_syounin_route_ko.edaedano is '�}�}�ԍ�';
comment on column saishuu_syounin_route_ko.gyoumu_role_id is '�Ɩ����[��ID';
comment on column saishuu_syounin_route_ko.saishuu_shounin_shori_kengen_name is '�ŏI���F����������';
comment on column saishuu_syounin_route_ko.touroku_user_id is '�o�^���[�U�[ID';
comment on column saishuu_syounin_route_ko.touroku_time is '�o�^����';
comment on column saishuu_syounin_route_ko.koushin_user_id is '�X�V���[�U�[ID';
comment on column saishuu_syounin_route_ko.koushin_time is '�X�V����';

comment on table saishuu_syounin_route_oya is '�ŏI���F���[�g�e';
comment on column saishuu_syounin_route_oya.denpyou_kbn is '�`�[�敪';
comment on column saishuu_syounin_route_oya.edano is '�}�ԍ�';
comment on column saishuu_syounin_route_oya.chuuki_mongon is '���L����';
comment on column saishuu_syounin_route_oya.yuukou_kigen_from is '�L�������J�n��';
comment on column saishuu_syounin_route_oya.yuukou_kigen_to is '�L�������I����';
comment on column saishuu_syounin_route_oya.touroku_user_id is '�o�^���[�U�[ID';
comment on column saishuu_syounin_route_oya.touroku_time is '�o�^����';
comment on column saishuu_syounin_route_oya.koushin_user_id is '�X�V���[�U�[ID';
comment on column saishuu_syounin_route_oya.koushin_time is '�X�V����';

comment on table security_log is '�Z�L�����e�B���O';
comment on column security_log.serial_no is '�V���A���ԍ�';
comment on column security_log.event_time is '�C�x���g����';
comment on column security_log.ip is '�ڑ���IP�A�h���X';
comment on column security_log.ip_xforwarded is 'IP�A�h���X(X-FORWARDED-FOR)';
comment on column security_log.user_id is '���[�U�[ID';
comment on column security_log.gyoumu_role_id is '�Ɩ����[��ID';
comment on column security_log.target is '����Ώ�';
comment on column security_log.type is '���O���';
comment on column security_log.detail is '���O�ڍ�';

comment on table segment_kamoku_zandaka is '�Z�O�����g�Ȗڎc��';
comment on column segment_kamoku_zandaka.segment_cd is '�Z�O�����g�R�[�h';
comment on column segment_kamoku_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column segment_kamoku_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column segment_kamoku_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column segment_kamoku_zandaka.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column segment_kamoku_zandaka.torihikisaki_name_seishiki is '����於�i�����j';
comment on column segment_kamoku_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column segment_kamoku_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column segment_kamoku_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column segment_kamoku_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column segment_kamoku_zandaka.kishu_zandaka is '����c��';

comment on table segment_master is '�Z�O�����g�}�X�^�[';
comment on column segment_master.segment_cd is '�Z�O�����g�R�[�h';
comment on column segment_master.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column segment_master.segment_name_seishiki is '�Z�O�����g���i�����j';

comment on table seikyuushobarai is '����������';
comment on column seikyuushobarai.denpyou_id is '�`�[ID';
comment on column seikyuushobarai.keijoubi is '�v���';
comment on column seikyuushobarai.shiharai_kigen is '�x������';
comment on column seikyuushobarai.shiharaibi is '�x����';
comment on column seikyuushobarai.masref_flg is '�}�X�^�[�Q�ƃt���O';
comment on column seikyuushobarai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column seikyuushobarai.kake_flg is '�|���t���O';
comment on column seikyuushobarai.hontai_kingaku_goukei is '�{�̋��z���v';
comment on column seikyuushobarai.shouhizeigaku_goukei is '����Ŋz���v';
comment on column seikyuushobarai.shiharai_kingaku_goukei is '�x�����z���v';
comment on column seikyuushobarai.hf1_cd is 'HF1�R�[�h';
comment on column seikyuushobarai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column seikyuushobarai.hf2_cd is 'HF2�R�[�h';
comment on column seikyuushobarai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column seikyuushobarai.hf3_cd is 'HF3�R�[�h';
comment on column seikyuushobarai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column seikyuushobarai.hf4_cd is 'HF4�R�[�h';
comment on column seikyuushobarai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column seikyuushobarai.hf5_cd is 'HF5�R�[�h';
comment on column seikyuushobarai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column seikyuushobarai.hf6_cd is 'HF6�R�[�h';
comment on column seikyuushobarai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column seikyuushobarai.hf7_cd is 'HF7�R�[�h';
comment on column seikyuushobarai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column seikyuushobarai.hf8_cd is 'HF8�R�[�h';
comment on column seikyuushobarai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column seikyuushobarai.hf9_cd is 'HF9�R�[�h';
comment on column seikyuushobarai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column seikyuushobarai.hf10_cd is 'HF10�R�[�h';
comment on column seikyuushobarai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column seikyuushobarai.hosoku is '�⑫';
comment on column seikyuushobarai.touroku_user_id is '�o�^���[�U�[ID';
comment on column seikyuushobarai.touroku_time is '�o�^����';
comment on column seikyuushobarai.koushin_user_id is '�X�V���[�U�[ID';
comment on column seikyuushobarai.koushin_time is '�X�V����';
comment on column seikyuushobarai.nyuryoku_houshiki is '���͕���';
comment on column seikyuushobarai.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table seikyuushobarai_meisai is '��������������';
comment on column seikyuushobarai_meisai.denpyou_id is '�`�[ID';
comment on column seikyuushobarai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column seikyuushobarai_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column seikyuushobarai_meisai.torihiki_name is '�����';
comment on column seikyuushobarai_meisai.tekiyou is '�E�v';
comment on column seikyuushobarai_meisai.zeiritsu is '�ŗ�';
comment on column seikyuushobarai_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column seikyuushobarai_meisai.shiharai_kingaku is '�x�����z';
comment on column seikyuushobarai_meisai.hontai_kingaku is '�{�̋��z';
comment on column seikyuushobarai_meisai.shouhizeigaku is '����Ŋz';
comment on column seikyuushobarai_meisai.kousaihi_shousai_hyouji_flg is '���۔�ڍו\���t���O';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column seikyuushobarai_meisai.kousaihi_shousai is '���۔�ڍ�';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu is '���۔�l��';
comment on column seikyuushobarai_meisai.kousaihi_hitori_kingaku is '���۔��l��������z';
comment on column seikyuushobarai_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column seikyuushobarai_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column seikyuushobarai_meisai.torihikisaki_cd is '�����R�[�h';
comment on column seikyuushobarai_meisai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column seikyuushobarai_meisai.furikomisaki_jouhou is '�U������';
comment on column seikyuushobarai_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column seikyuushobarai_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column seikyuushobarai_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column seikyuushobarai_meisai.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column seikyuushobarai_meisai.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column seikyuushobarai_meisai.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column seikyuushobarai_meisai.uf1_cd is 'UF1�R�[�h';
comment on column seikyuushobarai_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column seikyuushobarai_meisai.uf2_cd is 'UF2�R�[�h';
comment on column seikyuushobarai_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column seikyuushobarai_meisai.uf3_cd is 'UF3�R�[�h';
comment on column seikyuushobarai_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column seikyuushobarai_meisai.uf4_cd is 'UF4�R�[�h';
comment on column seikyuushobarai_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column seikyuushobarai_meisai.uf5_cd is 'UF5�R�[�h';
comment on column seikyuushobarai_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column seikyuushobarai_meisai.uf6_cd is 'UF6�R�[�h';
comment on column seikyuushobarai_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column seikyuushobarai_meisai.uf7_cd is 'UF7�R�[�h';
comment on column seikyuushobarai_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column seikyuushobarai_meisai.uf8_cd is 'UF8�R�[�h';
comment on column seikyuushobarai_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column seikyuushobarai_meisai.uf9_cd is 'UF9�R�[�h';
comment on column seikyuushobarai_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column seikyuushobarai_meisai.uf10_cd is 'UF10�R�[�h';
comment on column seikyuushobarai_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column seikyuushobarai_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column seikyuushobarai_meisai.project_name is '�v���W�F�N�g��';
comment on column seikyuushobarai_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column seikyuushobarai_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column seikyuushobarai_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column seikyuushobarai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column seikyuushobarai_meisai.touroku_time is '�o�^����';
comment on column seikyuushobarai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column seikyuushobarai_meisai.koushin_time is '�X�V����';
comment on column seikyuushobarai_meisai.jigyousha_kbn is '���Ǝҋ敪';
comment on column seikyuushobarai_meisai.bunri_kbn is '�����敪';
comment on column seikyuushobarai_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column seikyuushobarai_meisai.kashi_shiire_kbn is '�ݕ��d���敪';

comment on table setting_info is '�ݒ���';
comment on column setting_info.setting_name is '�ݒ薼';
comment on column setting_info.setting_name_wa is '�ݒ薼�i�a���j';
comment on column setting_info.setting_val is '�ݒ�l';
comment on column setting_info.category is '�J�e�S��';
comment on column setting_info.hyouji_jun is '�\����';
comment on column setting_info.editable_flg is '�ύX�\�t���O';
comment on column setting_info.hissu_flg is '�K�{�t���O';
comment on column setting_info.format_regex is '�t�H�[�}�b�g���K�\��';
comment on column setting_info.description is '����';

comment on table shain is '�Ј�';
comment on column shain.shain_no is '�Ј��ԍ�';
comment on column shain.user_full_name is '���[�U�[�t����';
comment on column shain.daihyou_futan_bumon_cd is '��\���S����R�[�h';
comment on column shain.yakushoku_cd is '��E�R�[�h';

comment on table shain_kouza is '�Ј�����';
comment on column shain_kouza.shain_no is '�Ј��ԍ�';
comment on column shain_kouza.saki_kinyuukikan_cd is '�U������Z�@�֋�s�R�[�h';
comment on column shain_kouza.saki_ginkou_shiten_cd is '�U�����s�x�X�R�[�h';
comment on column shain_kouza.saki_yokin_shabetsu is '�U����a�����';
comment on column shain_kouza.saki_kouza_bangou is '�U��������ԍ�';
comment on column shain_kouza.saki_kouza_meigi_kanji is '�U����������`����';
comment on column shain_kouza.saki_kouza_meigi_kana is '�U����������`�i���p�J�i�j';
comment on column shain_kouza.moto_kinyuukikan_cd is '�U�������Z�@�փR�[�h';
comment on column shain_kouza.moto_kinyuukikan_shiten_cd is '�U�������Z�@�֎x�X�R�[�h';
comment on column shain_kouza.moto_yokinshubetsu is '�U�����a�����';
comment on column shain_kouza.moto_kouza_bangou is '�U���������ԍ�';
comment on column shain_kouza.zaimu_edaban_cd is '�����}�ԃR�[�h';

comment on table shiharai_irai is '�x���˗�';
comment on column shiharai_irai.denpyou_id is '�`�[ID';
comment on column shiharai_irai.keijoubi is '�v���';
comment on column shiharai_irai.yoteibi is '�\���';
comment on column shiharai_irai.shiharaibi is '�x����';
comment on column shiharai_irai.shiharai_kijitsu is '�x������';
comment on column shiharai_irai.torihikisaki_cd is '�����R�[�h';
comment on column shiharai_irai.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column shiharai_irai.ichigensaki_torihikisaki_name is '�ꌩ�����於';
comment on column shiharai_irai.edi is 'EDI';
comment on column shiharai_irai.shiharai_goukei is '�x�����v';
comment on column shiharai_irai.sousai_goukei is '���E���v';
comment on column shiharai_irai.sashihiki_shiharai is '�����x���z';
comment on column shiharai_irai.manekin_gensen is '�T������';
comment on column shiharai_irai.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column shiharai_irai.hf1_cd is 'HF1�R�[�h';
comment on column shiharai_irai.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column shiharai_irai.hf2_cd is 'HF2�R�[�h';
comment on column shiharai_irai.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column shiharai_irai.hf3_cd is 'HF3�R�[�h';
comment on column shiharai_irai.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column shiharai_irai.hf4_cd is 'HF4�R�[�h';
comment on column shiharai_irai.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column shiharai_irai.hf5_cd is 'HF5�R�[�h';
comment on column shiharai_irai.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column shiharai_irai.hf6_cd is 'HF6�R�[�h';
comment on column shiharai_irai.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column shiharai_irai.hf7_cd is 'HF7�R�[�h';
comment on column shiharai_irai.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column shiharai_irai.hf8_cd is 'HF8�R�[�h';
comment on column shiharai_irai.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column shiharai_irai.hf9_cd is 'HF9�R�[�h';
comment on column shiharai_irai.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column shiharai_irai.hf10_cd is 'HF10�R�[�h';
comment on column shiharai_irai.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column shiharai_irai.shiharai_houhou is '�x�����@';
comment on column shiharai_irai.shiharai_shubetsu is '�x�����';
comment on column shiharai_irai.furikomi_ginkou_cd is '�U����s�R�[�h';
comment on column shiharai_irai.furikomi_ginkou_name is '�U����s����';
comment on column shiharai_irai.furikomi_ginkou_shiten_cd is '�U����s�x�X�R�[�h';
comment on column shiharai_irai.furikomi_ginkou_shiten_name is '�U����s�x�X����';
comment on column shiharai_irai.yokin_shubetsu is '�a�����';
comment on column shiharai_irai.kouza_bangou is '�����ԍ�';
comment on column shiharai_irai.kouza_meiginin is '�������`�l';
comment on column shiharai_irai.tesuuryou is '�萔��';
comment on column shiharai_irai.hosoku is '�⑫';
comment on column shiharai_irai.gyaku_shiwake_flg is '�t�d��t���O';
comment on column shiharai_irai.shutsuryoku_flg is '�o�̓t���O';
comment on column shiharai_irai.csv_upload_flg is 'CSV�A�b�v���[�h�t���O';
comment on column shiharai_irai.hassei_shubetsu is '�������';
comment on column shiharai_irai.saimu_made_flg is '���x���f�[�^�쐬�σt���O';
comment on column shiharai_irai.fb_made_flg is 'FB�f�[�^�쐬�σt���O';
comment on column shiharai_irai.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiharai_irai.touroku_time is '�o�^����';
comment on column shiharai_irai.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiharai_irai.koushin_time is '�X�V����';
comment on column shiharai_irai.nyuryoku_houshiki is '���͕���';
comment on column shiharai_irai.jigyousha_kbn is '���Ǝҋ敪';
comment on column shiharai_irai.jigyousha_no is '���Ǝғo�^�ԍ�';
comment on column shiharai_irai.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table shiharai_irai_meisai is '�x���˗�����';
comment on column shiharai_irai_meisai.denpyou_id is '�`�[ID';
comment on column shiharai_irai_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column shiharai_irai_meisai.shiwake_edano is '�d��}�ԍ�';
comment on column shiharai_irai_meisai.torihiki_name is '�����';
comment on column shiharai_irai_meisai.tekiyou is '�E�v';
comment on column shiharai_irai_meisai.shiharai_kingaku is '�x�����z';
comment on column shiharai_irai_meisai.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column shiharai_irai_meisai.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column shiharai_irai_meisai.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column shiharai_irai_meisai.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column shiharai_irai_meisai.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column shiharai_irai_meisai.zeiritsu is '�ŗ�';
comment on column shiharai_irai_meisai.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column shiharai_irai_meisai.uf1_cd is 'UF1�R�[�h';
comment on column shiharai_irai_meisai.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column shiharai_irai_meisai.uf2_cd is 'UF2�R�[�h';
comment on column shiharai_irai_meisai.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column shiharai_irai_meisai.uf3_cd is 'UF3�R�[�h';
comment on column shiharai_irai_meisai.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column shiharai_irai_meisai.uf4_cd is 'UF4�R�[�h';
comment on column shiharai_irai_meisai.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column shiharai_irai_meisai.uf5_cd is 'UF5�R�[�h';
comment on column shiharai_irai_meisai.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column shiharai_irai_meisai.uf6_cd is 'UF6�R�[�h';
comment on column shiharai_irai_meisai.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column shiharai_irai_meisai.uf7_cd is 'UF7�R�[�h';
comment on column shiharai_irai_meisai.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column shiharai_irai_meisai.uf8_cd is 'UF8�R�[�h';
comment on column shiharai_irai_meisai.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column shiharai_irai_meisai.uf9_cd is 'UF9�R�[�h';
comment on column shiharai_irai_meisai.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column shiharai_irai_meisai.uf10_cd is 'UF10�R�[�h';
comment on column shiharai_irai_meisai.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column shiharai_irai_meisai.project_cd is '�v���W�F�N�g�R�[�h';
comment on column shiharai_irai_meisai.project_name is '�v���W�F�N�g��';
comment on column shiharai_irai_meisai.segment_cd is '�Z�O�����g�R�[�h';
comment on column shiharai_irai_meisai.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column shiharai_irai_meisai.tekiyou_cd is '�E�v�R�[�h';
comment on column shiharai_irai_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiharai_irai_meisai.touroku_time is '�o�^����';
comment on column shiharai_irai_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiharai_irai_meisai.koushin_time is '�X�V����';
comment on column shiharai_irai_meisai.bunri_kbn is '�����敪';
comment on column shiharai_irai_meisai.kari_shiire_kbn is '�ؕ��d���敪';
comment on column shiharai_irai_meisai.zeinuki_kingaku is '�Ŕ����z';
comment on column shiharai_irai_meisai.shouhizeigaku is '����Ŋz';

comment on table shikkou_joukyou_ichiran_jouhou is '���s�󋵈ꗗ���';
comment on column shikkou_joukyou_ichiran_jouhou.user_id is '���[�U�[ID';
comment on column shikkou_joukyou_ichiran_jouhou.yosan_tani is '�\�Z�P��';

comment on table shiwake_de3 is '�d�󒊏o(de3)';
comment on column shiwake_de3.serial_no is '�V���A���ԍ�';
comment on column shiwake_de3.denpyou_id is '�`�[ID';
comment on column shiwake_de3.shiwake_status is '�d�󒊏o���';
comment on column shiwake_de3.touroku_time is '�o�^����';
comment on column shiwake_de3.koushin_time is '�X�V����';
comment on column shiwake_de3.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column shiwake_de3.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column shiwake_de3.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column shiwake_de3.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column shiwake_de3.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column shiwake_de3.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column shiwake_de3.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column shiwake_de3.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column shiwake_de3.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column shiwake_de3.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_de3.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column shiwake_de3.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_de3.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_de3.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_de3.tky is '�i�I�[�v���Q�P�j�E�v';
comment on column shiwake_de3.tno is '�i�I�[�v���Q�P�j�E�v�R�[�h';
comment on column shiwake_de3.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column shiwake_de3.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column shiwake_de3.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column shiwake_de3.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column shiwake_de3.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column shiwake_de3.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column shiwake_de3.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_de3.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column shiwake_de3.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_de3.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_de3.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_de3.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column shiwake_de3.valu is '�i�I�[�v���Q�P�j���z';
comment on column shiwake_de3.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column shiwake_de3.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column shiwake_de3.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column shiwake_de3.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column shiwake_de3.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column shiwake_de3.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column shiwake_de3.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column shiwake_de3.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column shiwake_de3.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column shiwake_de3.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column shiwake_de3.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column shiwake_de3.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column shiwake_de3.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column shiwake_de3.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column shiwake_de3.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column shiwake_de3.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column shiwake_de3.symd is '�i�I�[�v���Q�P�j�x����';
comment on column shiwake_de3.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column shiwake_de3.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column shiwake_de3.uymd is '�i�I�[�v���Q�P�j�����';
comment on column shiwake_de3.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_de3.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column shiwake_de3.sten is '�i�I�[�v���Q�P�j�X���t���O';
comment on column shiwake_de3.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column shiwake_de3.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column shiwake_de3.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column shiwake_de3.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column shiwake_de3.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column shiwake_de3.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column shiwake_de3.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column shiwake_de3.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_de3.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column shiwake_de3.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column shiwake_de3.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column shiwake_de3.gsep is '�i�I�[�v���Q�P�j�s��؂�';
comment on column shiwake_de3.rurizeikeisan is '�i�I�[�v���Q�P�j�ؕ��@���p����Ŋz�v�Z����';
comment on column shiwake_de3.surizeikeisan is '�i�I�[�v���Q�P�j�ݕ��@���p����Ŋz�v�Z����';
comment on column shiwake_de3.zurizeikeisan is '�i�I�[�v���Q�P�j�őΏۉȖځ@���p����Ŋz�v�Z����';
comment on column shiwake_de3.rmenzeikeika is '�i�I�[�v���Q�P�j�ؕ��@�d���Ŋz�T���o�ߑ[�u����';
comment on column shiwake_de3.smenzeikeika is '�i�I�[�v���Q�P�j�ݕ��@�d���Ŋz�T���o�ߑ[�u����';
comment on column shiwake_de3.zmenzeikeika is '�i�I�[�v���Q�P�j�őΏۉȖځ@�d���Ŋz�T���o�ߑ[�u����';

comment on table shiwake_pattern_master is '�d��p�^�[���}�X�^�[';
comment on column shiwake_pattern_master.denpyou_kbn is '�`�[�敪';
comment on column shiwake_pattern_master.shiwake_edano is '�d��}�ԍ�';
comment on column shiwake_pattern_master.delete_flg is '�폜�t���O';
comment on column shiwake_pattern_master.yuukou_kigen_from is '�L�������J�n��';
comment on column shiwake_pattern_master.yuukou_kigen_to is '�L�������I����';
comment on column shiwake_pattern_master.bunrui1 is '����1';
comment on column shiwake_pattern_master.bunrui2 is '����2';
comment on column shiwake_pattern_master.bunrui3 is '����3';
comment on column shiwake_pattern_master.torihiki_name is '�����';
comment on column shiwake_pattern_master.tekiyou_flg is '�E�v�t���O';
comment on column shiwake_pattern_master.tekiyou is '�E�v';
comment on column shiwake_pattern_master.default_hyouji_flg is '�f�t�H���g�\���t���O';
comment on column shiwake_pattern_master.kousaihi_hyouji_flg is '���۔�\���t���O';
comment on column shiwake_pattern_master.kousaihi_ninzuu_riyou_flg is '�l�����ڕ\���t���O';
comment on column shiwake_pattern_master.kousaihi_kijun_gaku is '���۔��z';
comment on column shiwake_pattern_master.kousaihi_check_houhou is '���۔�`�F�b�N���@';
comment on column shiwake_pattern_master.kousaihi_check_result is '���۔�`�F�b�N��o�^����';
comment on column shiwake_pattern_master.kake_flg is '�|���t���O';
comment on column shiwake_pattern_master.hyouji_jun is '�\����';
comment on column shiwake_pattern_master.shain_cd_renkei_flg is '�Ј��R�[�h�A�g�t���O';
comment on column shiwake_pattern_master.edaban_renkei_flg is '�����}�ԃR�[�h�A�g�t���O';
comment on column shiwake_pattern_master.kari_futan_bumon_cd is '�ؕ����S����R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_kamoku_cd is '�ؕ��ȖڃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_torihikisaki_cd is '�ؕ������R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_project_cd is '�ؕ��v���W�F�N�g�R�[�h�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_segment_cd is '�ؕ��Z�O�����g�R�[�h';
comment on column shiwake_pattern_master.kari_uf1_cd is '�ؕ�UF1�R�[�h';
comment on column shiwake_pattern_master.kari_uf2_cd is '�ؕ�UF2�R�[�h';
comment on column shiwake_pattern_master.kari_uf3_cd is '�ؕ�UF3�R�[�h';
comment on column shiwake_pattern_master.kari_uf4_cd is '�ؕ�UF4�R�[�h';
comment on column shiwake_pattern_master.kari_uf5_cd is '�ؕ�UF5�R�[�h';
comment on column shiwake_pattern_master.kari_uf6_cd is '�ؕ�UF6�R�[�h';
comment on column shiwake_pattern_master.kari_uf7_cd is '�ؕ�UF7�R�[�h';
comment on column shiwake_pattern_master.kari_uf8_cd is '�ؕ�UF8�R�[�h';
comment on column shiwake_pattern_master.kari_uf9_cd is '�ؕ�UF9�R�[�h';
comment on column shiwake_pattern_master.kari_uf10_cd is '�ؕ�UF10�R�[�h';
comment on column shiwake_pattern_master.kari_uf_kotei1_cd is '�ؕ�UF1�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei2_cd is '�ؕ�UF2�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei3_cd is '�ؕ�UF3�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei4_cd is '�ؕ�UF4�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei5_cd is '�ؕ�UF5�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei6_cd is '�ؕ�UF6�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei7_cd is '�ؕ�UF7�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei8_cd is '�ؕ�UF8�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei9_cd is '�ؕ�UF9�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_uf_kotei10_cd is '�ؕ�UF10�R�[�h�i�Œ�j';
comment on column shiwake_pattern_master.kari_kazei_kbn is '�ؕ��ېŋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_zeiritsu is '�ؕ�����ŗ��i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_keigen_zeiritsu_kbn is '�ؕ��y���ŗ��敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd1 is '�ݕ����S����R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd1 is '�ݕ��ȖڃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd1 is '�ݕ��Ȗڎ}�ԃR�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd1 is '�ݕ������R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd1 is '�ݕ��v���W�F�N�g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd1 is '�ݕ��Z�O�����g�R�[�h�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd1 is '�ݕ�UF1�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf2_cd1 is '�ݕ�UF2�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf3_cd1 is '�ݕ�UF3�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf4_cd1 is '�ݕ�UF4�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf5_cd1 is '�ݕ�UF5�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf6_cd1 is '�ݕ�UF6�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf7_cd1 is '�ݕ�UF7�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf8_cd1 is '�ݕ�UF8�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf9_cd1 is '�ݕ�UF9�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf10_cd1 is '�ݕ�UF10�R�[�h�P';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd1 is '�ݕ�UF1�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd1 is '�ݕ�UF2�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd1 is '�ݕ�UF3�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd1 is '�ݕ�UF4�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd1 is '�ݕ�UF5�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd1 is '�ݕ�UF6�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd1 is '�ݕ�UF7�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd1 is '�ݕ�UF8�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd1 is '�ݕ�UF9�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd1 is '�ݕ�UF10�R�[�h�P�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn1 is '�ݕ��ېŋ敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd2 is '�ݕ����S����R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd2 is '�ݕ������R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd2 is '�ݕ��ȖڃR�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd2 is '�ݕ��Ȗڎ}�ԃR�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd2 is '�ݕ��v���W�F�N�g�R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd2 is '�ݕ��Z�O�����g�R�[�h�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd2 is '�ݕ�UF1�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf2_cd2 is '�ݕ�UF2�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf3_cd2 is '�ݕ�UF3�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf4_cd2 is '�ݕ�UF4�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf5_cd2 is '�ݕ�UF5�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf6_cd2 is '�ݕ�UF6�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf7_cd2 is '�ݕ�UF7�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf8_cd2 is '�ݕ�UF8�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf9_cd2 is '�ݕ�UF9�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf10_cd2 is '�ݕ�UF10�R�[�h�Q';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd2 is '�ݕ�UF1�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd2 is '�ݕ�UF2�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd2 is '�ݕ�UF3�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd2 is '�ݕ�UF4�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd2 is '�ݕ�UF5�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd2 is '�ݕ�UF6�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd2 is '�ݕ�UF7�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd2 is '�ݕ�UF8�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd2 is '�ݕ�UF9�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd2 is '�ݕ�UF10�R�[�h�Q�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn2 is '�ݕ��ېŋ敪�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd3 is '�ݕ����S����R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd3 is '�ݕ������R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd3 is '�ݕ��ȖڃR�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd3 is '�ݕ��Ȗڎ}�ԃR�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd3 is '�ݕ��v���W�F�N�g�R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd3 is '�ݕ��Z�O�����g�R�[�h�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd3 is '�ݕ�UF1�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf2_cd3 is '�ݕ�UF2�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf3_cd3 is '�ݕ�UF3�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf4_cd3 is '�ݕ�UF4�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf5_cd3 is '�ݕ�UF5�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf6_cd3 is '�ݕ�UF6�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf7_cd3 is '�ݕ�UF7�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf8_cd3 is '�ݕ�UF8�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf9_cd3 is '�ݕ�UF9�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf10_cd3 is '�ݕ�UF10�R�[�h�R';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd3 is '�ݕ�UF1�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd3 is '�ݕ�UF2�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd3 is '�ݕ�UF3�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd3 is '�ݕ�UF4�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd3 is '�ݕ�UF5�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd3 is '�ݕ�UF6�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd3 is '�ݕ�UF7�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd3 is '�ݕ�UF8�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd3 is '�ݕ�UF9�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd3 is '�ݕ�UF10�R�[�h�R�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn3 is '�ݕ��ېŋ敪�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd4 is '�ݕ����S����R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd4 is '�ݕ������R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd4 is '�ݕ��ȖڃR�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd4 is '�ݕ��Ȗڎ}�ԃR�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd4 is '�ݕ��v���W�F�N�g�R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd4 is '�ݕ��Z�O�����g�R�[�h�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd4 is '�ݕ�UF1�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf2_cd4 is '�ݕ�UF2�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf3_cd4 is '�ݕ�UF3�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf4_cd4 is '�ݕ�UF4�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf5_cd4 is '�ݕ�UF5�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf6_cd4 is '�ݕ�UF6�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf7_cd4 is '�ݕ�UF7�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf8_cd4 is '�ݕ�UF8�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf9_cd4 is '�ݕ�UF9�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf10_cd4 is '�ݕ�UF10�R�[�h�S';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd4 is '�ݕ�UF1�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd4 is '�ݕ�UF2�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd4 is '�ݕ�UF3�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd4 is '�ݕ�UF4�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd4 is '�ݕ�UF5�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd4 is '�ݕ�UF6�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd4 is '�ݕ�UF7�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd4 is '�ݕ�UF8�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd4 is '�ݕ�UF9�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd4 is '�ݕ�UF10�R�[�h�S�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn4 is '�ݕ��ېŋ敪�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd5 is '�ݕ����S����R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd5 is '�ݕ������R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_cd5 is '�ݕ��ȖڃR�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd5 is '�ݕ��Ȗڎ}�ԃR�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_project_cd5 is '�ݕ��v���W�F�N�g�R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_segment_cd5 is '�ݕ��Z�O�����g�R�[�h�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_uf1_cd5 is '�ݕ�UF1�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf2_cd5 is '�ݕ�UF2�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf3_cd5 is '�ݕ�UF3�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf4_cd5 is '�ݕ�UF4�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf5_cd5 is '�ݕ�UF5�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf6_cd5 is '�ݕ�UF6�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf7_cd5 is '�ݕ�UF7�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf8_cd5 is '�ݕ�UF8�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf9_cd5 is '�ݕ�UF9�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf10_cd5 is '�ݕ�UF10�R�[�h�T';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd5 is '�ݕ�UF1�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd5 is '�ݕ�UF2�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd5 is '�ݕ�UF3�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd5 is '�ݕ�UF4�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd5 is '�ݕ�UF5�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd5 is '�ݕ�UF6�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd5 is '�ݕ�UF7�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd5 is '�ݕ�UF8�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd5 is '�ݕ�UF9�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd5 is '�ݕ�UF10�R�[�h�T�i�Œ�j';
comment on column shiwake_pattern_master.kashi_kazei_kbn5 is '�ݕ��ېŋ敪�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.touroku_user_id is '�o�^���[�U�[ID';
comment on column shiwake_pattern_master.touroku_time is '�o�^����';
comment on column shiwake_pattern_master.koushin_user_id is '�X�V���[�U�[ID';
comment on column shiwake_pattern_master.koushin_time is '�X�V����';
comment on column shiwake_pattern_master.old_kari_kazei_kbn is '���ؕ��ېŋ敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn1 is '���ݕ��ېŋ敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn2 is '���ݕ��ېŋ敪�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn3 is '���ݕ��ېŋ敪�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn4 is '���ݕ��ېŋ敪�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn5 is '���ݕ��ېŋ敪�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_bunri_kbn is '�ؕ������敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_bunri_kbn1 is '�ݕ������敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_bunri_kbn2 is '�ݕ������敪�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_bunri_kbn3 is '�ݕ������敪�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_bunri_kbn4 is '�ݕ������敪�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_bunri_kbn5 is '�ݕ������敪�T�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kari_shiire_kbn is '�ؕ��d���敪�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_shiire_kbn1 is '�ݕ��d���敪�P�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_shiire_kbn2 is '�ݕ��d���敪�Q�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_shiire_kbn3 is '�ݕ��d���敪�R�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_shiire_kbn4 is '�ݕ��d���敪�S�i�d��p�^�[���j';
comment on column shiwake_pattern_master.kashi_shiire_kbn5 is '�ݕ��d���敪�T�i�d��p�^�[���j';

comment on table shiwake_pattern_setting is '�d��p�^�[���ݒ�';
comment on column shiwake_pattern_setting.denpyou_kbn is '�`�[�敪';
comment on column shiwake_pattern_setting.setting_kbn is '�ݒ�敪';
comment on column shiwake_pattern_setting.setting_item is '�ݒ荀��';
comment on column shiwake_pattern_setting.default_value is '�f�t�H���g�l';
comment on column shiwake_pattern_setting.hyouji_flg is '�\���t���O';
comment on column shiwake_pattern_setting.shiwake_pattern_var1 is '�d��p�^�[���ϐ�1';
comment on column shiwake_pattern_setting.shiwake_pattern_var2 is '�d��p�^�[���ϐ�2';
comment on column shiwake_pattern_setting.shiwake_pattern_var3 is '�d��p�^�[���ϐ�3';
comment on column shiwake_pattern_setting.shiwake_pattern_var4 is '�d��p�^�[���ϐ�4';
comment on column shiwake_pattern_setting.shiwake_pattern_var5 is '�d��p�^�[���ϐ�5';

comment on table shiwake_pattern_var_setting is '�d��p�^�[���ϐ��ݒ�';
comment on column shiwake_pattern_var_setting.shiwake_pattern_var is '�d��p�^�[���ϐ�';
comment on column shiwake_pattern_var_setting.shiwake_pattern_var_name is '�d��p�^�[���ϐ�����';
comment on column shiwake_pattern_var_setting.var_setsumei is '�ϐ�����';
comment on column shiwake_pattern_var_setting.session_var is '�Z�b�V�����ϐ���';
comment on column shiwake_pattern_var_setting.column_name is '�J������';
comment on column shiwake_pattern_var_setting.hyouji_jun is '�\����';

comment on table shiwake_sias is '�d�󒊏o(SIAS)';
comment on column shiwake_sias.serial_no is '�V���A���ԍ�';
comment on column shiwake_sias.denpyou_id is '�`�[ID';
comment on column shiwake_sias.shiwake_status is '�d�󒊏o���';
comment on column shiwake_sias.touroku_time is '�o�^����';
comment on column shiwake_sias.koushin_time is '�X�V����';
comment on column shiwake_sias.dymd is '�i�I�[�v���Q�P�j�`�[���t';
comment on column shiwake_sias.seiri is '�i�I�[�v���Q�P�j�������t���O';
comment on column shiwake_sias.dcno is '�i�I�[�v���Q�P�j�`�[�ԍ�';
comment on column shiwake_sias.kymd is '�i�I�[�v���Q�P�j�N�[�N����';
comment on column shiwake_sias.kbmn is '�i�I�[�v���Q�P�j�N�[����R�[�h';
comment on column shiwake_sias.kusr is '�i�I�[�v���Q�P�j�N�[�҃R�[�h';
comment on column shiwake_sias.sgno is '�i�I�[�v���Q�P�j���F�O���[�vNo.';
comment on column shiwake_sias.hf1 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P';
comment on column shiwake_sias.hf2 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�Q';
comment on column shiwake_sias.hf3 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�R';
comment on column shiwake_sias.hf4 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�S';
comment on column shiwake_sias.hf5 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�T';
comment on column shiwake_sias.hf6 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�U';
comment on column shiwake_sias.hf7 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�V';
comment on column shiwake_sias.hf8 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�W';
comment on column shiwake_sias.hf9 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�X';
comment on column shiwake_sias.hf10 is '�i�I�[�v���Q�P�j�w�b�_�[�t�B�[���h�P�O';
comment on column shiwake_sias.rbmn is '�i�I�[�v���Q�P�j�ؕ��@����R�[�h';
comment on column shiwake_sias.rtor is '�i�I�[�v���Q�P�j�ؕ��@�����R�[�h';
comment on column shiwake_sias.rkmk is '�i�I�[�v���Q�P�j�ؕ��@�ȖڃR�[�h';
comment on column shiwake_sias.reda is '�i�I�[�v���Q�P�j�ؕ��@�}�ԃR�[�h';
comment on column shiwake_sias.rkoj is '�i�I�[�v���Q�P�j�ؕ��@�H���R�[�h';
comment on column shiwake_sias.rkos is '�i�I�[�v���Q�P�j�ؕ��@�H��R�[�h';
comment on column shiwake_sias.rprj is '�i�I�[�v���Q�P�j�ؕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_sias.rseg is '�i�I�[�v���Q�P�j�ؕ��@�Z�O�����g�R�[�h';
comment on column shiwake_sias.rdm1 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_sias.rdm2 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_sias.rdm3 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_sias.rdm4 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�S';
comment on column shiwake_sias.rdm5 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�T';
comment on column shiwake_sias.rdm6 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�U';
comment on column shiwake_sias.rdm7 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�V';
comment on column shiwake_sias.rdm8 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�W';
comment on column shiwake_sias.rdm9 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�X';
comment on column shiwake_sias.rdm10 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column shiwake_sias.rdm11 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column shiwake_sias.rdm12 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column shiwake_sias.rdm13 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column shiwake_sias.rdm14 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column shiwake_sias.rdm15 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column shiwake_sias.rdm16 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column shiwake_sias.rdm17 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column shiwake_sias.rdm18 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column shiwake_sias.rdm19 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column shiwake_sias.rdm20 is '�i�I�[�v���Q�P�j�ؕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column shiwake_sias.rrit is '�i�I�[�v���Q�P�j�ؕ��@�ŗ�';
comment on column shiwake_sias.rkeigen is '�i�I�[�v���Q�P�j�ؕ��@�y���ŗ��敪';
comment on column shiwake_sias.rzkb is '�i�I�[�v���Q�P�j�ؕ��@�ېŋ敪';
comment on column shiwake_sias.rgyo is '�i�I�[�v���Q�P�j�ؕ��@�Ǝ�敪';
comment on column shiwake_sias.rsre is '�i�I�[�v���Q�P�j�ؕ��@�d���敪';
comment on column shiwake_sias.rtky is '�i�I�[�v���Q�P�j�ؕ��@�E�v';
comment on column shiwake_sias.rtno is '�i�I�[�v���Q�P�j�ؕ��@�E�v�R�[�h';
comment on column shiwake_sias.sbmn is '�i�I�[�v���Q�P�j�ݕ��@����R�[�h';
comment on column shiwake_sias.stor is '�i�I�[�v���Q�P�j�ݕ��@�����R�[�h';
comment on column shiwake_sias.skmk is '�i�I�[�v���Q�P�j�ݕ��@�ȖڃR�[�h';
comment on column shiwake_sias.seda is '�i�I�[�v���Q�P�j�ݕ��@�}�ԃR�[�h';
comment on column shiwake_sias.skoj is '�i�I�[�v���Q�P�j�ݕ��@�H���R�[�h';
comment on column shiwake_sias.skos is '�i�I�[�v���Q�P�j�ݕ��@�H��R�[�h';
comment on column shiwake_sias.sprj is '�i�I�[�v���Q�P�j�ݕ��@�v���W�F�N�g�R�[�h';
comment on column shiwake_sias.sseg is '�i�I�[�v���Q�P�j�ݕ��@�Z�O�����g�R�[�h';
comment on column shiwake_sias.sdm1 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P';
comment on column shiwake_sias.sdm2 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q';
comment on column shiwake_sias.sdm3 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�R';
comment on column shiwake_sias.sdm4 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�S';
comment on column shiwake_sias.sdm5 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�T';
comment on column shiwake_sias.sdm6 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�U';
comment on column shiwake_sias.sdm7 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�V';
comment on column shiwake_sias.sdm8 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�W';
comment on column shiwake_sias.sdm9 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�X';
comment on column shiwake_sias.sdm10 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�O';
comment on column shiwake_sias.sdm11 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�P';
comment on column shiwake_sias.sdm12 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�Q';
comment on column shiwake_sias.sdm13 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�R';
comment on column shiwake_sias.sdm14 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�S';
comment on column shiwake_sias.sdm15 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�T';
comment on column shiwake_sias.sdm16 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�U';
comment on column shiwake_sias.sdm17 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�V';
comment on column shiwake_sias.sdm18 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�W';
comment on column shiwake_sias.sdm19 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�P�X';
comment on column shiwake_sias.sdm20 is '�i�I�[�v���Q�P�j�ݕ��@���j�o�[�T���t�B�[���h�Q�O';
comment on column shiwake_sias.srit is '�i�I�[�v���Q�P�j�ݕ��@�ŗ�';
comment on column shiwake_sias.skeigen is '�i�I�[�v���Q�P�j�ݕ��@�y���ŗ��敪';
comment on column shiwake_sias.szkb is '�i�I�[�v���Q�P�j�ݕ��@�ېŋ敪';
comment on column shiwake_sias.sgyo is '�i�I�[�v���Q�P�j�ݕ��@�Ǝ�敪';
comment on column shiwake_sias.ssre is '�i�I�[�v���Q�P�j�ݕ��@�d���敪';
comment on column shiwake_sias.stky is '�i�I�[�v���Q�P�j�ݕ��@�E�v';
comment on column shiwake_sias.stno is '�i�I�[�v���Q�P�j�ݕ��@�E�v�R�[�h';
comment on column shiwake_sias.zkmk is '�i�I�[�v���Q�P�j����őΏۉȖڃR�[�h';
comment on column shiwake_sias.zrit is '�i�I�[�v���Q�P�j����őΏۉȖڐŗ�';
comment on column shiwake_sias.zkeigen is '�i�I�[�v���Q�P�j����őΏۉȖځ@�y���ŗ��敪';
comment on column shiwake_sias.zzkb is '�i�I�[�v���Q�P�j����őΏۉȖځ@�ېŋ敪';
comment on column shiwake_sias.zgyo is '�i�I�[�v���Q�P�j����őΏۉȖځ@�Ǝ�敪';
comment on column shiwake_sias.zsre is '�i�I�[�v���Q�P�j����őΏۉȖځ@�d���敪';
comment on column shiwake_sias.exvl is '�i�I�[�v���Q�P�j�Ή����z';
comment on column shiwake_sias.valu is '�i�I�[�v���Q�P�j���z';
comment on column shiwake_sias.symd is '�i�I�[�v���Q�P�j�x����';
comment on column shiwake_sias.skbn is '�i�I�[�v���Q�P�j�x���敪';
comment on column shiwake_sias.skiz is '�i�I�[�v���Q�P�j�x������';
comment on column shiwake_sias.uymd is '�i�I�[�v���Q�P�j�����';
comment on column shiwake_sias.ukbn is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_sias.ukiz is '�i�I�[�v���Q�P�j�������';
comment on column shiwake_sias.dkec is '�i�I�[�v���Q�P�j�����R�[�h';
comment on column shiwake_sias.fusr is '�i�I�[�v���Q�P�j���͎҃R�[�h';
comment on column shiwake_sias.fsen is '�i�I�[�v���Q�P�j�tⳔԍ�';
comment on column shiwake_sias.tkflg is '�i�I�[�v���Q�P�j�ݎؕʓE�v�t���O';
comment on column shiwake_sias.bunri is '�i�I�[�v���Q�P�j�����敪';
comment on column shiwake_sias.heic is '�i�I�[�v���Q�P�j����';
comment on column shiwake_sias.rate is '�i�I�[�v���Q�P�j���[�g';
comment on column shiwake_sias.gexvl is '�i�I�[�v���Q�P�j�O�ݑΉ����z';
comment on column shiwake_sias.gvalu is '�i�I�[�v���Q�P�j�O�݋��z';
comment on column shiwake_sias.gsep is '�i�I�[�v���Q�P�j�s��؂�';
comment on column shiwake_sias.rurizeikeisan is '�i�I�[�v���Q�P�j�ؕ��@���p����Ŋz�v�Z����';
comment on column shiwake_sias.surizeikeisan is '�i�I�[�v���Q�P�j�ݕ��@���p����Ŋz�v�Z����';
comment on column shiwake_sias.zurizeikeisan is '�i�I�[�v���Q�P�j�őΏۉȖځ@���p����Ŋz�v�Z����';
comment on column shiwake_sias.rmenzeikeika is '�i�I�[�v���Q�P�j�ؕ��@�d���Ŋz�T���o�ߑ[�u����';
comment on column shiwake_sias.smenzeikeika is '�i�I�[�v���Q�P�j�ݕ��@�d���Ŋz�T���o�ߑ[�u����';
comment on column shiwake_sias.zmenzeikeika is '�i�I�[�v���Q�P�j�őΏۉȖځ@�d���Ŋz�T���o�ߑ[�u����';

comment on table shouhizeiritsu is '����ŗ�';
comment on column shouhizeiritsu.sort_jun is '���я�';
comment on column shouhizeiritsu.zeiritsu is '�ŗ�';
comment on column shouhizeiritsu.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column shouhizeiritsu.hasuu_keisan_kbn is '�[���v�Z�敪';
comment on column shouhizeiritsu.yuukou_kigen_from is '�L�������J�n��';
comment on column shouhizeiritsu.yuukou_kigen_to is '�L�������I����';

comment on table shounin_joukyou is '���F��';
comment on column shounin_joukyou.denpyou_id is '�`�[ID';
comment on column shounin_joukyou.edano is '�}�ԍ�';
comment on column shounin_joukyou.user_id is '���[�U�[ID';
comment on column shounin_joukyou.user_full_name is '���[�U�[�t����';
comment on column shounin_joukyou.bumon_cd is '����R�[�h';
comment on column shounin_joukyou.bumon_full_name is '����t����';
comment on column shounin_joukyou.bumon_role_id is '���働�[��ID';
comment on column shounin_joukyou.bumon_role_name is '���働�[����';
comment on column shounin_joukyou.gyoumu_role_id is '�Ɩ����[��ID';
comment on column shounin_joukyou.gyoumu_role_name is '�Ɩ����[����';
comment on column shounin_joukyou.joukyou_cd is '�󋵃R�[�h';
comment on column shounin_joukyou.joukyou is '��';
comment on column shounin_joukyou.comment is '�R�����g';
comment on column shounin_joukyou.shounin_route_edano is '���F���[�g�}�ԍ�';
comment on column shounin_joukyou.shounin_route_gougi_edano is '���F���[�g���c�}�ԍ�';
comment on column shounin_joukyou.shounin_route_gougi_edaedano is '���F���[�g���c�}�X�ԍ�';
comment on column shounin_joukyou.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column shounin_joukyou.shounin_shori_kengen_name is '���F����������';
comment on column shounin_joukyou.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_joukyou.touroku_time is '�o�^����';
comment on column shounin_joukyou.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_joukyou.koushin_time is '�X�V����';

comment on table shounin_route is '���F���[�g';
comment on column shounin_route.denpyou_id is '�`�[ID';
comment on column shounin_route.edano is '�}�ԍ�';
comment on column shounin_route.user_id is '���[�U�[ID';
comment on column shounin_route.user_full_name is '���[�U�[�t����';
comment on column shounin_route.bumon_cd is '����R�[�h';
comment on column shounin_route.bumon_full_name is '����t����';
comment on column shounin_route.bumon_role_id is '���働�[��ID';
comment on column shounin_route.bumon_role_name is '���働�[����';
comment on column shounin_route.gyoumu_role_id is '�Ɩ����[��ID';
comment on column shounin_route.gyoumu_role_name is '�Ɩ����[����';
comment on column shounin_route.genzai_flg is '���݃t���O';
comment on column shounin_route.saishu_shounin_flg is '�ŏI���F�t���O';
comment on column shounin_route.joukyou_edano is '���F�󋵎}��';
comment on column shounin_route.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column shounin_route.shounin_shori_kengen_name is '���F����������';
comment on column shounin_route.kihon_model_cd is '��{���f���R�[�h';
comment on column shounin_route.shounin_hissu_flg is '���F�K�{�t���O';
comment on column shounin_route.shounin_ken_flg is '���F���t���O';
comment on column shounin_route.henkou_flg is '�ύX�t���O';
comment on column shounin_route.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_route.touroku_time is '�o�^����';
comment on column shounin_route.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_route.koushin_time is '�X�V����';

comment on table shounin_route_gougi_ko is '���F���[�g���c�q';
comment on column shounin_route_gougi_ko.denpyou_id is '�`�[ID';
comment on column shounin_route_gougi_ko.edano is '�}�ԍ�';
comment on column shounin_route_gougi_ko.gougi_edano is '���c�}�ԍ�';
comment on column shounin_route_gougi_ko.gougi_edaedano is '���c�}�X�ԍ�';
comment on column shounin_route_gougi_ko.user_id is '���[�U�[ID';
comment on column shounin_route_gougi_ko.user_full_name is '���[�U�[�t����';
comment on column shounin_route_gougi_ko.bumon_cd is '����R�[�h';
comment on column shounin_route_gougi_ko.bumon_full_name is '����t����';
comment on column shounin_route_gougi_ko.bumon_role_id is '���働�[��ID';
comment on column shounin_route_gougi_ko.bumon_role_name is '���働�[����';
comment on column shounin_route_gougi_ko.gougi_genzai_flg is '���c���݃t���O';
comment on column shounin_route_gougi_ko.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column shounin_route_gougi_ko.shounin_shori_kengen_name is '���F����������';
comment on column shounin_route_gougi_ko.kihon_model_cd is '��{���f���R�[�h';
comment on column shounin_route_gougi_ko.shounin_hissu_flg is '���F�K�{�t���O';
comment on column shounin_route_gougi_ko.shounin_ken_flg is '���F���t���O';
comment on column shounin_route_gougi_ko.henkou_flg is '�ύX�t���O';
comment on column shounin_route_gougi_ko.shounin_ninzuu_cd is '���F�l���R�[�h';
comment on column shounin_route_gougi_ko.shounin_ninzuu_hiritsu is '���F�l���䗦';
comment on column shounin_route_gougi_ko.syori_cd is '�����R�[�h';
comment on column shounin_route_gougi_ko.gouginai_group is '���c���O���[�v';
comment on column shounin_route_gougi_ko.joukyou_edano is '���F�󋵎}��';

comment on table shounin_route_gougi_oya is '���F���[�g���c�e';
comment on column shounin_route_gougi_oya.denpyou_id is '�`�[ID';
comment on column shounin_route_gougi_oya.edano is '�}�ԍ�';
comment on column shounin_route_gougi_oya.gougi_edano is '���c�}�ԍ�';
comment on column shounin_route_gougi_oya.gougi_pattern_no is '���c�p�^�[���ԍ�';
comment on column shounin_route_gougi_oya.gougi_name is '���c��';
comment on column shounin_route_gougi_oya.syori_cd is '�����R�[�h';

comment on table shounin_shori_kengen is '���F��������';
comment on column shounin_shori_kengen.shounin_shori_kengen_no is '���F���������ԍ�';
comment on column shounin_shori_kengen.shounin_shori_kengen_name is '���F����������';
comment on column shounin_shori_kengen.kihon_model_cd is '��{���f���R�[�h';
comment on column shounin_shori_kengen.shounin_hissu_flg is '���F�K�{�t���O';
comment on column shounin_shori_kengen.shounin_ken_flg is '���F���t���O';
comment on column shounin_shori_kengen.henkou_flg is '�ύX�t���O';
comment on column shounin_shori_kengen.setsumei is '����';
comment on column shounin_shori_kengen.shounin_mongon is '���F����';
comment on column shounin_shori_kengen.hanrei_hyouji_cd is '�}��\���R�[�h';
comment on column shounin_shori_kengen.hyouji_jun is '�\����';
comment on column shounin_shori_kengen.touroku_user_id is '�o�^���[�U�[ID';
comment on column shounin_shori_kengen.touroku_time is '�o�^����';
comment on column shounin_shori_kengen.koushin_user_id is '�X�V���[�U�[ID';
comment on column shounin_shori_kengen.koushin_time is '�X�V����';

comment on table shozoku_bumon is '��������';
comment on column shozoku_bumon.bumon_cd is '����R�[�h';
comment on column shozoku_bumon.bumon_name is '���喼';
comment on column shozoku_bumon.oya_bumon_cd is '�e��������R�[�h';
comment on column shozoku_bumon.yuukou_kigen_from is '�L�������J�n��';
comment on column shozoku_bumon.yuukou_kigen_to is '�L�������I����';
comment on column shozoku_bumon.security_pattern is '�Z�L�����e�B�p�^�[��';
comment on column shozoku_bumon.touroku_user_id is '�o�^���[�U�[ID';
comment on column shozoku_bumon.touroku_time is '�o�^����';
comment on column shozoku_bumon.koushin_user_id is '�X�V���[�U�[ID';
comment on column shozoku_bumon.koushin_time is '�X�V����';

comment on table shozoku_bumon_shiwake_pattern_master is '��������d��p�^�[���}�X�^�[';
comment on column shozoku_bumon_shiwake_pattern_master.bumon_cd is '����R�[�h';
comment on column shozoku_bumon_shiwake_pattern_master.denpyou_kbn is '�`�[�敪';
comment on column shozoku_bumon_shiwake_pattern_master.shiwake_edano is '�d��}�ԍ�';
comment on column shozoku_bumon_shiwake_pattern_master.touroku_user_id is '�o�^���[�U�[ID';
comment on column shozoku_bumon_shiwake_pattern_master.touroku_time is '�o�^����';
comment on column shozoku_bumon_shiwake_pattern_master.koushin_user_id is '�X�V���[�U�[ID';
comment on column shozoku_bumon_shiwake_pattern_master.koushin_time is '�X�V����';

comment on table shozoku_bumon_wariate is '�������劄�蓖��';
comment on column shozoku_bumon_wariate.bumon_cd is '����R�[�h';
comment on column shozoku_bumon_wariate.bumon_role_id is '���働�[��ID';
comment on column shozoku_bumon_wariate.user_id is '���[�U�[ID';
comment on column shozoku_bumon_wariate.daihyou_futan_bumon_cd is '��\���S����R�[�h';
comment on column shozoku_bumon_wariate.yuukou_kigen_from is '�L�������J�n��';
comment on column shozoku_bumon_wariate.yuukou_kigen_to is '�L�������I����';
comment on column shozoku_bumon_wariate.hyouji_jun is '�\����';
comment on column shozoku_bumon_wariate.touroku_user_id is '�o�^���[�U�[ID';
comment on column shozoku_bumon_wariate.touroku_time is '�o�^����';
comment on column shozoku_bumon_wariate.koushin_user_id is '�X�V���[�U�[ID';
comment on column shozoku_bumon_wariate.koushin_time is '�X�V����';

comment on table shukujitsu_master is '�j���}�X�^�[';
comment on column shukujitsu_master.shukujitsu is '�j��';
comment on column shukujitsu_master.shukujitsu_name is '�j����';

comment on table syuukei_bumon is '�W�v����}�X�^�[';
comment on column syuukei_bumon.syuukei_bumon_cd is '�W�v����R�[�h';
comment on column syuukei_bumon.syuukei_bumon_name is '�W�v���喼';
comment on column syuukei_bumon.kessanki_bangou is '���Z���ԍ�';
comment on column syuukei_bumon.shaukei_bumon_flg is '�W�v����t���O';

comment on table teiki_jouhou is '��������';
comment on column teiki_jouhou.user_id is '���[�U�[ID';
comment on column teiki_jouhou.shiyou_kaishibi is '�g�p�J�n��';
comment on column teiki_jouhou.shiyou_shuuryoubi is '�g�p�I����';
comment on column teiki_jouhou.intra_teiki_kukan is '�C���g���Œ�����';
comment on column teiki_jouhou.intra_restoreroute is '�C���g���ŕ������t������o�H������';
comment on column teiki_jouhou.web_teiki_kukan is '�����ԏ��';
comment on column teiki_jouhou.web_teiki_serialize_data is '�����ԃV���A���C�Y�f�[�^';
comment on column teiki_jouhou.touroku_user_id is '�o�^���[�U�[ID';
comment on column teiki_jouhou.touroku_time is '�o�^����';
comment on column teiki_jouhou.koushin_user_id is '�X�V���[�U�[ID';
comment on column teiki_jouhou.koushin_time is '�X�V����';

comment on table temp_csvmake is 'CSV�쐬�p�ꎞ�e�[�u��';
comment on column temp_csvmake.row_num is '�s�ԍ�';
comment on column temp_csvmake.data_rowbinary is '�s�o�C�i���f�[�^';

comment on table tenpu_denpyou_jyogai is '�Y�t�`�[���O';
comment on column tenpu_denpyou_jyogai.denpyou_id is '�`�[ID';

comment on table tenpu_file is '�Y�t�t�@�C��';
comment on column tenpu_file.denpyou_id is '�`�[ID';
comment on column tenpu_file.edano is '�}�ԍ�';
comment on column tenpu_file.file_name is '�t�@�C����';
comment on column tenpu_file.file_size is '�t�@�C���T�C�Y';
comment on column tenpu_file.content_type is '�R���e���c�^�C�v';
comment on column tenpu_file.binary_data is '�o�C�i���[�f�[�^';
comment on column tenpu_file.touroku_user_id is '�o�^���[�U�[ID';
comment on column tenpu_file.touroku_time is '�o�^����';
comment on column tenpu_file.koushin_user_id is '�X�V���[�U�[ID';
comment on column tenpu_file.koushin_time is '�X�V����';

comment on table torihikisaki is '�����';
comment on column torihikisaki.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki.yuubin_bangou is '�X�֔ԍ�';
comment on column torihikisaki.juusho1 is '�Z���P';
comment on column torihikisaki.juusho2 is '�Z���Q';
comment on column torihikisaki.telno is '�d�b�ԍ�';
comment on column torihikisaki.faxno is 'FAX�ԍ�';
comment on column torihikisaki.kouza_meiginin is '�������`�l';
comment on column torihikisaki.kouza_meiginin_furigana is '�������`�l�ӂ艼��';
comment on column torihikisaki.shiharai_shubetsu is '�x�����';
comment on column torihikisaki.yokin_shubetsu is '�a�����';
comment on column torihikisaki.kouza_bangou is '�����ԍ�';
comment on column torihikisaki.tesuuryou_futan_kbn is '�萔�����S�敪';
comment on column torihikisaki.furikomi_kbn is '�U���敪';
comment on column torihikisaki.furikomi_ginkou_cd is '�U����s�R�[�h';
comment on column torihikisaki.furikomi_ginkou_shiten_cd is '�U����s�x�X�R�[�h';
comment on column torihikisaki.shiharaibi is '�x����';
comment on column torihikisaki.shiharai_kijitsu is '�x������';
comment on column torihikisaki.yakujou_kingaku is '�����z';
comment on column torihikisaki.torihikisaki_name_hankana is '����於�i���p�J�i�j';
comment on column torihikisaki.sbusyo is '����敔��';
comment on column torihikisaki.stanto is '�����S����';
comment on column torihikisaki.keicd is '�h��';
comment on column torihikisaki.nayose is '���񂹃t���O';
comment on column torihikisaki.f_setuin is '�߈���s�t���O';
comment on column torihikisaki.stan is '��S����';
comment on column torihikisaki.sbcod is '����R�[�h';
comment on column torihikisaki.skicd is '�ȖڃR�[�h';
comment on column torihikisaki.f_soufu is '���t�ē�';
comment on column torihikisaki.annai is '�ē���';
comment on column torihikisaki.tsokbn is '�������S�敪';
comment on column torihikisaki.f_shitu is '�x���ʒm';
comment on column torihikisaki.cdm2 is '�d����ԍ�';
comment on column torihikisaki.dm1 is '�⏕�R�[�h�P';
comment on column torihikisaki.dm2 is '�⏕�R�[�h�Q';
comment on column torihikisaki.dm3 is '�⏕�R�[�h�R';
comment on column torihikisaki.gendo is '���S���x�z';

comment on table torihikisaki_furikomisaki is '�����U����';
comment on column torihikisaki_furikomisaki.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_furikomisaki.ginkou_id is '��sID';
comment on column torihikisaki_furikomisaki.kinyuukikan_cd is '���Z�@�փR�[�h';
comment on column torihikisaki_furikomisaki.kinyuukikan_shiten_cd is '���Z�@�֎x�X�R�[�h';
comment on column torihikisaki_furikomisaki.yokin_shubetsu is '�a�����';
comment on column torihikisaki_furikomisaki.kouza_bangou is '�����ԍ�';
comment on column torihikisaki_furikomisaki.kouza_meiginin is '�������`�l';

comment on table torihikisaki_hojo is '�����⏕';
comment on column torihikisaki_hojo.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_hojo.dm1 is '�⏕�R�[�h1';
comment on column torihikisaki_hojo.dm2 is '�⏕�R�[�h2';
comment on column torihikisaki_hojo.dm3 is '�⏕�R�[�h3';
comment on column torihikisaki_hojo.stflg is '�����~';

comment on table torihikisaki_kamoku_zandaka is '�����Ȗڎc��';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_kamoku_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column torihikisaki_kamoku_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column torihikisaki_kamoku_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_name_seishiki is '����於�i�����j';
comment on column torihikisaki_kamoku_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column torihikisaki_kamoku_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column torihikisaki_kamoku_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column torihikisaki_kamoku_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column torihikisaki_kamoku_zandaka.kishu_zandaka is '����c��';

comment on table torihikisaki_master is '�����}�X�^�[';
comment on column torihikisaki_master.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_master.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column torihikisaki_master.torihikisaki_name_seishiki is '����於�i�����j';
comment on column torihikisaki_master.torihikisaki_name_hankana is '����於�i���p�J�i�j';
comment on column torihikisaki_master.nyuryoku_from_date is '���͋��J�n�N����';
comment on column torihikisaki_master.nyuryoku_to_date is '���͋��I���N����';
comment on column torihikisaki_master.tekikaku_no is '�K�i���������s���Ǝғo�^�ԍ�';
comment on column torihikisaki_master.menzei_jigyousha_flg is '�ƐŎ��Ǝғ��t���O';

comment on table torihikisaki_shiharaihouhou is '�����x�����@';
comment on column torihikisaki_shiharaihouhou.torihikisaki_cd is '�����R�[�h';
comment on column torihikisaki_shiharaihouhou.shiharai_id is '�x��ID';
comment on column torihikisaki_shiharaihouhou.shimebi is '����';
comment on column torihikisaki_shiharaihouhou.shiharaibi_mm is '�x�����iMM�j';
comment on column torihikisaki_shiharaihouhou.shiharaibi_dd is '�x�����iDD�j';
comment on column torihikisaki_shiharaihouhou.shiharai_kbn is '�x���敪';
comment on column torihikisaki_shiharaihouhou.shiharaikijitsu_mm is '�x�������iMM�j';
comment on column torihikisaki_shiharaihouhou.shiharaikijitsu_dd is '�x�������iDD�j';
comment on column torihikisaki_shiharaihouhou.harai_h is '�x���␳(�x����)';
comment on column torihikisaki_shiharaihouhou.kijitu_h is '�x���␳(�x������)';
comment on column torihikisaki_shiharaihouhou.shiharai_houhou is '�x�����@';

comment on table tsukekae is '�t��';
comment on column tsukekae.denpyou_id is '�`�[ID';
comment on column tsukekae.denpyou_date is '�`�[���t';
comment on column tsukekae.shouhyou_shorui_flg is '�؜ߏ��ރt���O';
comment on column tsukekae.zeiritsu is '�ŗ�';
comment on column tsukekae.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column tsukekae.kingaku_goukei is '���z���v';
comment on column tsukekae.hf1_cd is 'HF1�R�[�h';
comment on column tsukekae.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column tsukekae.hf2_cd is 'HF2�R�[�h';
comment on column tsukekae.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column tsukekae.hf3_cd is 'HF3�R�[�h';
comment on column tsukekae.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column tsukekae.hf4_cd is 'HF4�R�[�h';
comment on column tsukekae.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column tsukekae.hf5_cd is 'HF5�R�[�h';
comment on column tsukekae.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column tsukekae.hf6_cd is 'HF6�R�[�h';
comment on column tsukekae.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column tsukekae.hf7_cd is 'HF7�R�[�h';
comment on column tsukekae.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column tsukekae.hf8_cd is 'HF8�R�[�h';
comment on column tsukekae.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column tsukekae.hf9_cd is 'HF9�R�[�h';
comment on column tsukekae.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column tsukekae.hf10_cd is 'HF10�R�[�h';
comment on column tsukekae.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column tsukekae.hosoku is '�⑫';
comment on column tsukekae.tsukekae_kbn is '�t�֋敪';
comment on column tsukekae.moto_kamoku_cd is '�t�֌��ȖڃR�[�h';
comment on column tsukekae.moto_kamoku_name is '�t�֌��Ȗږ�';
comment on column tsukekae.moto_kamoku_edaban_cd is '�t�֌��Ȗڎ}�ԃR�[�h';
comment on column tsukekae.moto_kamoku_edaban_name is '�t�֌��Ȗڎ}�Ԗ�';
comment on column tsukekae.moto_futan_bumon_cd is '�t�֌����S����R�[�h';
comment on column tsukekae.moto_futan_bumon_name is '�t�֌����S���喼';
comment on column tsukekae.moto_torihikisaki_cd is '�t�֌������R�[�h';
comment on column tsukekae.moto_torihikisaki_name_ryakushiki is '�t�֌�����於�i�����j';
comment on column tsukekae.moto_kazei_kbn is '�t�֌��ېŋ敪';
comment on column tsukekae.moto_uf1_cd is '�t�֌�UF1�R�[�h';
comment on column tsukekae.moto_uf1_name_ryakushiki is '�t�֌�UF1���i�����j';
comment on column tsukekae.moto_uf2_cd is '�t�֌�UF2�R�[�h';
comment on column tsukekae.moto_uf2_name_ryakushiki is '�t�֌�UF2���i�����j';
comment on column tsukekae.moto_uf3_cd is '�t�֌�UF3�R�[�h';
comment on column tsukekae.moto_uf3_name_ryakushiki is '�t�֌�UF3���i�����j';
comment on column tsukekae.moto_uf4_cd is '�t�֌�UF4�R�[�h';
comment on column tsukekae.moto_uf4_name_ryakushiki is '�t�֌�UF4���i�����j';
comment on column tsukekae.moto_uf5_cd is '�t�֌�UF5�R�[�h';
comment on column tsukekae.moto_uf5_name_ryakushiki is '�t�֌�UF5���i�����j';
comment on column tsukekae.moto_uf6_cd is '�t�֌�UF6�R�[�h';
comment on column tsukekae.moto_uf6_name_ryakushiki is '�t�֌�UF6���i�����j';
comment on column tsukekae.moto_uf7_cd is '�t�֌�UF7�R�[�h';
comment on column tsukekae.moto_uf7_name_ryakushiki is '�t�֌�UF7���i�����j';
comment on column tsukekae.moto_uf8_cd is '�t�֌�UF8�R�[�h';
comment on column tsukekae.moto_uf8_name_ryakushiki is '�t�֌�UF8���i�����j';
comment on column tsukekae.moto_uf9_cd is '�t�֌�UF9�R�[�h';
comment on column tsukekae.moto_uf9_name_ryakushiki is '�t�֌�UF9���i�����j';
comment on column tsukekae.moto_uf10_cd is '�t�֌�UF10�R�[�h';
comment on column tsukekae.moto_uf10_name_ryakushiki is '�t�֌�UF10���i�����j';
comment on column tsukekae.moto_project_cd is '�t�֌��v���W�F�N�g�R�[�h';
comment on column tsukekae.moto_project_name is '�t�֌��v���W�F�N�g��';
comment on column tsukekae.moto_segment_cd is '�t�֌��Z�O�����g�R�[�h';
comment on column tsukekae.moto_segment_name_ryakushiki is '�t�֌��Z�O�����g���i�����j';
comment on column tsukekae.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsukekae.touroku_time is '�o�^����';
comment on column tsukekae.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsukekae.koushin_time is '�X�V����';
comment on column tsukekae.moto_jigyousha_kbn is '�t�֌����Ǝҋ敪';
comment on column tsukekae.moto_bunri_kbn is '�t�֌������敪';
comment on column tsukekae.moto_shiire_kbn is '�t�֌��d���敪';
comment on column tsukekae.moto_zeigaku_houshiki is '�t�֌��Ŋz�v�Z����';
comment on column tsukekae.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table tsukekae_meisai is '�t�֖���';
comment on column tsukekae_meisai.denpyou_id is '�`�[ID';
comment on column tsukekae_meisai.denpyou_edano is '�`�[�}�ԍ�';
comment on column tsukekae_meisai.tekiyou is '�E�v';
comment on column tsukekae_meisai.kingaku is '���z';
comment on column tsukekae_meisai.hontai_kingaku is '�{�̋��z';
comment on column tsukekae_meisai.shouhizeigaku is '����Ŋz';
comment on column tsukekae_meisai.bikou is '���l�i��v�`�[�j';
comment on column tsukekae_meisai.saki_kamoku_cd is '�t�֐�ȖڃR�[�h';
comment on column tsukekae_meisai.saki_kamoku_name is '�t�֐�Ȗږ�';
comment on column tsukekae_meisai.saki_kamoku_edaban_cd is '�t�֐�Ȗڎ}�ԃR�[�h';
comment on column tsukekae_meisai.saki_kamoku_edaban_name is '�t�֐�Ȗڎ}�Ԗ�';
comment on column tsukekae_meisai.saki_futan_bumon_cd is '�t�֐敉�S����R�[�h';
comment on column tsukekae_meisai.saki_futan_bumon_name is '�t�֐敉�S���喼';
comment on column tsukekae_meisai.saki_torihikisaki_cd is '�t�֐�����R�[�h';
comment on column tsukekae_meisai.saki_torihikisaki_name_ryakushiki is '�t�֐����於�i�����j';
comment on column tsukekae_meisai.saki_kazei_kbn is '�t�֐�ېŋ敪';
comment on column tsukekae_meisai.saki_uf1_cd is '�t�֐�UF1�R�[�h';
comment on column tsukekae_meisai.saki_uf1_name_ryakushiki is '�t�֐�UF1���i�����j';
comment on column tsukekae_meisai.saki_uf2_cd is '�t�֐�UF2�R�[�h';
comment on column tsukekae_meisai.saki_uf2_name_ryakushiki is '�t�֐�UF2���i�����j';
comment on column tsukekae_meisai.saki_uf3_cd is '�t�֐�UF3�R�[�h';
comment on column tsukekae_meisai.saki_uf3_name_ryakushiki is '�t�֐�UF3���i�����j';
comment on column tsukekae_meisai.saki_uf4_cd is '�t�֐�UF4�R�[�h';
comment on column tsukekae_meisai.saki_uf4_name_ryakushiki is '�t�֐�UF4���i�����j';
comment on column tsukekae_meisai.saki_uf5_cd is '�t�֐�UF5�R�[�h';
comment on column tsukekae_meisai.saki_uf5_name_ryakushiki is '�t�֐�UF5���i�����j';
comment on column tsukekae_meisai.saki_uf6_cd is '�t�֐�UF6�R�[�h';
comment on column tsukekae_meisai.saki_uf6_name_ryakushiki is '�t�֐�UF6���i�����j';
comment on column tsukekae_meisai.saki_uf7_cd is '�t�֐�UF7�R�[�h';
comment on column tsukekae_meisai.saki_uf7_name_ryakushiki is '�t�֐�UF7���i�����j';
comment on column tsukekae_meisai.saki_uf8_cd is '�t�֐�UF8�R�[�h';
comment on column tsukekae_meisai.saki_uf8_name_ryakushiki is '�t�֐�UF8���i�����j';
comment on column tsukekae_meisai.saki_uf9_cd is '�t�֐�UF9�R�[�h';
comment on column tsukekae_meisai.saki_uf9_name_ryakushiki is '�t�֐�UF9���i�����j';
comment on column tsukekae_meisai.saki_uf10_cd is '�t�֐�UF10�R�[�h';
comment on column tsukekae_meisai.saki_uf10_name_ryakushiki is '�t�֐�UF10���i�����j';
comment on column tsukekae_meisai.saki_project_cd is '�t�֐�v���W�F�N�g�R�[�h';
comment on column tsukekae_meisai.saki_project_name is '�t�֐�v���W�F�N�g��';
comment on column tsukekae_meisai.saki_segment_cd is '�t�֐�Z�O�����g�R�[�h';
comment on column tsukekae_meisai.saki_segment_name_ryakushiki is '�t�֐�Z�O�����g���i�����j';
comment on column tsukekae_meisai.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsukekae_meisai.touroku_time is '�o�^����';
comment on column tsukekae_meisai.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsukekae_meisai.koushin_time is '�X�V����';
comment on column tsukekae_meisai.saki_jigyousha_kbn is '�t�֐掖�Ǝҋ敪';
comment on column tsukekae_meisai.saki_bunri_kbn is '�t�֐敪���敪';
comment on column tsukekae_meisai.saki_shiire_kbn is '�t�֐�d���敪';
comment on column tsukekae_meisai.saki_zeigaku_houshiki is '�t�֐�Ŋz�v�Z����';

comment on table tsuuchi is '�ʒm';
comment on column tsuuchi.serial_no is '�V���A���ԍ�';
comment on column tsuuchi.user_id is '���[�U�[ID';
comment on column tsuuchi.denpyou_id is '�`�[ID';
comment on column tsuuchi.edano is '�}�ԍ�';
comment on column tsuuchi.kidoku_flg is '���ǃt���O';
comment on column tsuuchi.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsuuchi.touroku_time is '�o�^����';
comment on column tsuuchi.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsuuchi.koushin_time is '�X�V����';

comment on table tsuukinteiki is '�ʋΒ��';
comment on column tsuukinteiki.denpyou_id is '�`�[ID';
comment on column tsuukinteiki.shiyou_kikan_kbn is '�g�p���ԋ敪';
comment on column tsuukinteiki.shiyou_kaishibi is '�g�p�J�n��';
comment on column tsuukinteiki.shiyou_shuuryoubi is '�g�p�I����';
comment on column tsuukinteiki.jyousha_kukan is '��ԋ��';
comment on column tsuukinteiki.teiki_serialize_data is '�����ԃV���A���C�Y�f�[�^';
comment on column tsuukinteiki.shiharaibi is '�x����';
comment on column tsuukinteiki.tekiyou is '�E�v';
comment on column tsuukinteiki.zeiritsu is '�ŗ�';
comment on column tsuukinteiki.keigen_zeiritsu_kbn is '�y���ŗ��敪';
comment on column tsuukinteiki.kingaku is '���z';
comment on column tsuukinteiki.tenyuuryoku_flg is '����̓t���O';
comment on column tsuukinteiki.hf1_cd is 'HF1�R�[�h';
comment on column tsuukinteiki.hf1_name_ryakushiki is 'HF1���i�����j';
comment on column tsuukinteiki.hf2_cd is 'HF2�R�[�h';
comment on column tsuukinteiki.hf2_name_ryakushiki is 'HF2���i�����j';
comment on column tsuukinteiki.hf3_cd is 'HF3�R�[�h';
comment on column tsuukinteiki.hf3_name_ryakushiki is 'HF3���i�����j';
comment on column tsuukinteiki.hf4_cd is 'HF4�R�[�h';
comment on column tsuukinteiki.hf4_name_ryakushiki is 'HF4���i�����j';
comment on column tsuukinteiki.hf5_cd is 'HF5�R�[�h';
comment on column tsuukinteiki.hf5_name_ryakushiki is 'HF5���i�����j';
comment on column tsuukinteiki.hf6_cd is 'HF6�R�[�h';
comment on column tsuukinteiki.hf6_name_ryakushiki is 'HF6���i�����j';
comment on column tsuukinteiki.hf7_cd is 'HF7�R�[�h';
comment on column tsuukinteiki.hf7_name_ryakushiki is 'HF7���i�����j';
comment on column tsuukinteiki.hf8_cd is 'HF8�R�[�h';
comment on column tsuukinteiki.hf8_name_ryakushiki is 'HF8���i�����j';
comment on column tsuukinteiki.hf9_cd is 'HF9�R�[�h';
comment on column tsuukinteiki.hf9_name_ryakushiki is 'HF9���i�����j';
comment on column tsuukinteiki.hf10_cd is 'HF10�R�[�h';
comment on column tsuukinteiki.hf10_name_ryakushiki is 'HF10���i�����j';
comment on column tsuukinteiki.shiwake_edano is '�d��}�ԍ�';
comment on column tsuukinteiki.torihiki_name is '�����';
comment on column tsuukinteiki.kari_futan_bumon_cd is '�ؕ����S����R�[�h';
comment on column tsuukinteiki.kari_futan_bumon_name is '�ؕ����S���喼';
comment on column tsuukinteiki.torihikisaki_cd is '�����R�[�h';
comment on column tsuukinteiki.torihikisaki_name_ryakushiki is '����於�i�����j';
comment on column tsuukinteiki.kari_kamoku_cd is '�ؕ��ȖڃR�[�h';
comment on column tsuukinteiki.kari_kamoku_name is '�ؕ��Ȗږ�';
comment on column tsuukinteiki.kari_kamoku_edaban_cd is '�ؕ��Ȗڎ}�ԃR�[�h';
comment on column tsuukinteiki.kari_kamoku_edaban_name is '�ؕ��Ȗڎ}�Ԗ�';
comment on column tsuukinteiki.kari_kazei_kbn is '�ؕ��ېŋ敪';
comment on column tsuukinteiki.kashi_futan_bumon_cd is '�ݕ����S����R�[�h';
comment on column tsuukinteiki.kashi_futan_bumon_name is '�ݕ����S���喼';
comment on column tsuukinteiki.kashi_kamoku_cd is '�ݕ��ȖڃR�[�h';
comment on column tsuukinteiki.kashi_kamoku_name is '�ݕ��Ȗږ�';
comment on column tsuukinteiki.kashi_kamoku_edaban_cd is '�ݕ��Ȗڎ}�ԃR�[�h';
comment on column tsuukinteiki.kashi_kamoku_edaban_name is '�ݕ��Ȗڎ}�Ԗ�';
comment on column tsuukinteiki.kashi_kazei_kbn is '�ݕ��ېŋ敪';
comment on column tsuukinteiki.uf1_cd is 'UF1�R�[�h';
comment on column tsuukinteiki.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column tsuukinteiki.uf2_cd is 'UF2�R�[�h';
comment on column tsuukinteiki.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column tsuukinteiki.uf3_cd is 'UF3�R�[�h';
comment on column tsuukinteiki.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column tsuukinteiki.uf4_cd is 'UF4�R�[�h';
comment on column tsuukinteiki.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column tsuukinteiki.uf5_cd is 'UF5�R�[�h';
comment on column tsuukinteiki.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column tsuukinteiki.uf6_cd is 'UF6�R�[�h';
comment on column tsuukinteiki.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column tsuukinteiki.uf7_cd is 'UF7�R�[�h';
comment on column tsuukinteiki.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column tsuukinteiki.uf8_cd is 'UF8�R�[�h';
comment on column tsuukinteiki.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column tsuukinteiki.uf9_cd is 'UF9�R�[�h';
comment on column tsuukinteiki.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column tsuukinteiki.uf10_cd is 'UF10�R�[�h';
comment on column tsuukinteiki.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column tsuukinteiki.project_cd is '�v���W�F�N�g�R�[�h';
comment on column tsuukinteiki.project_name is '�v���W�F�N�g��';
comment on column tsuukinteiki.segment_cd is '�Z�O�����g�R�[�h';
comment on column tsuukinteiki.segment_name_ryakushiki is '�Z�O�����g���i�����j';
comment on column tsuukinteiki.tekiyou_cd is '�E�v�R�[�h';
comment on column tsuukinteiki.touroku_user_id is '�o�^���[�U�[ID';
comment on column tsuukinteiki.touroku_time is '�o�^����';
comment on column tsuukinteiki.koushin_user_id is '�X�V���[�U�[ID';
comment on column tsuukinteiki.koushin_time is '�X�V����';
comment on column tsuukinteiki.zeinuki_kingaku is '�Ŕ����z';
comment on column tsuukinteiki.shouhizeigaku is '����Ŋz';
comment on column tsuukinteiki.shiharaisaki_name is '�x���於';
comment on column tsuukinteiki.jigyousha_kbn is '���Ǝҋ敪';
comment on column tsuukinteiki.bunri_kbn is '�����敪';
comment on column tsuukinteiki.kari_shiire_kbn is '�ؕ��d���敪';
comment on column tsuukinteiki.kashi_shiire_kbn is '�ݕ��d���敪';
comment on column tsuukinteiki.invoice_denpyou is '�C���{�C�X�Ή��`�[';

comment on table uf_kotei1_ichiran is '���j�o�[�T���t�B�[���h�P�ꗗ�i�Œ�l�j';
comment on column uf_kotei1_ichiran.uf_kotei1_cd is 'UF1�R�[�h';
comment on column uf_kotei1_ichiran.uf_kotei1_name_ryakushiki is 'UF1���i�����j';
comment on column uf_kotei1_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei1_zandaka is '���j�o�[�T���t�B�[���h�P�c���i�Œ�l�j';
comment on column uf_kotei1_zandaka.uf_kotei1_cd is 'UF1�R�[�h';
comment on column uf_kotei1_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei1_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei1_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei1_zandaka.uf_kotei1_name_ryakushiki is 'UF1���i�����j';
comment on column uf_kotei1_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei1_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei1_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei1_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei1_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei10_ichiran is '���j�o�[�T���t�B�[���h�P�O�ꗗ�i�Œ�l�j';
comment on column uf_kotei10_ichiran.uf_kotei10_cd is 'UF10�R�[�h';
comment on column uf_kotei10_ichiran.uf_kotei10_name_ryakushiki is 'UF10���i�����j';
comment on column uf_kotei10_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei10_zandaka is '���j�o�[�T���t�B�[���h�P�O�c���i�Œ�l�j';
comment on column uf_kotei10_zandaka.uf_kotei10_cd is 'UF10�R�[�h';
comment on column uf_kotei10_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei10_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei10_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei10_zandaka.uf_kotei10_name_ryakushiki is 'UF10���i�����j';
comment on column uf_kotei10_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei10_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei10_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei10_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei10_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei2_ichiran is '���j�o�[�T���t�B�[���h�Q�ꗗ�i�Œ�l�j';
comment on column uf_kotei2_ichiran.uf_kotei2_cd is 'UF2�R�[�h';
comment on column uf_kotei2_ichiran.uf_kotei2_name_ryakushiki is 'UF2���i�����j';
comment on column uf_kotei2_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei2_zandaka is '���j�o�[�T���t�B�[���h�Q�c���i�Œ�l�j';
comment on column uf_kotei2_zandaka.uf_kotei2_cd is 'UF2�R�[�h';
comment on column uf_kotei2_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei2_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei2_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei2_zandaka.uf_kotei2_name_ryakushiki is 'UF2���i�����j';
comment on column uf_kotei2_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei2_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei2_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei2_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei2_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei3_ichiran is '���j�o�[�T���t�B�[���h�R�ꗗ�i�Œ�l�j';
comment on column uf_kotei3_ichiran.uf_kotei3_cd is 'UF3�R�[�h';
comment on column uf_kotei3_ichiran.uf_kotei3_name_ryakushiki is 'UF3���i�����j';
comment on column uf_kotei3_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei3_zandaka is '���j�o�[�T���t�B�[���h�R�c���i�Œ�l�j';
comment on column uf_kotei3_zandaka.uf_kotei3_cd is 'UF3�R�[�h';
comment on column uf_kotei3_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei3_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei3_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei3_zandaka.uf_kotei3_name_ryakushiki is 'UF3���i�����j';
comment on column uf_kotei3_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei3_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei3_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei3_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei3_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei4_ichiran is '���j�o�[�T���t�B�[���h�S�ꗗ�i�Œ�l�j';
comment on column uf_kotei4_ichiran.uf_kotei4_cd is 'UF4�R�[�h';
comment on column uf_kotei4_ichiran.uf_kotei4_name_ryakushiki is 'UF4���i�����j';
comment on column uf_kotei4_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei4_zandaka is '���j�o�[�T���t�B�[���h�S�c���i�Œ�l�j';
comment on column uf_kotei4_zandaka.uf_kotei4_cd is 'UF4�R�[�h';
comment on column uf_kotei4_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei4_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei4_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei4_zandaka.uf_kotei4_name_ryakushiki is 'UF4���i�����j';
comment on column uf_kotei4_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei4_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei4_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei4_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei4_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei5_ichiran is '���j�o�[�T���t�B�[���h�T�ꗗ�i�Œ�l�j';
comment on column uf_kotei5_ichiran.uf_kotei5_cd is 'UF5�R�[�h';
comment on column uf_kotei5_ichiran.uf_kotei5_name_ryakushiki is 'UF5���i�����j';
comment on column uf_kotei5_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei5_zandaka is '���j�o�[�T���t�B�[���h�T�c���i�Œ�l�j';
comment on column uf_kotei5_zandaka.uf_kotei5_cd is 'UF5�R�[�h';
comment on column uf_kotei5_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei5_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei5_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei5_zandaka.uf_kotei5_name_ryakushiki is 'UF5���i�����j';
comment on column uf_kotei5_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei5_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei5_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei5_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei5_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei6_ichiran is '���j�o�[�T���t�B�[���h�U�ꗗ�i�Œ�l�j';
comment on column uf_kotei6_ichiran.uf_kotei6_cd is 'UF6�R�[�h';
comment on column uf_kotei6_ichiran.uf_kotei6_name_ryakushiki is 'UF6���i�����j';
comment on column uf_kotei6_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei6_zandaka is '���j�o�[�T���t�B�[���h�U�c���i�Œ�l�j';
comment on column uf_kotei6_zandaka.uf_kotei6_cd is 'UF6�R�[�h';
comment on column uf_kotei6_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei6_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei6_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei6_zandaka.uf_kotei6_name_ryakushiki is 'UF6���i�����j';
comment on column uf_kotei6_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei6_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei6_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei6_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei6_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei7_ichiran is '���j�o�[�T���t�B�[���h�V�ꗗ�i�Œ�l�j';
comment on column uf_kotei7_ichiran.uf_kotei7_cd is 'UF7�R�[�h';
comment on column uf_kotei7_ichiran.uf_kotei7_name_ryakushiki is 'UF7���i�����j';
comment on column uf_kotei7_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei7_zandaka is '���j�o�[�T���t�B�[���h�V�c���i�Œ�l�j';
comment on column uf_kotei7_zandaka.uf_kotei7_cd is 'UF7�R�[�h';
comment on column uf_kotei7_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei7_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei7_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei7_zandaka.uf_kotei7_name_ryakushiki is 'UF7���i�����j';
comment on column uf_kotei7_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei7_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei7_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei7_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei7_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei8_ichiran is '���j�o�[�T���t�B�[���h�W�ꗗ�i�Œ�l�j';
comment on column uf_kotei8_ichiran.uf_kotei8_cd is 'UF8�R�[�h';
comment on column uf_kotei8_ichiran.uf_kotei8_name_ryakushiki is 'UF8���i�����j';
comment on column uf_kotei8_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei8_zandaka is '���j�o�[�T���t�B�[���h�W�c���i�Œ�l�j';
comment on column uf_kotei8_zandaka.uf_kotei8_cd is 'UF8�R�[�h';
comment on column uf_kotei8_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei8_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei8_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei8_zandaka.uf_kotei8_name_ryakushiki is 'UF8���i�����j';
comment on column uf_kotei8_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei8_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei8_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei8_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei8_zandaka.kishu_zandaka is '����c��';

comment on table uf_kotei9_ichiran is '���j�o�[�T���t�B�[���h�X�ꗗ�i�Œ�l�j';
comment on column uf_kotei9_ichiran.uf_kotei9_cd is 'UF9�R�[�h';
comment on column uf_kotei9_ichiran.uf_kotei9_name_ryakushiki is 'UF9���i�����j';
comment on column uf_kotei9_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf_kotei9_zandaka is '���j�o�[�T���t�B�[���h�X�c���i�Œ�l�j';
comment on column uf_kotei9_zandaka.uf_kotei9_cd is 'UF9�R�[�h';
comment on column uf_kotei9_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf_kotei9_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf_kotei9_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf_kotei9_zandaka.uf_kotei9_name_ryakushiki is 'UF9���i�����j';
comment on column uf_kotei9_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf_kotei9_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf_kotei9_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf_kotei9_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf_kotei9_zandaka.kishu_zandaka is '����c��';

comment on table uf1_ichiran is '���j�o�[�T���t�B�[���h�P�ꗗ';
comment on column uf1_ichiran.uf1_cd is 'UF1�R�[�h';
comment on column uf1_ichiran.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column uf1_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf1_zandaka is '���j�o�[�T���t�B�[���h�P�c��';
comment on column uf1_zandaka.uf1_cd is 'UF1�R�[�h';
comment on column uf1_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf1_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf1_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf1_zandaka.uf1_name_ryakushiki is 'UF1���i�����j';
comment on column uf1_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf1_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf1_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf1_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf1_zandaka.kishu_zandaka is '����c��';

comment on table uf10_ichiran is '���j�o�[�T���t�B�[���h�P�O�ꗗ';
comment on column uf10_ichiran.uf10_cd is 'UF10�R�[�h';
comment on column uf10_ichiran.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column uf10_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf10_zandaka is '���j�o�[�T���t�B�[���h�P�O�c��';
comment on column uf10_zandaka.uf10_cd is 'UF10�R�[�h';
comment on column uf10_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf10_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf10_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf10_zandaka.uf10_name_ryakushiki is 'UF10���i�����j';
comment on column uf10_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf10_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf10_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf10_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf10_zandaka.kishu_zandaka is '����c��';

comment on table uf2_ichiran is '���j�o�[�T���t�B�[���h�Q�ꗗ';
comment on column uf2_ichiran.uf2_cd is 'UF2�R�[�h';
comment on column uf2_ichiran.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column uf2_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf2_zandaka is '���j�o�[�T���t�B�[���h�Q�c��';
comment on column uf2_zandaka.uf2_cd is 'UF2�R�[�h';
comment on column uf2_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf2_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf2_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf2_zandaka.uf2_name_ryakushiki is 'UF2���i�����j';
comment on column uf2_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf2_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf2_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf2_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf2_zandaka.kishu_zandaka is '����c��';

comment on table uf3_ichiran is '���j�o�[�T���t�B�[���h�R�ꗗ';
comment on column uf3_ichiran.uf3_cd is 'UF3�R�[�h';
comment on column uf3_ichiran.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column uf3_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf3_zandaka is '���j�o�[�T���t�B�[���h�R�c��';
comment on column uf3_zandaka.uf3_cd is 'UF3�R�[�h';
comment on column uf3_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf3_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf3_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf3_zandaka.uf3_name_ryakushiki is 'UF3���i�����j';
comment on column uf3_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf3_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf3_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf3_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf3_zandaka.kishu_zandaka is '����c��';

comment on table uf4_ichiran is '���j�o�[�T���t�B�[���h�S�ꗗ';
comment on column uf4_ichiran.uf4_cd is 'UF4�R�[�h';
comment on column uf4_ichiran.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column uf4_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf4_zandaka is '���j�o�[�T���t�B�[���h�S�c��';
comment on column uf4_zandaka.uf4_cd is 'UF4�R�[�h';
comment on column uf4_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf4_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf4_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf4_zandaka.uf4_name_ryakushiki is 'UF4���i�����j';
comment on column uf4_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf4_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf4_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf4_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf4_zandaka.kishu_zandaka is '����c��';

comment on table uf5_ichiran is '���j�o�[�T���t�B�[���h�T�ꗗ';
comment on column uf5_ichiran.uf5_cd is 'UF5�R�[�h';
comment on column uf5_ichiran.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column uf5_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf5_zandaka is '���j�o�[�T���t�B�[���h�T�c��';
comment on column uf5_zandaka.uf5_cd is 'UF5�R�[�h';
comment on column uf5_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf5_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf5_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf5_zandaka.uf5_name_ryakushiki is 'UF5���i�����j';
comment on column uf5_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf5_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf5_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf5_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf5_zandaka.kishu_zandaka is '����c��';

comment on table uf6_ichiran is '���j�o�[�T���t�B�[���h�U�ꗗ';
comment on column uf6_ichiran.uf6_cd is 'UF6�R�[�h';
comment on column uf6_ichiran.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column uf6_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf6_zandaka is '���j�o�[�T���t�B�[���h�U�c��';
comment on column uf6_zandaka.uf6_cd is 'UF6�R�[�h';
comment on column uf6_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf6_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf6_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf6_zandaka.uf6_name_ryakushiki is 'UF6���i�����j';
comment on column uf6_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf6_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf6_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf6_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf6_zandaka.kishu_zandaka is '����c��';

comment on table uf7_ichiran is '���j�o�[�T���t�B�[���h�V�ꗗ';
comment on column uf7_ichiran.uf7_cd is 'UF7�R�[�h';
comment on column uf7_ichiran.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column uf7_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf7_zandaka is '���j�o�[�T���t�B�[���h�V�c��';
comment on column uf7_zandaka.uf7_cd is 'UF7�R�[�h';
comment on column uf7_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf7_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf7_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf7_zandaka.uf7_name_ryakushiki is 'UF7���i�����j';
comment on column uf7_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf7_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf7_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf7_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf7_zandaka.kishu_zandaka is '����c��';

comment on table uf8_ichiran is '���j�o�[�T���t�B�[���h�W�ꗗ';
comment on column uf8_ichiran.uf8_cd is 'UF8�R�[�h';
comment on column uf8_ichiran.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column uf8_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf8_zandaka is '���j�o�[�T���t�B�[���h�W�c��';
comment on column uf8_zandaka.uf8_cd is 'UF8�R�[�h';
comment on column uf8_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf8_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf8_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf8_zandaka.uf8_name_ryakushiki is 'UF8���i�����j';
comment on column uf8_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf8_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf8_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf8_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf8_zandaka.kishu_zandaka is '����c��';

comment on table uf9_ichiran is '���j�o�[�T���t�B�[���h�X�ꗗ';
comment on column uf9_ichiran.uf9_cd is 'UF9�R�[�h';
comment on column uf9_ichiran.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column uf9_ichiran.kessanki_bangou is '���Z���ԍ�';

comment on table uf9_zandaka is '���j�o�[�T���t�B�[���h�X�c��';
comment on column uf9_zandaka.uf9_cd is 'UF9�R�[�h';
comment on column uf9_zandaka.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column uf9_zandaka.kessanki_bangou is '���Z���ԍ�';
comment on column uf9_zandaka.kamoku_naibu_cd is '�Ȗړ����R�[�h';
comment on column uf9_zandaka.uf9_name_ryakushiki is 'UF9���i�����j';
comment on column uf9_zandaka.chouhyou_shaturyoku_no is '���[�o�͏��ԍ�';
comment on column uf9_zandaka.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column uf9_zandaka.kamoku_name_seishiki is '�Ȗږ��i�����j';
comment on column uf9_zandaka.taishaku_zokusei is '�ݎؑ���';
comment on column uf9_zandaka.kishu_zandaka is '����c��';

comment on table user_default_value is '���[�U�[�ʃf�t�H���g�l';
comment on column user_default_value.kbn is '�敪';
comment on column user_default_value.user_id is '���[�U�[ID';
comment on column user_default_value.default_value is '�f�t�H���g�l';

comment on table user_info is '���[�U�[���';
comment on column user_info.user_id is '���[�U�[ID';
comment on column user_info.shain_no is '�Ј��ԍ�';
comment on column user_info.user_sei is '���[�U�[��';
comment on column user_info.user_mei is '���[�U�[��';
comment on column user_info.mail_address is '���[���A�h���X';
comment on column user_info.yuukou_kigen_from is '�L�������J�n��';
comment on column user_info.yuukou_kigen_to is '�L�������I����';
comment on column user_info.touroku_user_id is '�o�^���[�U�[ID';
comment on column user_info.touroku_time is '�o�^����';
comment on column user_info.koushin_user_id is '�X�V���[�U�[ID';
comment on column user_info.koushin_time is '�X�V����';
comment on column user_info.pass_koushin_date is '�p�X���[�h�ύX��';
comment on column user_info.pass_failure_count is '�p�X���[�h����';
comment on column user_info.pass_failure_time is '�p�X���[�h��莞��';
comment on column user_info.tmp_lock_flg is '�A�J�E���g�ꎞ���b�N�t���O';
comment on column user_info.tmp_lock_time is '�A�J�E���g�ꎞ���b�N����';
comment on column user_info.lock_flg is '�A�J�E���g���b�N�t���O';
comment on column user_info.lock_time is '�A�J�E���g���b�N����';
comment on column user_info.dairikihyou_flg is '�㗝�N�[�\�t���O';
comment on column user_info.houjin_card_riyou_flag is '�@�l�J�[�h���p';
comment on column user_info.houjin_card_shikibetsuyou_num is '�@�l�J�[�h���ʗp�ԍ�';
comment on column user_info.security_pattern is '�Z�L�����e�B�p�^�[��';
comment on column user_info.security_wfonly_flg is '�Z�L�����e�B���[�N�t���[����t���O';
comment on column user_info.shounin_route_henkou_level is '���F���[�g�ύX�������x��';
comment on column user_info.maruhi_kengen_flg is '�}����ݒ茠��';
comment on column user_info.maruhi_kaijyo_flg is '�}�����������';
comment on column user_info.zaimu_kyoten_nyuryoku_only_flg is '���_���͂̂ݎg�p�t���O';

comment on table version is '���@�[�W����';
comment on column version.version is '���@�[�W����';

comment on table yosan_kiankingaku_check is '�\�Z�E�N�ċ��z�`�F�b�N';
comment on column yosan_kiankingaku_check.denpyou_id is '�`�[ID';
comment on column yosan_kiankingaku_check.syuukei_bumon_cd is '�W�v����R�[�h';
comment on column yosan_kiankingaku_check.kamoku_gaibu_cd is '�ȖڊO���R�[�h';
comment on column yosan_kiankingaku_check.kamoku_edaban_cd is '�Ȗڎ}�ԃR�[�h';
comment on column yosan_kiankingaku_check.futan_bumon_cd is '���S����R�[�h';
comment on column yosan_kiankingaku_check.syuukei_bumon_name is '�W�v���喼';
comment on column yosan_kiankingaku_check.kamoku_name_ryakushiki is '�Ȗږ��i�����j';
comment on column yosan_kiankingaku_check.edaban_name is '�}�Ԗ�';
comment on column yosan_kiankingaku_check.futan_bumon_name is '���S���喼';
comment on column yosan_kiankingaku_check.kijun_kingaku is '����z';
comment on column yosan_kiankingaku_check.jissekigaku is '���ъz';
comment on column yosan_kiankingaku_check.shinsei_kingaku is '�\�����z';

comment on table yosan_kiankingaku_check_comment is '�\�Z�E�N�ċ��z�`�F�b�N�R�����g';
comment on column yosan_kiankingaku_check_comment.denpyou_id is '�`�[ID';
comment on column yosan_kiankingaku_check_comment.comment is '�R�����g';

comment on table yosan_shikkou_shori_nengetsu is '�\�Z���s�����N��';
comment on column yosan_shikkou_shori_nengetsu.from_nengetsu is '�J�n�N��';
comment on column yosan_shikkou_shori_nengetsu.to_nengetsu is '�I���N��';

comment on table invoice_start is '�C���{�C�X�J�n';
comment on column invoice_start.invoice_flg is '�C���{�C�X�J�n�t���O';
comment on column invoice_start.touroku_user_id is '�o�^���[�U�[ID';
comment on column invoice_start.touroku_time is '�o�^����';

-- e���������̏����l�}��
INSERT INTO ebunsho_kensaku VALUES (0,4,0);
INSERT INTO ebunsho_kensaku VALUES (0,5,0);
INSERT INTO ebunsho_kensaku VALUES (0,6,0);
INSERT INTO ebunsho_kensaku VALUES (1,4,0);
INSERT INTO ebunsho_kensaku VALUES (1,5,0);
INSERT INTO ebunsho_kensaku VALUES (1,6,0);
INSERT INTO ebunsho_kensaku VALUES (2,4,0);
INSERT INTO ebunsho_kensaku VALUES (2,5,0);
INSERT INTO ebunsho_kensaku VALUES (2,6,0);
INSERT INTO ebunsho_kensaku VALUES (3,4,0);
INSERT INTO ebunsho_kensaku VALUES (3,5,0);
INSERT INTO ebunsho_kensaku VALUES (3,6,0);
INSERT INTO ebunsho_kensaku VALUES (4,4,0);
INSERT INTO ebunsho_kensaku VALUES (4,5,0);
INSERT INTO ebunsho_kensaku VALUES (4,6,0);
INSERT INTO ebunsho_kensaku VALUES (5,4,0);
INSERT INTO ebunsho_kensaku VALUES (5,5,0);
INSERT INTO ebunsho_kensaku VALUES (5,6,0);
INSERT INTO ebunsho_kensaku VALUES (6,4,0);
INSERT INTO ebunsho_kensaku VALUES (6,5,0);
INSERT INTO ebunsho_kensaku VALUES (6,6,0);
INSERT INTO ebunsho_kensaku VALUES (7,4,0);
INSERT INTO ebunsho_kensaku VALUES (7,5,0);
INSERT INTO ebunsho_kensaku VALUES (7,6,0);