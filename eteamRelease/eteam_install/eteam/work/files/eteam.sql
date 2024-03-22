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

-- （期別）消費税設定テーブルの作成
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
  hassei_shubetsu character varying default '経費' not null,
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

-- インボイス開始テーブルの作成
create table if not exists invoice_start (
  invoice_flg character varying(1) not null,
  touroku_user_id character varying(30) not null,
  touroku_time timestamp(6) without time zone not null
);

comment on table ebunsho_kensaku is 'e文書検索設定';
comment on column ebunsho_kensaku.doc_type is '書類種別';
comment on column ebunsho_kensaku.item_no is '項目';
comment on column ebunsho_kensaku.nini_flg is '任意フラグ';

comment on table batch_haita_seigyo is 'バッチ排他制御';
comment on column batch_haita_seigyo.dummy is 'ダミー';

comment on table batch_log is 'バッチログ';
comment on column batch_log.serial_no is 'シリアル番号';
comment on column batch_log.start_time is '開始日時';
comment on column batch_log.end_time is '終了日時';
comment on column batch_log.batch_name is 'バッチ名';
comment on column batch_log.batch_status is 'バッチステータス';
comment on column batch_log.count_name is '件数名';
comment on column batch_log.count is '件数';
comment on column batch_log.batch_kbn is 'バッチ区分';

comment on table batch_log_invalid_denpyou_log is '不良伝票ログ';
comment on column batch_log_invalid_denpyou_log.file_name is 'ファイル名';
comment on column batch_log_invalid_denpyou_log.denpyou_start_gyou is '伝票開始行';
comment on column batch_log_invalid_denpyou_log.denpyou_end_gyou is '伝票終了行';
comment on column batch_log_invalid_denpyou_log.denpyou_date is '伝票日付';
comment on column batch_log_invalid_denpyou_log.denpyou_bangou is '伝票番号';
comment on column batch_log_invalid_denpyou_log.taishaku_sagaku_kingaku is '貸借差額金額';
comment on column batch_log_invalid_denpyou_log.gaiyou is '概要';
comment on column batch_log_invalid_denpyou_log.naiyou is '内容';

comment on table batch_log_invalid_file_log is '不良データログ';
comment on column batch_log_invalid_file_log.file_name is 'ファイル名';
comment on column batch_log_invalid_file_log.gyou_no is '行No';
comment on column batch_log_invalid_file_log.koumoku_no is '項目No';
comment on column batch_log_invalid_file_log.koumoku_name is '項目名称';
comment on column batch_log_invalid_file_log.invalid_value is '不正な値';
comment on column batch_log_invalid_file_log.error_naiyou is 'エラー内容';

comment on table batch_log_invalid_log_himoduke is 'バッチログ不良ログ紐づけ';
comment on column batch_log_invalid_log_himoduke.serial_no is 'シリアル番号';
comment on column batch_log_invalid_log_himoduke.edaban is '枝番';
comment on column batch_log_invalid_log_himoduke.file_name is 'ファイル名';

comment on table bookmark is 'ブックマーク';
comment on column bookmark.user_id is 'ユーザーID';
comment on column bookmark.denpyou_kbn is '伝票区分';
comment on column bookmark.hyouji_jun is '表示順';
comment on column bookmark.memo is 'メモ';
comment on column bookmark.touroku_user_id is '登録ユーザーID';
comment on column bookmark.touroku_time is '登録日時';
comment on column bookmark.koushin_user_id is '更新ユーザーID';
comment on column bookmark.koushin_time is '更新日時';

comment on table bumon_kamoku_edaban_yosan is '負担部門科目枝番予算';
comment on column bumon_kamoku_edaban_yosan.futan_bumon_cd is '負担部門コード';
comment on column bumon_kamoku_edaban_yosan.kamoku_gaibu_cd is '科目外部コード';
comment on column bumon_kamoku_edaban_yosan.kessanki_bangou is '決算期番号';
comment on column bumon_kamoku_edaban_yosan.kamoku_naibu_cd is '科目内部コード';
comment on column bumon_kamoku_edaban_yosan.edaban_code is '枝番コード';
comment on column bumon_kamoku_edaban_yosan.futan_bumon_name is '負担部門名';
comment on column bumon_kamoku_edaban_yosan.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column bumon_kamoku_edaban_yosan.kamoku_name_ryakushiki is '科目名（略式）';
comment on column bumon_kamoku_edaban_yosan.kamoku_name_seishiki is '科目名（正式）';
comment on column bumon_kamoku_edaban_yosan.taishaku_zokusei is '貸借属性';
comment on column bumon_kamoku_edaban_yosan.yosan_01 is '1ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_02 is '2ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_03 is '3ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_03_shu is '3ｹ月目修正予算';
comment on column bumon_kamoku_edaban_yosan.yosan_04 is '4ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_05 is '5ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_06 is '6ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_06_shu is '6ｹ月目修正予算';
comment on column bumon_kamoku_edaban_yosan.yosan_07 is '7ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_08 is '8ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_09 is '9ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_09_shu is '9ｹ月目修正予算';
comment on column bumon_kamoku_edaban_yosan.yosan_10 is '10ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_11 is '11ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_12 is '12ｹ月目予算';
comment on column bumon_kamoku_edaban_yosan.yosan_12_shu is '12ｹ月目修正予算';

comment on table bumon_kamoku_edaban_zandaka is '負担部門科目枝番残高';
comment on column bumon_kamoku_edaban_zandaka.futan_bumon_cd is '負担部門コード';
comment on column bumon_kamoku_edaban_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column bumon_kamoku_edaban_zandaka.kessanki_bangou is '決算期番号';
comment on column bumon_kamoku_edaban_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column bumon_kamoku_edaban_zandaka.edaban_code is '枝番コード';
comment on column bumon_kamoku_edaban_zandaka.futan_bumon_name is '負担部門名';
comment on column bumon_kamoku_edaban_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column bumon_kamoku_edaban_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column bumon_kamoku_edaban_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column bumon_kamoku_edaban_zandaka.taishaku_zokusei is '貸借属性';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari00 is '期首残高(借方)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi00 is '期首残高(貸方)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari01 is '1ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi01 is '1ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari02 is '2ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi02 is '2ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari03 is '3ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi03 is '3ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari03_shu is '3ｹ月目修正(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi03_shu is '3ｹ月目修正(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari04 is '4ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi04 is '4ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari05 is '5ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi05 is '5ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari06 is '6ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi06 is '6ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari06_shu is '6ｹ月目修正(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi06_shu is '6ｹ月目修正(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari07 is '7ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi07 is '7ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari08 is '8ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi08 is '8ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari09 is '9ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi09 is '9ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari09_shu is '9ｹ月目修正(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi09_shu is '9ｹ月目修正(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari10 is '10ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi10 is '10ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari11 is '11ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi11 is '11ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari12 is '12ｹ月目(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi12 is '12ｹ月目(貸)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kari12_shu is '12ｹ月目修正(借)';
comment on column bumon_kamoku_edaban_zandaka.zandaka_kashi12_shu is '12ｹ月目修正(貸)';
comment on column kamoku_edaban_zandaka.kazei_kbn is '課税区分';
comment on column kamoku_edaban_zandaka.bunri_kbn is '分離区分';


comment on table bumon_kamoku_yosan is '負担部門科目予算';
comment on column bumon_kamoku_yosan.futan_bumon_cd is '負担部門コード';
comment on column bumon_kamoku_yosan.kamoku_gaibu_cd is '科目外部コード';
comment on column bumon_kamoku_yosan.kessanki_bangou is '決算期番号';
comment on column bumon_kamoku_yosan.kamoku_naibu_cd is '科目内部コード';
comment on column bumon_kamoku_yosan.futan_bumon_name is '負担部門名';
comment on column bumon_kamoku_yosan.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column bumon_kamoku_yosan.kamoku_name_ryakushiki is '科目名（略式）';
comment on column bumon_kamoku_yosan.kamoku_name_seishiki is '科目名（正式）';
comment on column bumon_kamoku_yosan.taishaku_zokusei is '貸借属性';
comment on column bumon_kamoku_yosan.yosan_01 is '1ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_02 is '2ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_03 is '3ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_03_shu is '3ｹ月目修正予算';
comment on column bumon_kamoku_yosan.yosan_04 is '4ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_05 is '5ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_06 is '6ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_06_shu is '6ｹ月目修正予算';
comment on column bumon_kamoku_yosan.yosan_07 is '7ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_08 is '8ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_09 is '9ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_09_shu is '9ｹ月目修正予算';
comment on column bumon_kamoku_yosan.yosan_10 is '10ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_11 is '11ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_12 is '12ｹ月目予算';
comment on column bumon_kamoku_yosan.yosan_12_shu is '12ｹ月目修正予算';

comment on table bumon_kamoku_zandaka is '負担部門科目残高';
comment on column bumon_kamoku_zandaka.futan_bumon_cd is '負担部門コード';
comment on column bumon_kamoku_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column bumon_kamoku_zandaka.kessanki_bangou is '決算期番号';
comment on column bumon_kamoku_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column bumon_kamoku_zandaka.futan_bumon_name is '負担部門名';
comment on column bumon_kamoku_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column bumon_kamoku_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column bumon_kamoku_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column bumon_kamoku_zandaka.taishaku_zokusei is '貸借属性';
comment on column bumon_kamoku_zandaka.zandaka_kari00 is '期首残高(借方)';
comment on column bumon_kamoku_zandaka.zandaka_kashi00 is '期首残高(貸方)';
comment on column bumon_kamoku_zandaka.zandaka_kari01 is '1ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi01 is '1ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari02 is '2ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi02 is '2ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari03 is '3ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi03 is '3ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari03_shu is '3ｹ月目修正(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi03_shu is '3ｹ月目修正(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari04 is '4ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi04 is '4ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari05 is '5ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi05 is '5ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari06 is '6ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi06 is '6ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari06_shu is '6ｹ月目修正(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi06_shu is '6ｹ月目修正(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari07 is '7ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi07 is '7ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari08 is '8ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi08 is '8ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari09 is '9ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi09 is '9ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari09_shu is '9ｹ月目修正(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi09_shu is '9ｹ月目修正(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari10 is '10ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi10 is '10ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari11 is '11ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi11 is '11ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari12 is '12ｹ月目(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi12 is '12ｹ月目(貸)';
comment on column bumon_kamoku_zandaka.zandaka_kari12_shu is '12ｹ月目修正(借)';
comment on column bumon_kamoku_zandaka.zandaka_kashi12_shu is '12ｹ月目修正(貸)';

comment on table bumon_master is '負担部門';
comment on column bumon_master.futan_bumon_cd is '負担部門コード';
comment on column bumon_master.futan_bumon_name is '負担部門名';
comment on column bumon_master.kessanki_bangou is '決算期番号';
comment on column bumon_master.shaukei_bumon_flg is '集計部門フラグ';
comment on column bumon_master.shiire_kbn is '仕入区分';
comment on column bumon_master.nyuryoku_from_date is '入力開始日';
comment on column bumon_master.nyuryoku_to_date is '入力終了日';

comment on table bumon_role is '部門ロール';
comment on column bumon_role.bumon_role_id is '部門ロールID';
comment on column bumon_role.bumon_role_name is '部門ロール名';
comment on column bumon_role.hyouji_jun is '表示順';
comment on column bumon_role.touroku_user_id is '登録ユーザーID';
comment on column bumon_role.touroku_time is '登録日時';
comment on column bumon_role.koushin_user_id is '更新ユーザーID';
comment on column bumon_role.koushin_time is '更新日時';

comment on table bumon_suishou_route_ko is '部門推奨ルート子';
comment on column bumon_suishou_route_ko.denpyou_kbn is '伝票区分';
comment on column bumon_suishou_route_ko.bumon_cd is '部門コード';
comment on column bumon_suishou_route_ko.edano is '枝番号';
comment on column bumon_suishou_route_ko.edaedano is '枝枝番号';
comment on column bumon_suishou_route_ko.bumon_role_id is '部門ロールID';
comment on column bumon_suishou_route_ko.shounin_shori_kengen_no is '承認処理権限番号';
comment on column bumon_suishou_route_ko.gougi_pattern_no is '合議パターン番号';
comment on column bumon_suishou_route_ko.gougi_edano is '合議枝番';
comment on column bumon_suishou_route_ko.touroku_user_id is '登録ユーザーID';
comment on column bumon_suishou_route_ko.touroku_time is '登録日時';
comment on column bumon_suishou_route_ko.koushin_user_id is '更新ユーザーID';
comment on column bumon_suishou_route_ko.koushin_time is '更新日時';

comment on table bumon_suishou_route_oya is '部門推奨ルート親';
comment on column bumon_suishou_route_oya.denpyou_kbn is '伝票区分';
comment on column bumon_suishou_route_oya.bumon_cd is '部門コード';
comment on column bumon_suishou_route_oya.edano is '枝番号';
comment on column bumon_suishou_route_oya.default_flg is 'デフォルトフラグ';
comment on column bumon_suishou_route_oya.shiwake_edano is '仕訳枝番号';
comment on column bumon_suishou_route_oya.kingaku_from is '金額開始';
comment on column bumon_suishou_route_oya.kingaku_to is '金額終了';
comment on column bumon_suishou_route_oya.yuukou_kigen_from is '有効期限開始日';
comment on column bumon_suishou_route_oya.yuukou_kigen_to is '有効期限終了日';
comment on column bumon_suishou_route_oya.touroku_user_id is '登録ユーザーID';
comment on column bumon_suishou_route_oya.touroku_time is '登録日時';
comment on column bumon_suishou_route_oya.koushin_user_id is '更新ユーザーID';
comment on column bumon_suishou_route_oya.koushin_time is '更新日時';

comment on table bus_line_master is 'バス路線名マスター';
comment on column bus_line_master.line_cd is 'バス路線コード';
comment on column bus_line_master.line_name is '路線名';

comment on table daikou_shitei is '代行指定';
comment on column daikou_shitei.daikou_user_id is '代行ユーザーID';
comment on column daikou_shitei.hi_daikou_user_id is '被代行ユーザーID';
comment on column daikou_shitei.touroku_user_id is '登録ユーザーID';
comment on column daikou_shitei.touroku_time is '登録日時';
comment on column daikou_shitei.koushin_user_id is '更新ユーザーID';
comment on column daikou_shitei.koushin_time is '更新日時';

comment on table denpyou is '伝票';
comment on column denpyou.denpyou_id is '伝票ID';
comment on column denpyou.denpyou_kbn is '伝票区分';
comment on column denpyou.denpyou_joutai is '伝票状態';
comment on column denpyou.sanshou_denpyou_id is '参照伝票ID';
comment on column denpyou.daihyou_futan_bumon_cd is '代表負担部門コード';
comment on column denpyou.touroku_user_id is '登録ユーザーID';
comment on column denpyou.touroku_time is '登録日時';
comment on column denpyou.koushin_user_id is '更新ユーザーID';
comment on column denpyou.koushin_time is '更新日時';
comment on column denpyou.serial_no is 'シリアル番号';
comment on column denpyou.chuushutsu_zumi_flg is '抽出済フラグ';
comment on column denpyou.shounin_route_henkou_flg is '承認ルート変更フラグ';
comment on column denpyou.maruhi_flg is 'マル秘文書フラグ';
comment on column denpyou.yosan_check_nengetsu is '予算執行対象月';

comment on table denpyou_ichiran is '伝票一覧';
comment on column denpyou_ichiran.denpyou_id is '伝票ID';
comment on column denpyou_ichiran.name is 'ステータス';
comment on column denpyou_ichiran.denpyou_kbn is '伝票区分';
comment on column denpyou_ichiran.jisshi_kian_bangou is '実施起案番号';
comment on column denpyou_ichiran.shishutsu_kian_bangou is '支出起案番号';
comment on column denpyou_ichiran.yosan_shikkou_taishou is '予算執行対象';
comment on column denpyou_ichiran.yosan_check_nengetsu is '予算執行対象月';
comment on column denpyou_ichiran.serial_no is 'シリアル番号';
comment on column denpyou_ichiran.denpyou_shubetsu_url is '伝票種別URL';
comment on column denpyou_ichiran.touroku_time is '登録日時';
comment on column denpyou_ichiran.bumon_full_name is '部門フル名';
comment on column denpyou_ichiran.user_full_name is 'ユーザーフル名';
comment on column denpyou_ichiran.user_id is 'ユーザーID';
comment on column denpyou_ichiran.denpyou_joutai is '伝票状態';
comment on column denpyou_ichiran.koushin_time is '更新日時';
comment on column denpyou_ichiran.shouninbi is '承認日';
comment on column denpyou_ichiran.maruhi_flg is 'マル秘文書フラグ';
comment on column denpyou_ichiran.all_cnt is '全承認人数カウント';
comment on column denpyou_ichiran.cur_cnt is '承認済人数カウント';
comment on column denpyou_ichiran.zan_cnt is '残り承認人数カウント';
comment on column denpyou_ichiran.gen_bumon_full_name is '現在承認者部門フル名';
comment on column denpyou_ichiran.gen_user_full_name is '現在承認者ユーザーフル名';
comment on column denpyou_ichiran.gen_gyoumu_role_name is '現在承認者業務ロール名';
comment on column denpyou_ichiran.gen_name is '現在承認者名称';
comment on column denpyou_ichiran.version is 'バージョン';
comment on column denpyou_ichiran.kingaku is '金額';
comment on column denpyou_ichiran.gaika is '外貨';
comment on column denpyou_ichiran.houjin_kingaku is '法人カード払金額';
comment on column denpyou_ichiran.tehai_kingaku is '会社手配金額';
comment on column denpyou_ichiran.torihikisaki1 is '取引先1';
comment on column denpyou_ichiran.shiharaibi is '支払日';
comment on column denpyou_ichiran.shiharaikiboubi is '支払希望日';
comment on column denpyou_ichiran.shiharaihouhou is '支払方法';
comment on column denpyou_ichiran.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column denpyou_ichiran.keijoubi is '計上日';
comment on column denpyou_ichiran.shiwakekeijoubi is '仕訳計上日';
comment on column denpyou_ichiran.seisan_yoteibi is '精算予定日';
comment on column denpyou_ichiran.karibarai_denpyou_id is '仮払伝票ID';
comment on column denpyou_ichiran.houmonsaki is '訪問先';
comment on column denpyou_ichiran.mokuteki is '目的';
comment on column denpyou_ichiran.kenmei is '件名';
comment on column denpyou_ichiran.naiyou is '内容';
comment on column denpyou_ichiran.user_sei is 'ユーザー姓';
comment on column denpyou_ichiran.user_mei is 'ユーザー名';
comment on column denpyou_ichiran.seisankikan_from is '精算期間開始日';
comment on column denpyou_ichiran.seisankikan_to is '精算期間終了日';
comment on column denpyou_ichiran.gen_user_id is '現在承認者ユーザーIDリスト';
comment on column denpyou_ichiran.gen_gyoumu_role_id is '現在承認者業務ロールIDリスト';
comment on column denpyou_ichiran.kian_bangou_unyou_flg is '起案番号運用フラグ';
comment on column denpyou_ichiran.yosan_shikkou_taishou_cd is '予算執行対象コード';
comment on column denpyou_ichiran.kian_syuryou_flg is '起案終了フラグ';
comment on column denpyou_ichiran.futan_bumon_cd is '負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kari_futan_bumon_cd is '借方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_cd is '借方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kari_kamoku_edaban_cd is '借方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran.kari_torihikisaki_cd is '借方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_futan_bumon_cd is '貸方負担部門コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_cd is '貸方科目コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_kamoku_edaban_cd is '貸方科目枝番コード(一覧検索用)';
comment on column denpyou_ichiran.kashi_torihikisaki_cd is '貸方取引先コード(一覧検索用)';
comment on column denpyou_ichiran.meisai_kingaku is '明細金額(一覧検索用)';
comment on column denpyou_ichiran.tekiyou is '摘要(一覧検索用)';
comment on column denpyou_ichiran.houjin_card_use is '法人カード使用フラグ';
comment on column denpyou_ichiran.kaisha_tehai_use is '会社手配使用フラグ';
comment on column denpyou_ichiran.ryoushuusho_exist is '領収書フラグ';
comment on column denpyou_ichiran.miseisan_karibarai_exist is '未精算仮払伝票フラグ';
comment on column denpyou_ichiran.miseisan_ukagai_exist is '未精算伺い伝票フラグ';
comment on column denpyou_ichiran.shiwake_status is '仕訳データ作成ステータス';
comment on column denpyou_ichiran.fb_status is 'FBデータ作成ステータス';
comment on column denpyou_ichiran.jisshi_nendo is '実施年度';
comment on column denpyou_ichiran.shishutsu_nendo is '支出年度';
comment on column denpyou_ichiran.bumon_cd is '部門コード';
comment on column denpyou_ichiran.kian_bangou_input is '起案番号採番フラグ';
comment on column denpyou_ichiran.jigyousha_kbn is '事業者区分';
comment on column denpyou_ichiran.shain_no is '社員番号（起票者）';
comment on column denpyou_ichiran.shiharai_name is '支払先名(一覧検索用)';
comment on column denpyou_ichiran.zeinuki_meisai_kingaku is '税抜明細金額(一覧検索用)';
comment on column denpyou_ichiran.zeinuki_kingaku is '税抜金額';

comment on table denpyou_id_saiban is '伝票ID採番';
comment on column denpyou_id_saiban.touroku_date is '登録日';
comment on column denpyou_id_saiban.denpyou_kbn is '伝票区分';
comment on column denpyou_id_saiban.sequence_val is 'シーケンス値';

comment on table denpyou_kian_himozuke is '伝票起案紐付';
comment on column denpyou_kian_himozuke.denpyou_id is '伝票ID';
comment on column denpyou_kian_himozuke.bumon_cd is '部門コード';
comment on column denpyou_kian_himozuke.nendo is '年度';
comment on column denpyou_kian_himozuke.ryakugou is '略号';
comment on column denpyou_kian_himozuke.kian_bangou_from is '開始起案番号';
comment on column denpyou_kian_himozuke.kian_bangou is '起案番号';
comment on column denpyou_kian_himozuke.kian_syuryo_flg is '起案終了フラグ';
comment on column denpyou_kian_himozuke.kian_syuryo_bi is '起案終了日';
comment on column denpyou_kian_himozuke.kian_denpyou is '起案伝票';
comment on column denpyou_kian_himozuke.kian_denpyou_kbn is '起案伝票区分';
comment on column denpyou_kian_himozuke.jisshi_nendo is '実施年度';
comment on column denpyou_kian_himozuke.jisshi_kian_bangou is '実施起案番号';
comment on column denpyou_kian_himozuke.shishutsu_nendo is '支出年度';
comment on column denpyou_kian_himozuke.shishutsu_kian_bangou is '支出起案番号';
comment on column denpyou_kian_himozuke.ringi_kingaku is '稟議金額';
comment on column denpyou_kian_himozuke.ringi_kingaku_hikitsugimoto_denpyou is '稟議金額引継ぎ元伝票';
comment on column denpyou_kian_himozuke.ringi_kingaku_chouka_comment is '稟議金額超過コメント';

comment on table denpyou_serial_no_saiban is '伝票番号採番';
comment on column denpyou_serial_no_saiban.sequence_val is 'シーケンス値';
comment on column denpyou_serial_no_saiban.max_value is '最大値';
comment on column denpyou_serial_no_saiban.min_value is '最小値';

comment on table denpyou_shubetsu_ichiran is '伝票種別一覧';
comment on column denpyou_shubetsu_ichiran.denpyou_kbn is '伝票区分';
comment on column denpyou_shubetsu_ichiran.version is 'バージョン';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu is '伝票種別';
comment on column denpyou_shubetsu_ichiran.denpyou_karibarai_nashi_shubetsu is '伝票種別（仮払なし）';
comment on column denpyou_shubetsu_ichiran.denpyou_print_shubetsu is '伝票種別（帳票）';
comment on column denpyou_shubetsu_ichiran.denpyou_print_karibarai_nashi_shubetsu is '伝票種別（帳票・仮払なし）';
comment on column denpyou_shubetsu_ichiran.hyouji_jun is '表示順';
comment on column denpyou_shubetsu_ichiran.gyoumu_shubetsu is '業務種別';
comment on column denpyou_shubetsu_ichiran.naiyou is '内容（伝票）';
comment on column denpyou_shubetsu_ichiran.denpyou_shubetsu_url is '伝票種別URL';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_from is '有効期限開始日';
comment on column denpyou_shubetsu_ichiran.yuukou_kigen_to is '有効期限終了日';
comment on column denpyou_shubetsu_ichiran.kanren_sentaku_flg is '関連伝票選択フラグ';
comment on column denpyou_shubetsu_ichiran.kanren_hyouji_flg is '関連伝票入力欄表示フラグ';
comment on column denpyou_shubetsu_ichiran.denpyou_print_flg is '申請時帳票出力フラグ';
comment on column denpyou_shubetsu_ichiran.kianbangou_unyou_flg is '起案番号運用フラグ';
comment on column denpyou_shubetsu_ichiran.yosan_shikkou_taishou is '予算執行対象';
comment on column denpyou_shubetsu_ichiran.route_hantei_kingaku is 'ルート判定金額';
comment on column denpyou_shubetsu_ichiran.route_torihiki_flg is 'ルート取引毎設定フラグ';
comment on column denpyou_shubetsu_ichiran.shounin_jyoukyou_print_flg is '承認状況欄印刷フラグ';
comment on column denpyou_shubetsu_ichiran.shinsei_shori_kengen_name is '申請処理権限名';
comment on column denpyou_shubetsu_ichiran.shiiresaki_flg is '仕入先フラグ';
comment on column denpyou_shubetsu_ichiran.touroku_user_id is '登録ユーザーID';
comment on column denpyou_shubetsu_ichiran.touroku_time is '登録日時';
comment on column denpyou_shubetsu_ichiran.koushin_user_id is '更新ユーザーID';
comment on column denpyou_shubetsu_ichiran.koushin_time is '更新日時';

comment on table ebunsho_data is 'e文書データ';
comment on column ebunsho_data.ebunsho_no is 'e文書番号';
comment on column ebunsho_data.ebunsho_edano is 'e文書枝番号';
comment on column ebunsho_data.ebunsho_shubetsu is 'e文書種別';
comment on column ebunsho_data.ebunsho_nengappi is 'e文書年月日';
comment on column ebunsho_data.ebunsho_kingaku is 'e文書金額';
comment on column ebunsho_data.ebunsho_hakkousha is 'e文書発行者';
comment on column ebunsho_data.ebunsho_hinmei is 'e文書品名';

comment on table ebunsho_file is 'e文書ファイル';
comment on column ebunsho_file.denpyou_id is '伝票ID';
comment on column ebunsho_file.edano is '枝番号';
comment on column ebunsho_file.ebunsho_no is 'e文書番号';
comment on column ebunsho_file.binary_data is 'バイナリーデータ';
comment on column ebunsho_file.denshitorihiki_flg is '電子取引フラグ';
comment on column ebunsho_file.tsfuyo_flg is 'タイムスタンプ付与フラグ';
comment on column ebunsho_file.touroku_user_id is '登録ユーザーID';
comment on column ebunsho_file.touroku_time is '登録日時';

comment on table eki_master is '駅マスター';
comment on column eki_master.region_cd is '地域コード';
comment on column eki_master.line_cd is '路線コード';
comment on column eki_master.eki_cd is '駅コード';
comment on column eki_master.line_name is '路線名';
comment on column eki_master.eki_name is '駅名';

comment on table event_log is 'イベントログ';
comment on column event_log.serial_no is 'シリアル番号';
comment on column event_log.start_time is '開始日時';
comment on column event_log.end_time is '終了日時';
comment on column event_log.user_id is 'ユーザーID';
comment on column event_log.gamen_id is '画面ID';
comment on column event_log.event_id is 'イベントID';
comment on column event_log.result is '処理結果';

comment on table extension_setting is '拡張子設定';
comment on column extension_setting.extension_cd is '拡張子コード';
comment on column extension_setting.extension_flg is '有効フラグ';

comment on table fb is 'FB抽出';
comment on column fb.serial_no is 'シリアル番号';
comment on column fb.denpyou_id is '伝票ID';
comment on column fb.user_id is 'ユーザーID';
comment on column fb.fb_status is 'FB抽出状態';
comment on column fb.touroku_time is '登録日時';
comment on column fb.koushin_time is '更新日時';
comment on column fb.shubetsu_cd is '種別コード';
comment on column fb.cd_kbn is 'コード区分';
comment on column fb.kaisha_cd is '会社コード';
comment on column fb.kaisha_name_hankana is '会社名（半角カナ）';
comment on column fb.furikomi_date is '振込日';
comment on column fb.moto_kinyuukikan_cd is '振込元金融機関コード';
comment on column fb.moto_kinyuukikan_name_hankana is '振込元金融機関名（半角カナ）';
comment on column fb.moto_kinyuukikan_shiten_cd is '振込元金融機関支店コード';
comment on column fb.moto_kinyuukikan_shiten_name_hankana is '振込元金融機関支店名（半角カナ）';
comment on column fb.moto_yokin_shumoku_cd is '振込元預金種目コード';
comment on column fb.moto_kouza_bangou is '振込元口座番号';
comment on column fb.saki_kinyuukikan_cd is '振込先金融機関銀行コード';
comment on column fb.saki_kinyuukikan_name_hankana is '振込先金融機関名（半角カナ）';
comment on column fb.saki_kinyuukikan_shiten_cd is '振込先金融機関支店コード';
comment on column fb.saki_kinyuukikan_shiten_name_hankana is '振込先金融機関支店名（半角カナ）';
comment on column fb.saki_yokin_shumoku_cd is '振込先預金種目コード';
comment on column fb.saki_kouza_bangou is '振込先口座番号';
comment on column fb.saki_kouza_meigi_kana is '振込先口座名義（半角カナ）';
comment on column fb.kingaku is '金額';
comment on column fb.shinki_cd is '新規コード';
comment on column fb.kokyaku_cd1 is '顧客コード１';
comment on column fb.furikomi_kbn is '振込区分';

comment on table furikae is '振替';
comment on column furikae.denpyou_id is '伝票ID';
comment on column furikae.denpyou_date is '伝票日付';
comment on column furikae.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column furikae.kingaku is '金額';
comment on column furikae.hontai_kingaku is '本体金額';
comment on column furikae.shouhizeigaku is '消費税額';
comment on column furikae.kari_zeiritsu is '借方税率';
comment on column furikae.kari_keigen_zeiritsu_kbn is '借方軽減税率区分';
comment on column furikae.tekiyou is '摘要';
comment on column furikae.hf1_cd is 'HF1コード';
comment on column furikae.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column furikae.hf2_cd is 'HF2コード';
comment on column furikae.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column furikae.hf3_cd is 'HF3コード';
comment on column furikae.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column furikae.hf4_cd is 'HF4コード';
comment on column furikae.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column furikae.hf5_cd is 'HF5コード';
comment on column furikae.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column furikae.hf6_cd is 'HF6コード';
comment on column furikae.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column furikae.hf7_cd is 'HF7コード';
comment on column furikae.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column furikae.hf8_cd is 'HF8コード';
comment on column furikae.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column furikae.hf9_cd is 'HF9コード';
comment on column furikae.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column furikae.hf10_cd is 'HF10コード';
comment on column furikae.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column furikae.bikou is '備考（会計伝票）';
comment on column furikae.kari_futan_bumon_cd is '借方負担部門コード';
comment on column furikae.kari_futan_bumon_name is '借方負担部門名';
comment on column furikae.kari_kamoku_cd is '借方科目コード';
comment on column furikae.kari_kamoku_name is '借方科目名';
comment on column furikae.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column furikae.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column furikae.kari_kazei_kbn is '借方課税区分';
comment on column furikae.kari_torihikisaki_cd is '借方取引先コード';
comment on column furikae.kari_torihikisaki_name_ryakushiki is '借方取引先名（略式）';
comment on column furikae.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column furikae.kashi_futan_bumon_name is '貸方負担部門名';
comment on column furikae.kashi_kamoku_cd is '貸方科目コード';
comment on column furikae.kashi_kamoku_name is '貸方科目名';
comment on column furikae.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column furikae.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column furikae.kashi_kazei_kbn is '貸方課税区分';
comment on column furikae.kashi_torihikisaki_cd is '貸方取引先コード';
comment on column furikae.kashi_torihikisaki_name_ryakushiki is '貸方取引先名（略式）';
comment on column furikae.kari_uf1_cd is '借方UF1コード';
comment on column furikae.kari_uf1_name_ryakushiki is '借方UF1名（略式）';
comment on column furikae.kari_uf2_cd is '借方UF2コード';
comment on column furikae.kari_uf2_name_ryakushiki is '借方UF2名（略式）';
comment on column furikae.kari_uf3_cd is '借方UF3コード';
comment on column furikae.kari_uf3_name_ryakushiki is '借方UF3名（略式）';
comment on column furikae.kari_uf4_cd is '借方UF4コード';
comment on column furikae.kari_uf4_name_ryakushiki is '借方UF4名（略式）';
comment on column furikae.kari_uf5_cd is '借方UF5コード';
comment on column furikae.kari_uf5_name_ryakushiki is '借方UF5名（略式）';
comment on column furikae.kari_uf6_cd is '借方UF6コード';
comment on column furikae.kari_uf6_name_ryakushiki is '借方UF6名（略式）';
comment on column furikae.kari_uf7_cd is '借方UF7コード';
comment on column furikae.kari_uf7_name_ryakushiki is '借方UF7名（略式）';
comment on column furikae.kari_uf8_cd is '借方UF8コード';
comment on column furikae.kari_uf8_name_ryakushiki is '借方UF8名（略式）';
comment on column furikae.kari_uf9_cd is '借方UF9コード';
comment on column furikae.kari_uf9_name_ryakushiki is '借方UF9名（略式）';
comment on column furikae.kari_uf10_cd is '借方UF10コード';
comment on column furikae.kari_uf10_name_ryakushiki is '借方UF10名（略式）';
comment on column furikae.kashi_uf1_cd is '貸方UF1コード';
comment on column furikae.kashi_uf1_name_ryakushiki is '貸方UF1名（略式）';
comment on column furikae.kashi_uf2_cd is '貸方UF2コード';
comment on column furikae.kashi_uf2_name_ryakushiki is '貸方UF2名（略式）';
comment on column furikae.kashi_uf3_cd is '貸方UF3コード';
comment on column furikae.kashi_uf3_name_ryakushiki is '貸方UF3名（略式）';
comment on column furikae.kashi_uf4_cd is '貸方UF4コード';
comment on column furikae.kashi_uf4_name_ryakushiki is '貸方UF4名（略式）';
comment on column furikae.kashi_uf5_cd is '貸方UF5コード';
comment on column furikae.kashi_uf5_name_ryakushiki is '貸方UF5名（略式）';
comment on column furikae.kashi_uf6_cd is '貸方UF6コード';
comment on column furikae.kashi_uf6_name_ryakushiki is '貸方UF6名（略式）';
comment on column furikae.kashi_uf7_cd is '貸方UF7コード';
comment on column furikae.kashi_uf7_name_ryakushiki is '貸方UF7名（略式）';
comment on column furikae.kashi_uf8_cd is '貸方UF8コード';
comment on column furikae.kashi_uf8_name_ryakushiki is '貸方UF8名（略式）';
comment on column furikae.kashi_uf9_cd is '貸方UF9コード';
comment on column furikae.kashi_uf9_name_ryakushiki is '貸方UF9名（略式）';
comment on column furikae.kashi_uf10_cd is '貸方UF10コード';
comment on column furikae.kashi_uf10_name_ryakushiki is '貸方UF10名（略式）';
comment on column furikae.kari_project_cd is '借方プロジェクトコード';
comment on column furikae.kari_project_name is '借方プロジェクト名';
comment on column furikae.kari_segment_cd is '借方セグメントコード';
comment on column furikae.kari_segment_name_ryakushiki is '借方セグメント名（略式）';
comment on column furikae.kashi_project_cd is '貸方プロジェクトコード';
comment on column furikae.kashi_project_name is '貸方プロジェクト名';
comment on column furikae.kashi_segment_cd is '貸方セグメントコード';
comment on column furikae.kashi_segment_name_ryakushiki is '貸方セグメント名（略式）';
comment on column furikae.touroku_user_id is '登録ユーザーID';
comment on column furikae.touroku_time is '登録日時';
comment on column furikae.koushin_user_id is '更新ユーザーID';
comment on column furikae.koushin_time is '更新日時';
comment on column furikae.kari_bunri_kbn is '借方分離区分';
comment on column furikae.kashi_bunri_kbn is '貸方分離区分';
comment on column furikae.kari_jigyousha_kbn is '借方事業者区分';
comment on column furikae.kari_shiire_kbn is '借方仕入区分';
comment on column furikae.kari_zeigaku_houshiki is '借方税額計算方式';
comment on column furikae.kashi_jigyousha_kbn is '貸方事業者区分';
comment on column furikae.kashi_shiire_kbn is '貸方仕入区分';
comment on column furikae.kashi_zeigaku_houshiki is '貸方税額計算方式';
comment on column furikae.invoice_denpyou is 'インボイス対応伝票';
comment on column furikae.kashi_zeiritsu is '貸方税率';
comment on column furikae.kashi_keigen_zeiritsu_kbn is '貸方軽減税率区分';

comment on table furikomi_bi_rule_hi is '振込日ルール(日付)';
comment on column furikomi_bi_rule_hi.kijun_date is '基準日';
comment on column furikomi_bi_rule_hi.furikomi_date is '振込日';

comment on table furikomi_bi_rule_youbi is '振込日ルール(曜日)';
comment on column furikomi_bi_rule_youbi.kijun_weekday is '基準曜日';
comment on column furikomi_bi_rule_youbi.furikomi_weekday is '振込曜日';

comment on table gamen_kengen_seigyo is '画面権限制御';
comment on column gamen_kengen_seigyo.gamen_id is '画面ID';
comment on column gamen_kengen_seigyo.gamen_name is '画面名';
comment on column gamen_kengen_seigyo.bumon_shozoku_riyoukanou_flg is '部門所属ユーザー利用可能フラグ';
comment on column gamen_kengen_seigyo.system_kanri_riyoukanou_flg is 'システム管理利用可能フラグ';
comment on column gamen_kengen_seigyo.workflow_riyoukanou_flg is 'ワークフロー利用可能フラグ';
comment on column gamen_kengen_seigyo.kaishasettei_riyoukanou_flg is '会社設定利用可能フラグ';
comment on column gamen_kengen_seigyo.keirishori_riyoukanou_flg is '経理処理権限利用可能フラグ';
comment on column gamen_kengen_seigyo.kinou_seigyo_cd is '機能制御コード';

comment on table gamen_koumoku_seigyo is '画面項目制御';
comment on column gamen_koumoku_seigyo.denpyou_kbn is '伝票区分';
comment on column gamen_koumoku_seigyo.koumoku_id is '項目ID';
comment on column gamen_koumoku_seigyo.default_koumoku_name is 'デフォルト項目名';
comment on column gamen_koumoku_seigyo.koumoku_name is '項目名';
comment on column gamen_koumoku_seigyo.hyouji_flg is '表示フラグ';
comment on column gamen_koumoku_seigyo.hissu_flg is '必須フラグ';
comment on column gamen_koumoku_seigyo.hyouji_seigyo_flg is '表示制御フラグ';
comment on column gamen_koumoku_seigyo.pdf_hyouji_flg is '帳票表示フラグ';
comment on column gamen_koumoku_seigyo.pdf_hyouji_seigyo_flg is '帳票表示制御フラグ';
comment on column gamen_koumoku_seigyo.code_output_flg is '帳票コード印字フラグ';
comment on column gamen_koumoku_seigyo.code_output_seigyo_flg is '帳票コード印字制御フラグ';
comment on column gamen_koumoku_seigyo.hyouji_jun is '表示順';

comment on table gazou_file is '画像ファイル';
comment on column gazou_file.serial_no is 'シリアル番号';
comment on column gazou_file.file_name is 'ファイル名';
comment on column gazou_file.file_size is 'ファイルサイズ';
comment on column gazou_file.content_type is 'コンテンツタイプ';
comment on column gazou_file.binary_data is 'バイナリーデータ';
comment on column gazou_file.touroku_user_id is '登録ユーザーID';
comment on column gazou_file.touroku_time is '登録日時';
comment on column gazou_file.koushin_user_id is '更新ユーザーID';
comment on column gazou_file.koushin_time is '更新日時';

comment on table gougi_pattern_ko is '合議パターン子';
comment on column gougi_pattern_ko.gougi_pattern_no is '合議パターン番号';
comment on column gougi_pattern_ko.edano is '枝番号';
comment on column gougi_pattern_ko.bumon_cd is '部門コード';
comment on column gougi_pattern_ko.bumon_role_id is '部門ロールID';
comment on column gougi_pattern_ko.shounin_shori_kengen_no is '承認処理権限番号';
comment on column gougi_pattern_ko.shounin_ninzuu_cd is '承認人数コード';
comment on column gougi_pattern_ko.shounin_ninzuu_hiritsu is '承認人数比率';
comment on column gougi_pattern_ko.touroku_user_id is '登録ユーザーID';
comment on column gougi_pattern_ko.touroku_time is '登録日時';
comment on column gougi_pattern_ko.koushin_user_id is '更新ユーザーID';
comment on column gougi_pattern_ko.koushin_time is '更新日時';

comment on table gougi_pattern_oya is '合議パターン親';
comment on column gougi_pattern_oya.gougi_pattern_no is '合議パターン番号';
comment on column gougi_pattern_oya.gougi_name is '合議名';
comment on column gougi_pattern_oya.hyouji_jun is '表示順';
comment on column gougi_pattern_oya.touroku_user_id is '登録ユーザーID';
comment on column gougi_pattern_oya.touroku_time is '登録日時';
comment on column gougi_pattern_oya.koushin_user_id is '更新ユーザーID';
comment on column gougi_pattern_oya.koushin_time is '更新日時';

comment on table gyoumu_role is '業務ロール';
comment on column gyoumu_role.gyoumu_role_id is '業務ロールID';
comment on column gyoumu_role.gyoumu_role_name is '業務ロール名';
comment on column gyoumu_role.hyouji_jun is '表示順';
comment on column gyoumu_role.touroku_user_id is '登録ユーザーID';
comment on column gyoumu_role.touroku_time is '登録日時';
comment on column gyoumu_role.koushin_user_id is '更新ユーザーID';
comment on column gyoumu_role.koushin_time is '更新日時';

comment on table gyoumu_role_kinou_seigyo is '業務ロール機能制御';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_id is '業務ロールID';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_kinou_seigyo_cd is '業務ロール機能制御コード';
comment on column gyoumu_role_kinou_seigyo.gyoumu_role_kinou_seigyo_kbn is '業務ロール機能制御区分';
comment on column gyoumu_role_kinou_seigyo.touroku_user_id is '登録ユーザーID';
comment on column gyoumu_role_kinou_seigyo.touroku_time is '登録日時';
comment on column gyoumu_role_kinou_seigyo.koushin_user_id is '更新ユーザーID';
comment on column gyoumu_role_kinou_seigyo.koushin_time is '更新日時';

comment on table gyoumu_role_wariate is '業務ロール割り当て';
comment on column gyoumu_role_wariate.user_id is 'ユーザーID';
comment on column gyoumu_role_wariate.gyoumu_role_id is '業務ロールID';
comment on column gyoumu_role_wariate.yuukou_kigen_from is '有効期限開始日';
comment on column gyoumu_role_wariate.yuukou_kigen_to is '有効期限終了日';
comment on column gyoumu_role_wariate.shori_bumon_cd is '処理部門コード';
comment on column gyoumu_role_wariate.touroku_user_id is '登録ユーザーID';
comment on column gyoumu_role_wariate.touroku_time is '登録日時';
comment on column gyoumu_role_wariate.koushin_user_id is '更新ユーザーID';
comment on column gyoumu_role_wariate.koushin_time is '更新日時';

comment on table heishu_master is '幣種マスター';
comment on column heishu_master.heishu_cd is '幣種コード';
comment on column heishu_master.currency_unit is '通貨単位';
comment on column heishu_master.country_name is '国または地域';
comment on column heishu_master.conversion_unit is '換算単位';
comment on column heishu_master.decimal_position is '小数部桁数';
comment on column heishu_master.availability_flg is '使用可否';
comment on column heishu_master.display_order is '表示順';

comment on table help is 'ヘルプ';
comment on column help.gamen_id is '画面ID';
comment on column help.help_rich_text is 'ヘルプリッチテキスト';
comment on column help.touroku_user_id is '登録ユーザーID';
comment on column help.touroku_time is '登録日時';
comment on column help.koushin_user_id is '更新ユーザーID';
comment on column help.koushin_time is '更新日時';

comment on table hf1_ichiran is 'ヘッダフィールド１一覧';
comment on column hf1_ichiran.hf1_cd is 'HF1コード';
comment on column hf1_ichiran.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column hf1_ichiran.kessanki_bangou is '決算期番号';

comment on table hf10_ichiran is 'ヘッダフィールド１０一覧';
comment on column hf10_ichiran.hf10_cd is 'HF10コード';
comment on column hf10_ichiran.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column hf10_ichiran.kessanki_bangou is '決算期番号';

comment on table hf2_ichiran is 'ヘッダフィールド２一覧';
comment on column hf2_ichiran.hf2_cd is 'HF2コード';
comment on column hf2_ichiran.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column hf2_ichiran.kessanki_bangou is '決算期番号';

comment on table hf3_ichiran is 'ヘッダフィールド３一覧';
comment on column hf3_ichiran.hf3_cd is 'HF3コード';
comment on column hf3_ichiran.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column hf3_ichiran.kessanki_bangou is '決算期番号';

comment on table hf4_ichiran is 'ヘッダフィールド４一覧';
comment on column hf4_ichiran.hf4_cd is 'HF4コード';
comment on column hf4_ichiran.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column hf4_ichiran.kessanki_bangou is '決算期番号';

comment on table hf5_ichiran is 'ヘッダフィールド５一覧';
comment on column hf5_ichiran.hf5_cd is 'HF5コード';
comment on column hf5_ichiran.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column hf5_ichiran.kessanki_bangou is '決算期番号';

comment on table hf6_ichiran is 'ヘッダフィールド６一覧';
comment on column hf6_ichiran.hf6_cd is 'HF6コード';
comment on column hf6_ichiran.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column hf6_ichiran.kessanki_bangou is '決算期番号';

comment on table hf7_ichiran is 'ヘッダフィールド７一覧';
comment on column hf7_ichiran.hf7_cd is 'HF7コード';
comment on column hf7_ichiran.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column hf7_ichiran.kessanki_bangou is '決算期番号';

comment on table hf8_ichiran is 'ヘッダフィールド８一覧';
comment on column hf8_ichiran.hf8_cd is 'HF8コード';
comment on column hf8_ichiran.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column hf8_ichiran.kessanki_bangou is '決算期番号';

comment on table hf9_ichiran is 'ヘッダフィールド９一覧';
comment on column hf9_ichiran.hf9_cd is 'HF9コード';
comment on column hf9_ichiran.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column hf9_ichiran.kessanki_bangou is '決算期番号';

comment on table hizuke_control is '日付コントロール';
comment on column hizuke_control.system_kanri_date is 'システム管理日付';
comment on column hizuke_control.fb_data_sakuseibi_flg is 'FBデータ作成日フラグ';
comment on column hizuke_control.kojinseisan_shiharaibi is '個人精算支払日';
comment on column hizuke_control.kinyuukikan_eigyoubi_flg is '金融機関営業日フラグ';
comment on column hizuke_control.toujitsu_kbn_flg is '当日区分フラグ';
comment on column hizuke_control.keijoubi_flg is '計上日フラグ';

comment on table houjin_card_import is '法人カードインポート';
comment on column houjin_card_import.user_id is 'ユーザーID';
comment on column houjin_card_import.houjin_card_shubetsu is '法人カード種別';

comment on table houjin_card_jouhou is '法人カード使用履歴情報';
comment on column houjin_card_jouhou.card_jouhou_id is 'カード情報ID';
comment on column houjin_card_jouhou.card_shubetsu is 'カード種別コード';
comment on column houjin_card_jouhou.torikomi_denpyou_id is '取込先伝票ID';
comment on column houjin_card_jouhou.busho_cd is '部署コード';
comment on column houjin_card_jouhou.shain_bangou is '社員番号';
comment on column houjin_card_jouhou.shiyousha is '使用者';
comment on column houjin_card_jouhou.riyoubi is '利用日';
comment on column houjin_card_jouhou.kingaku is '金額';
comment on column houjin_card_jouhou.card_bangou is 'カード番号';
comment on column houjin_card_jouhou.kameiten is '加盟店';
comment on column houjin_card_jouhou.gyoushu_cd is '業種コード';
comment on column houjin_card_jouhou.jyogai_flg is '除外フラグ';
comment on column houjin_card_jouhou.jyogai_riyuu is '除外理由';
comment on column houjin_card_jouhou.touroku_user_id is '登録ユーザーID';
comment on column houjin_card_jouhou.touroku_time is '登録日時';
comment on column houjin_card_jouhou.koushin_user_id is '更新ユーザーID';
comment on column houjin_card_jouhou.koushin_time is '更新日時';

comment on table ic_card is 'ICカード';
comment on column ic_card.ic_card_no is 'ICカード番号';
comment on column ic_card.token is 'トークン';
comment on column ic_card.user_id is 'ユーザーID';

comment on table ic_card_rireki is 'ICカード利用履歴';
comment on column ic_card_rireki.ic_card_no is 'ICカード番号';
comment on column ic_card_rireki.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column ic_card_rireki.ic_card_riyoubi is 'ICカード利用日';
comment on column ic_card_rireki.tanmatu_cd is '端末種別';
comment on column ic_card_rireki.line_cd_from is '路線コード（FROM）';
comment on column ic_card_rireki.line_name_from is '路線名（FROM）';
comment on column ic_card_rireki.eki_cd_from is '駅コード（FROM）';
comment on column ic_card_rireki.eki_name_from is '駅名（FROM）';
comment on column ic_card_rireki.line_cd_to is '路線コード（TO）';
comment on column ic_card_rireki.line_name_to is '路線名（TO）';
comment on column ic_card_rireki.eki_cd_to is '駅コード（TO）';
comment on column ic_card_rireki.eki_name_to is '駅名（TO）';
comment on column ic_card_rireki.kingaku is '金額';
comment on column ic_card_rireki.user_id is 'ユーザーID';
comment on column ic_card_rireki.jyogai_flg is '除外フラグ';
comment on column ic_card_rireki.shori_cd is '処理内容';
comment on column ic_card_rireki.balance is '残高';
comment on column ic_card_rireki.all_byte is '全バイト配列';

comment on table info_id_saiban is 'インフォメーションID採番';
comment on column info_id_saiban.touroku_date is '登録日';
comment on column info_id_saiban.sequence_val is 'シーケンス値';

comment on table information is 'インフォメーション';
comment on column information.info_id is 'インフォメーションID';
comment on column information.tsuuchi_kikan_from is '通知期間開始日';
comment on column information.tsuuchi_kikan_to is '通知期間終了日';
comment on column information.tsuuchi_naiyou is '通知内容';
comment on column information.touroku_user_id is '登録ユーザーID';
comment on column information.touroku_time is '登録日時';
comment on column information.koushin_user_id is '更新ユーザーID';
comment on column information.koushin_time is '更新日時';

comment on table information_sort is 'インフォメーション順序';
comment on column information_sort.info_id is 'インフォメーションID';
comment on column information_sort.hyouji_jun is '表示順';

comment on table jidouhikiotoshi is '自動引落';
comment on column jidouhikiotoshi.denpyou_id is '伝票ID';
comment on column jidouhikiotoshi.keijoubi is '計上日';
comment on column jidouhikiotoshi.hikiotoshibi is '引落日';
comment on column jidouhikiotoshi.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column jidouhikiotoshi.hontai_kingaku_goukei is '本体金額合計';
comment on column jidouhikiotoshi.shouhizeigaku_goukei is '消費税額合計';
comment on column jidouhikiotoshi.shiharai_kingaku_goukei is '支払金額合計';
comment on column jidouhikiotoshi.hf1_cd is 'HF1コード';
comment on column jidouhikiotoshi.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column jidouhikiotoshi.hf2_cd is 'HF2コード';
comment on column jidouhikiotoshi.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column jidouhikiotoshi.hf3_cd is 'HF3コード';
comment on column jidouhikiotoshi.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column jidouhikiotoshi.hf4_cd is 'HF4コード';
comment on column jidouhikiotoshi.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column jidouhikiotoshi.hf5_cd is 'HF5コード';
comment on column jidouhikiotoshi.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column jidouhikiotoshi.hf6_cd is 'HF6コード';
comment on column jidouhikiotoshi.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column jidouhikiotoshi.hf7_cd is 'HF7コード';
comment on column jidouhikiotoshi.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column jidouhikiotoshi.hf8_cd is 'HF8コード';
comment on column jidouhikiotoshi.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column jidouhikiotoshi.hf9_cd is 'HF9コード';
comment on column jidouhikiotoshi.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column jidouhikiotoshi.hf10_cd is 'HF10コード';
comment on column jidouhikiotoshi.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column jidouhikiotoshi.hosoku is '補足';
comment on column jidouhikiotoshi.touroku_user_id is '登録ユーザーID';
comment on column jidouhikiotoshi.touroku_time is '登録日時';
comment on column jidouhikiotoshi.koushin_user_id is '更新ユーザーID';
comment on column jidouhikiotoshi.koushin_time is '更新日時';
comment on column jidouhikiotoshi.nyuryoku_houshiki is '入力方式';
comment on column jidouhikiotoshi.invoice_denpyou is 'インボイス対応伝票';

comment on table jidouhikiotoshi_meisai is '自動引落明細';
comment on column jidouhikiotoshi_meisai.denpyou_id is '伝票ID';
comment on column jidouhikiotoshi_meisai.denpyou_edano is '伝票枝番号';
comment on column jidouhikiotoshi_meisai.shiwake_edano is '仕訳枝番号';
comment on column jidouhikiotoshi_meisai.torihiki_name is '取引名';
comment on column jidouhikiotoshi_meisai.tekiyou is '摘要';
comment on column jidouhikiotoshi_meisai.zeiritsu is '税率';
comment on column jidouhikiotoshi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column jidouhikiotoshi_meisai.shiharai_kingaku is '支払金額';
comment on column jidouhikiotoshi_meisai.hontai_kingaku is '本体金額';
comment on column jidouhikiotoshi_meisai.shouhizeigaku is '消費税額';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column jidouhikiotoshi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column jidouhikiotoshi_meisai.torihikisaki_cd is '取引先コード';
comment on column jidouhikiotoshi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column jidouhikiotoshi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column jidouhikiotoshi_meisai.kari_kamoku_name is '借方科目名';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column jidouhikiotoshi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column jidouhikiotoshi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column jidouhikiotoshi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column jidouhikiotoshi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column jidouhikiotoshi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column jidouhikiotoshi_meisai.uf1_cd is 'UF1コード';
comment on column jidouhikiotoshi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column jidouhikiotoshi_meisai.uf2_cd is 'UF2コード';
comment on column jidouhikiotoshi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column jidouhikiotoshi_meisai.uf3_cd is 'UF3コード';
comment on column jidouhikiotoshi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column jidouhikiotoshi_meisai.uf4_cd is 'UF4コード';
comment on column jidouhikiotoshi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column jidouhikiotoshi_meisai.uf5_cd is 'UF5コード';
comment on column jidouhikiotoshi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column jidouhikiotoshi_meisai.uf6_cd is 'UF6コード';
comment on column jidouhikiotoshi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column jidouhikiotoshi_meisai.uf7_cd is 'UF7コード';
comment on column jidouhikiotoshi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column jidouhikiotoshi_meisai.uf8_cd is 'UF8コード';
comment on column jidouhikiotoshi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column jidouhikiotoshi_meisai.uf9_cd is 'UF9コード';
comment on column jidouhikiotoshi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column jidouhikiotoshi_meisai.uf10_cd is 'UF10コード';
comment on column jidouhikiotoshi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column jidouhikiotoshi_meisai.project_cd is 'プロジェクトコード';
comment on column jidouhikiotoshi_meisai.project_name is 'プロジェクト名';
comment on column jidouhikiotoshi_meisai.segment_cd is 'セグメントコード';
comment on column jidouhikiotoshi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column jidouhikiotoshi_meisai.tekiyou_cd is '摘要コード';
comment on column jidouhikiotoshi_meisai.touroku_user_id is '登録ユーザーID';
comment on column jidouhikiotoshi_meisai.touroku_time is '登録日時';
comment on column jidouhikiotoshi_meisai.koushin_user_id is '更新ユーザーID';
comment on column jidouhikiotoshi_meisai.koushin_time is '更新日時';
comment on column jidouhikiotoshi_meisai.jigyousha_kbn is '事業者区分';
comment on column jidouhikiotoshi_meisai.bunri_kbn is '分離区分';
comment on column jidouhikiotoshi_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column jidouhikiotoshi_meisai.kashi_shiire_kbn is '貸方仕入区分';

comment on table jishou is '事象';
comment on column jishou.jishou_id is '事象ID';
comment on column jishou.midashi_id is '見出しID';
comment on column jishou.jishou_name is '事象名';
comment on column jishou.hyouji_jun is '表示順';
comment on column jishou.touroku_user_id is '登録ユーザーID';
comment on column jishou.touroku_time is '登録日時';
comment on column jishou.koushin_user_id is '更新ユーザーID';
comment on column jishou.koushin_time is '更新日時';

comment on table jishou_dpkbn_kanren is '事象伝票区分関連';
comment on column jishou_dpkbn_kanren.jishou_id is '事象ID';
comment on column jishou_dpkbn_kanren.denpyou_kbn is '伝票区分';
comment on column jishou_dpkbn_kanren.hyouji_jun is '表示順';
comment on column jishou_dpkbn_kanren.touroku_user_id is '登録ユーザーID';
comment on column jishou_dpkbn_kanren.touroku_time is '登録日時';
comment on column jishou_dpkbn_kanren.koushin_user_id is '更新ユーザーID';
comment on column jishou_dpkbn_kanren.koushin_time is '更新日時';

comment on table kaigai_koutsuu_shudan_master is '海外用交通手段マスター';
comment on column kaigai_koutsuu_shudan_master.sort_jun is '並び順';
comment on column kaigai_koutsuu_shudan_master.koutsuu_shudan is '交通手段';
comment on column kaigai_koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_koutsuu_shudan_master.zei_kubun is '税区分';
comment on column kaigai_koutsuu_shudan_master.edaban is '枝番コード';

comment on table kaigai_nittou_nado_master is '海外用日当等マスター';
comment on column kaigai_nittou_nado_master.shubetsu1 is '種別１';
comment on column kaigai_nittou_nado_master.shubetsu2 is '種別２';
comment on column kaigai_nittou_nado_master.yakushoku_cd is '役職コード';
comment on column kaigai_nittou_nado_master.tanka is '単価（邦貨）';
comment on column kaigai_nittou_nado_master.heishu_cd is '幣種コード';
comment on column kaigai_nittou_nado_master.currency_unit is '通貨単位';
comment on column kaigai_nittou_nado_master.tanka_gaika is '単価（外貨）';
comment on column kaigai_nittou_nado_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_nittou_nado_master.nittou_shukuhakuhi_flg is '日当・宿泊費フラグ';
comment on column kaigai_nittou_nado_master.zei_kubun is '税区分';
comment on column kaigai_nittou_nado_master.edaban is '枝番コード';

comment on table kaigai_ryohi_karibarai is '海外旅費仮払';
comment on column kaigai_ryohi_karibarai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai.karibarai_on is '仮払申請フラグ';
comment on column kaigai_ryohi_karibarai.dairiflg is '代理フラグ';
comment on column kaigai_ryohi_karibarai.user_id is 'ユーザーID';
comment on column kaigai_ryohi_karibarai.shain_no is '社員番号';
comment on column kaigai_ryohi_karibarai.user_sei is 'ユーザー姓';
comment on column kaigai_ryohi_karibarai.user_mei is 'ユーザー名';
comment on column kaigai_ryohi_karibarai.houmonsaki is '訪問先';
comment on column kaigai_ryohi_karibarai.mokuteki is '目的';
comment on column kaigai_ryohi_karibarai.seisankikan_from is '精算期間開始日';
comment on column kaigai_ryohi_karibarai.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column kaigai_ryohi_karibarai.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column kaigai_ryohi_karibarai.seisankikan_to is '精算期間終了日';
comment on column kaigai_ryohi_karibarai.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column kaigai_ryohi_karibarai.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column kaigai_ryohi_karibarai.shiharaibi is '支払日';
comment on column kaigai_ryohi_karibarai.shiharaikiboubi is '支払希望日';
comment on column kaigai_ryohi_karibarai.shiharaihouhou is '支払方法';
comment on column kaigai_ryohi_karibarai.tekiyou is '摘要';
comment on column kaigai_ryohi_karibarai.kingaku is '金額';
comment on column kaigai_ryohi_karibarai.karibarai_kingaku is '仮払金額';
comment on column kaigai_ryohi_karibarai.sashihiki_num is '差引回数';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka is '差引単価';
comment on column kaigai_ryohi_karibarai.sashihiki_num_kaigai is '差引回数（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai is '差引単価（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_heishu_cd_kaigai is '差引幣種コード（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_rate_kaigai is '差引レート（海外）';
comment on column kaigai_ryohi_karibarai.sashihiki_tanka_kaigai_gaika is '差引単価（海外）外貨';
comment on column kaigai_ryohi_karibarai.hf1_cd is 'HF1コード';
comment on column kaigai_ryohi_karibarai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column kaigai_ryohi_karibarai.hf2_cd is 'HF2コード';
comment on column kaigai_ryohi_karibarai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column kaigai_ryohi_karibarai.hf3_cd is 'HF3コード';
comment on column kaigai_ryohi_karibarai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column kaigai_ryohi_karibarai.hf4_cd is 'HF4コード';
comment on column kaigai_ryohi_karibarai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column kaigai_ryohi_karibarai.hf5_cd is 'HF5コード';
comment on column kaigai_ryohi_karibarai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column kaigai_ryohi_karibarai.hf6_cd is 'HF6コード';
comment on column kaigai_ryohi_karibarai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column kaigai_ryohi_karibarai.hf7_cd is 'HF7コード';
comment on column kaigai_ryohi_karibarai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column kaigai_ryohi_karibarai.hf8_cd is 'HF8コード';
comment on column kaigai_ryohi_karibarai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column kaigai_ryohi_karibarai.hf9_cd is 'HF9コード';
comment on column kaigai_ryohi_karibarai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column kaigai_ryohi_karibarai.hf10_cd is 'HF10コード';
comment on column kaigai_ryohi_karibarai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column kaigai_ryohi_karibarai.hosoku is '補足';
comment on column kaigai_ryohi_karibarai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohi_karibarai.torihiki_name is '取引名';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohi_karibarai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohi_karibarai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohi_karibarai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohi_karibarai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohi_karibarai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohi_karibarai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohi_karibarai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohi_karibarai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohi_karibarai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohi_karibarai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohi_karibarai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohi_karibarai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohi_karibarai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohi_karibarai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohi_karibarai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohi_karibarai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohi_karibarai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohi_karibarai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohi_karibarai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohi_karibarai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohi_karibarai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohi_karibarai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohi_karibarai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohi_karibarai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohi_karibarai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohi_karibarai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohi_karibarai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohi_karibarai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohi_karibarai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohi_karibarai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohi_karibarai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohi_karibarai.project_name is 'プロジェクト名';
comment on column kaigai_ryohi_karibarai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohi_karibarai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohi_karibarai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohi_karibarai.seisan_kanryoubi is '精算完了日';
comment on column kaigai_ryohi_karibarai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai.koushin_time is '更新日時';

comment on table kaigai_ryohi_karibarai_keihi_meisai is '海外旅費仮払経費明細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohi_karibarai_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohi_karibarai_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';

comment on table kaigai_ryohi_karibarai_meisai is '海外旅費仮払明細';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohi_karibarai_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohi_karibarai_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohi_karibarai_meisai.kikan_from is '期間開始日';
comment on column kaigai_ryohi_karibarai_meisai.kikan_to is '期間終了日';
comment on column kaigai_ryohi_karibarai_meisai.kyuujitsu_nissuu is '休日日数';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu_cd is '種別コード';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu1 is '種別１';
comment on column kaigai_ryohi_karibarai_meisai.shubetsu2 is '種別２';
comment on column kaigai_ryohi_karibarai_meisai.haya_flg is '早フラグ';
comment on column kaigai_ryohi_karibarai_meisai.yasu_flg is '安フラグ';
comment on column kaigai_ryohi_karibarai_meisai.raku_flg is '楽フラグ';
comment on column kaigai_ryohi_karibarai_meisai.koutsuu_shudan is '交通手段';
comment on column kaigai_ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_ryohi_karibarai_meisai.naiyou is '内容（旅費精算）';
comment on column kaigai_ryohi_karibarai_meisai.bikou is '備考（旅費精算）';
comment on column kaigai_ryohi_karibarai_meisai.oufuku_flg is '往復フラグ';
comment on column kaigai_ryohi_karibarai_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column kaigai_ryohi_karibarai_meisai.nissuu is '日数';
comment on column kaigai_ryohi_karibarai_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohi_karibarai_meisai.rate is 'レート';
comment on column kaigai_ryohi_karibarai_meisai.gaika is '外貨金額';
comment on column kaigai_ryohi_karibarai_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohi_karibarai_meisai.tanka is '単価';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column kaigai_ryohi_karibarai_meisai.suuryou is '数量';
comment on column kaigai_ryohi_karibarai_meisai.suuryou_kigou is '数量記号';
comment on column kaigai_ryohi_karibarai_meisai.meisai_kingaku is '明細金額';
comment on column kaigai_ryohi_karibarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohi_karibarai_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohi_karibarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohi_karibarai_meisai.koushin_time is '更新日時';

comment on table kaigai_ryohiseisan is '海外旅費精算';
comment on column kaigai_ryohiseisan.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan.karibarai_denpyou_id is '仮払伝票ID';
comment on column kaigai_ryohiseisan.karibarai_on is '仮払申請フラグ';
comment on column kaigai_ryohiseisan.karibarai_mishiyou_flg is '仮払金未使用フラグ';
comment on column kaigai_ryohiseisan.shucchou_chuushi_flg is '出張中止フラグ';
comment on column kaigai_ryohiseisan.dairiflg is '代理フラグ';
comment on column kaigai_ryohiseisan.user_id is 'ユーザーID';
comment on column kaigai_ryohiseisan.shain_no is '社員番号';
comment on column kaigai_ryohiseisan.user_sei is 'ユーザー姓';
comment on column kaigai_ryohiseisan.user_mei is 'ユーザー名';
comment on column kaigai_ryohiseisan.houmonsaki is '訪問先';
comment on column kaigai_ryohiseisan.mokuteki is '目的';
comment on column kaigai_ryohiseisan.seisankikan_from is '精算期間開始日';
comment on column kaigai_ryohiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column kaigai_ryohiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column kaigai_ryohiseisan.seisankikan_to is '精算期間終了日';
comment on column kaigai_ryohiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column kaigai_ryohiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column kaigai_ryohiseisan.keijoubi is '計上日';
comment on column kaigai_ryohiseisan.shiharaibi is '支払日';
comment on column kaigai_ryohiseisan.shiharaikiboubi is '支払希望日';
comment on column kaigai_ryohiseisan.shiharaihouhou is '支払方法';
comment on column kaigai_ryohiseisan.tekiyou is '摘要';
comment on column kaigai_ryohiseisan.kaigai_tekiyou is '摘要（海外）';
comment on column kaigai_ryohiseisan.zeiritsu is '税率';
comment on column kaigai_ryohiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohiseisan.goukei_kingaku is '合計金額';
comment on column kaigai_ryohiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column kaigai_ryohiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column kaigai_ryohiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column kaigai_ryohiseisan.sashihiki_num is '差引回数';
comment on column kaigai_ryohiseisan.sashihiki_tanka is '差引単価';
comment on column kaigai_ryohiseisan.sashihiki_num_kaigai is '差引回数（海外）';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai is '差引単価（海外）';
comment on column kaigai_ryohiseisan.sashihiki_heishu_cd_kaigai is '差引幣種コード（海外）';
comment on column kaigai_ryohiseisan.sashihiki_rate_kaigai is '差引レート（海外）';
comment on column kaigai_ryohiseisan.sashihiki_tanka_kaigai_gaika is '差引単価（海外）外貨';
comment on column kaigai_ryohiseisan.hf1_cd is 'HF1コード';
comment on column kaigai_ryohiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column kaigai_ryohiseisan.hf2_cd is 'HF2コード';
comment on column kaigai_ryohiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column kaigai_ryohiseisan.hf3_cd is 'HF3コード';
comment on column kaigai_ryohiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column kaigai_ryohiseisan.hf4_cd is 'HF4コード';
comment on column kaigai_ryohiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column kaigai_ryohiseisan.hf5_cd is 'HF5コード';
comment on column kaigai_ryohiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column kaigai_ryohiseisan.hf6_cd is 'HF6コード';
comment on column kaigai_ryohiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column kaigai_ryohiseisan.hf7_cd is 'HF7コード';
comment on column kaigai_ryohiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column kaigai_ryohiseisan.hf8_cd is 'HF8コード';
comment on column kaigai_ryohiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column kaigai_ryohiseisan.hf9_cd is 'HF9コード';
comment on column kaigai_ryohiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column kaigai_ryohiseisan.hf10_cd is 'HF10コード';
comment on column kaigai_ryohiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column kaigai_ryohiseisan.hosoku is '補足';
comment on column kaigai_ryohiseisan.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohiseisan.torihiki_name is '取引名';
comment on column kaigai_ryohiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohiseisan.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohiseisan.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohiseisan.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohiseisan.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohiseisan.ryohi_kazei_flg is '旅費課税フラグ';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohiseisan.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohiseisan.uf1_cd is 'UF1コード';
comment on column kaigai_ryohiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohiseisan.uf2_cd is 'UF2コード';
comment on column kaigai_ryohiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohiseisan.uf3_cd is 'UF3コード';
comment on column kaigai_ryohiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohiseisan.uf4_cd is 'UF4コード';
comment on column kaigai_ryohiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohiseisan.uf5_cd is 'UF5コード';
comment on column kaigai_ryohiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohiseisan.uf6_cd is 'UF6コード';
comment on column kaigai_ryohiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohiseisan.uf7_cd is 'UF7コード';
comment on column kaigai_ryohiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohiseisan.uf8_cd is 'UF8コード';
comment on column kaigai_ryohiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohiseisan.uf9_cd is 'UF9コード';
comment on column kaigai_ryohiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohiseisan.uf10_cd is 'UF10コード';
comment on column kaigai_ryohiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohiseisan.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohiseisan.project_name is 'プロジェクト名';
comment on column kaigai_ryohiseisan.segment_cd is 'セグメントコード';
comment on column kaigai_ryohiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohiseisan.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohiseisan.kaigai_shiwake_edano is '海外仕訳枝番号';
comment on column kaigai_ryohiseisan.kaigai_torihiki_name is '海外取引名';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_cd is '海外借方負担部門コード';
comment on column kaigai_ryohiseisan.kaigai_kari_futan_bumon_name is '海外借方負担部門名';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_cd is '海外取引先コード';
comment on column kaigai_ryohiseisan.kaigai_torihikisaki_name_ryakushiki is '海外取引先名（略式）';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_cd is '海外借方科目コード';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_name is '海外借方科目名';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_cd is '海外借方科目枝番コード';
comment on column kaigai_ryohiseisan.kaigai_kari_kamoku_edaban_name is '海外借方科目枝番名';
comment on column kaigai_ryohiseisan.kaigai_kari_kazei_kbn is '海外借方課税区分';
comment on column kaigai_ryohiseisan.kaigai_kazei_flg is '海外課税フラグ';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_cd is '海外貸方負担部門コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_futan_bumon_name is '海外貸方負担部門名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_cd is '海外貸方科目コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_name is '海外貸方科目名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_cd is '海外貸方科目枝番コード';
comment on column kaigai_ryohiseisan.kaigai_kashi_kamoku_edaban_name is '海外貸方科目枝番名';
comment on column kaigai_ryohiseisan.kaigai_kashi_kazei_kbn is '海外貸方課税区分';
comment on column kaigai_ryohiseisan.kaigai_uf1_cd is '海外UF1コード';
comment on column kaigai_ryohiseisan.kaigai_uf1_name_ryakushiki is '海外UF1名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf2_cd is '海外UF2コード';
comment on column kaigai_ryohiseisan.kaigai_uf2_name_ryakushiki is '海外UF2名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf3_cd is '海外UF3コード';
comment on column kaigai_ryohiseisan.kaigai_uf3_name_ryakushiki is '海外UF3名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf4_cd is '海外UF4コード';
comment on column kaigai_ryohiseisan.kaigai_uf4_name_ryakushiki is '海外UF4名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf5_cd is '海外UF5コード';
comment on column kaigai_ryohiseisan.kaigai_uf5_name_ryakushiki is '海外UF5名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf6_cd is '海外UF6コード';
comment on column kaigai_ryohiseisan.kaigai_uf6_name_ryakushiki is '海外UF6名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf7_cd is '海外UF7コード';
comment on column kaigai_ryohiseisan.kaigai_uf7_name_ryakushiki is '海外UF7名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf8_cd is '海外UF8コード';
comment on column kaigai_ryohiseisan.kaigai_uf8_name_ryakushiki is '海外UF8名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf9_cd is '海外UF9コード';
comment on column kaigai_ryohiseisan.kaigai_uf9_name_ryakushiki is '海外UF9名（略式）';
comment on column kaigai_ryohiseisan.kaigai_uf10_cd is '海外UF10コード';
comment on column kaigai_ryohiseisan.kaigai_uf10_name_ryakushiki is '海外UF10名（略式）';
comment on column kaigai_ryohiseisan.kaigai_project_cd is '海外プロジェクトコード';
comment on column kaigai_ryohiseisan.kaigai_project_name is '海外プロジェクト名';
comment on column kaigai_ryohiseisan.kaigai_segment_cd is '海外セグメントコード';
comment on column kaigai_ryohiseisan.kaigai_segment_name_ryakushiki is '海外セグメント名（略式）';
comment on column kaigai_ryohiseisan.kaigai_tekiyou_cd is '海外摘要コード';
comment on column kaigai_ryohiseisan.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan.koushin_time is '更新日時';
comment on column kaigai_ryohiseisan.bunri_kbn is '分離区分';
comment on column kaigai_ryohiseisan.kari_shiire_kbn is '借方仕入区分';
comment on column kaigai_ryohiseisan.kashi_shiire_kbn is '貸方仕入区分';
comment on column kaigai_ryohiseisan.kaigai_bunri_kbn is '海外分離区分';
comment on column kaigai_ryohiseisan.kaigai_kari_shiire_kbn is '海外借方仕入区分';
comment on column kaigai_ryohiseisan.kaigai_kashi_shiire_kbn is '海外貸方仕入区分';
comment on column kaigai_ryohiseisan.invoice_denpyou is 'インボイス対応伝票';

comment on table kaigai_ryohiseisan_keihi_meisai is '海外旅費精算経費明細';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column kaigai_ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column kaigai_ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column kaigai_ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column kaigai_ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column kaigai_ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column kaigai_ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column kaigai_ryohiseisan_keihi_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohiseisan_keihi_meisai.rate is 'レート';
comment on column kaigai_ryohiseisan_keihi_meisai.gaika is '外貨金額';
comment on column kaigai_ryohiseisan_keihi_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column kaigai_ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column kaigai_ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column kaigai_ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column kaigai_ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column kaigai_ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column kaigai_ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan_keihi_meisai.koushin_time is '更新日時';
comment on column kaigai_ryohiseisan_keihi_meisai.shiharaisaki_name is '支払先名';
comment on column kaigai_ryohiseisan_keihi_meisai.jigyousha_kbn is '事業者区分';
comment on column kaigai_ryohiseisan_keihi_meisai.bunri_kbn is '分離区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column kaigai_ryohiseisan_keihi_meisai.kashi_shiire_kbn is '貸方仕入区分';

comment on table kaigai_ryohiseisan_meisai is '海外旅費精算明細';
comment on column kaigai_ryohiseisan_meisai.denpyou_id is '伝票ID';
comment on column kaigai_ryohiseisan_meisai.kaigai_flg is '海外フラグ';
comment on column kaigai_ryohiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column kaigai_ryohiseisan_meisai.kikan_from is '期間開始日';
comment on column kaigai_ryohiseisan_meisai.kikan_to is '期間終了日';
comment on column kaigai_ryohiseisan_meisai.kyuujitsu_nissuu is '休日日数';
comment on column kaigai_ryohiseisan_meisai.shubetsu_cd is '種別コード';
comment on column kaigai_ryohiseisan_meisai.shubetsu1 is '種別１';
comment on column kaigai_ryohiseisan_meisai.shubetsu2 is '種別２';
comment on column kaigai_ryohiseisan_meisai.haya_flg is '早フラグ';
comment on column kaigai_ryohiseisan_meisai.yasu_flg is '安フラグ';
comment on column kaigai_ryohiseisan_meisai.raku_flg is '楽フラグ';
comment on column kaigai_ryohiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column kaigai_ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column kaigai_ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column kaigai_ryohiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column kaigai_ryohiseisan_meisai.bikou is '備考（旅費精算）';
comment on column kaigai_ryohiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column kaigai_ryohiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column kaigai_ryohiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column kaigai_ryohiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column kaigai_ryohiseisan_meisai.nissuu is '日数';
comment on column kaigai_ryohiseisan_meisai.heishu_cd is '幣種コード';
comment on column kaigai_ryohiseisan_meisai.rate is 'レート';
comment on column kaigai_ryohiseisan_meisai.gaika is '外貨金額';
comment on column kaigai_ryohiseisan_meisai.currency_unit is '通貨単位';
comment on column kaigai_ryohiseisan_meisai.tanka is '単価';
comment on column kaigai_ryohiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column kaigai_ryohiseisan_meisai.suuryou is '数量';
comment on column kaigai_ryohiseisan_meisai.suuryou_kigou is '数量記号';
comment on column kaigai_ryohiseisan_meisai.meisai_kingaku is '明細金額';
comment on column kaigai_ryohiseisan_meisai.zei_kubun is '税区分';
comment on column kaigai_ryohiseisan_meisai.kazei_flg is '課税フラグ';
comment on column kaigai_ryohiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column kaigai_ryohiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column kaigai_ryohiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column kaigai_ryohiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column kaigai_ryohiseisan_meisai.touroku_time is '登録日時';
comment on column kaigai_ryohiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column kaigai_ryohiseisan_meisai.koushin_time is '更新日時';
comment on column kaigai_ryohiseisan_meisai.shiharaisaki_name is '支払先名';
comment on column kaigai_ryohiseisan_meisai.jigyousha_kbn is '事業者区分';
comment on column kaigai_ryohiseisan_meisai.zeinuki_kingaku is '税抜金額';
comment on column kaigai_ryohiseisan_meisai.shouhizeigaku is '消費税額';
comment on column kaigai_ryohiseisan_meisai.zeigaku_fix_flg is '税額修正フラグ';

comment on table kaisha_info is '会社情報';
comment on column kaisha_info.kessanki_bangou is '決算期番号';
comment on column kaisha_info.hf1_shiyou_flg is 'HF1使用フラグ';
comment on column kaisha_info.hf1_hissu_flg is 'HF1必須フラグ';
comment on column kaisha_info.hf1_name is 'HF1名';
comment on column kaisha_info.hf2_shiyou_flg is 'HF2使用フラグ';
comment on column kaisha_info.hf2_hissu_flg is 'HF2必須フラグ';
comment on column kaisha_info.hf2_name is 'HF2名';
comment on column kaisha_info.hf3_shiyou_flg is 'HF3使用フラグ';
comment on column kaisha_info.hf3_hissu_flg is 'HF3必須フラグ';
comment on column kaisha_info.hf3_name is 'HF3名';
comment on column kaisha_info.hf4_shiyou_flg is 'HF4使用フラグ';
comment on column kaisha_info.hf4_hissu_flg is 'HF4必須フラグ';
comment on column kaisha_info.hf4_name is 'HF4名';
comment on column kaisha_info.hf5_shiyou_flg is 'HF5使用フラグ';
comment on column kaisha_info.hf5_hissu_flg is 'HF5必須フラグ';
comment on column kaisha_info.hf5_name is 'HF5名';
comment on column kaisha_info.hf6_shiyou_flg is 'HF6使用フラグ';
comment on column kaisha_info.hf6_hissu_flg is 'HF6必須フラグ';
comment on column kaisha_info.hf6_name is 'HF6名';
comment on column kaisha_info.hf7_shiyou_flg is 'HF7使用フラグ';
comment on column kaisha_info.hf7_hissu_flg is 'HF7必須フラグ';
comment on column kaisha_info.hf7_name is 'HF7名';
comment on column kaisha_info.hf8_shiyou_flg is 'HF8使用フラグ';
comment on column kaisha_info.hf8_hissu_flg is 'HF8必須フラグ';
comment on column kaisha_info.hf8_name is 'HF8名';
comment on column kaisha_info.hf9_shiyou_flg is 'HF9使用フラグ';
comment on column kaisha_info.hf9_hissu_flg is 'HF9必須フラグ';
comment on column kaisha_info.hf9_name is 'HF9名';
comment on column kaisha_info.hf10_shiyou_flg is 'HF10使用フラグ';
comment on column kaisha_info.hf10_hissu_flg is 'HF10必須フラグ';
comment on column kaisha_info.hf10_name is 'HF10名';
comment on column kaisha_info.uf1_shiyou_flg is 'UF1使用フラグ';
comment on column kaisha_info.uf1_name is 'UF1名';
comment on column kaisha_info.uf2_shiyou_flg is 'UF2使用フラグ';
comment on column kaisha_info.uf2_name is 'UF2名';
comment on column kaisha_info.uf3_shiyou_flg is 'UF3使用フラグ';
comment on column kaisha_info.uf3_name is 'UF3名';
comment on column kaisha_info.uf4_shiyou_flg is 'UF4使用フラグ';
comment on column kaisha_info.uf4_name is 'UF4名';
comment on column kaisha_info.uf5_shiyou_flg is 'UF5使用フラグ';
comment on column kaisha_info.uf5_name is 'UF5名';
comment on column kaisha_info.uf6_shiyou_flg is 'UF6使用フラグ';
comment on column kaisha_info.uf6_name is 'UF6名';
comment on column kaisha_info.uf7_shiyou_flg is 'UF7使用フラグ';
comment on column kaisha_info.uf7_name is 'UF7名';
comment on column kaisha_info.uf8_shiyou_flg is 'UF8使用フラグ';
comment on column kaisha_info.uf8_name is 'UF8名';
comment on column kaisha_info.uf9_shiyou_flg is 'UF9使用フラグ';
comment on column kaisha_info.uf9_name is 'UF9名';
comment on column kaisha_info.uf10_shiyou_flg is 'UF10使用フラグ';
comment on column kaisha_info.uf10_name is 'UF10名';
comment on column kaisha_info.uf_kotei1_shiyou_flg is 'UF1使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei1_name is 'UF1名(固定値)';
comment on column kaisha_info.uf_kotei2_shiyou_flg is 'UF2使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei2_name is 'UF2名(固定値)';
comment on column kaisha_info.uf_kotei3_shiyou_flg is 'UF3使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei3_name is 'UF3名(固定値)';
comment on column kaisha_info.uf_kotei4_shiyou_flg is 'UF4使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei4_name is 'UF4名(固定値)';
comment on column kaisha_info.uf_kotei5_shiyou_flg is 'UF5使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei5_name is 'UF5名(固定値)';
comment on column kaisha_info.uf_kotei6_shiyou_flg is 'UF6使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei6_name is 'UF6名(固定値)';
comment on column kaisha_info.uf_kotei7_shiyou_flg is 'UF7使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei7_name is 'UF7名(固定値)';
comment on column kaisha_info.uf_kotei8_shiyou_flg is 'UF8使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei8_name is 'UF8名(固定値)';
comment on column kaisha_info.uf_kotei9_shiyou_flg is 'UF9使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei9_name is 'UF9名(固定値)';
comment on column kaisha_info.uf_kotei10_shiyou_flg is 'UF10使用フラグ(固定値)';
comment on column kaisha_info.uf_kotei10_name is 'UF10名(固定値)';
comment on column kaisha_info.pjcd_shiyou_flg is 'プロジェクトコード使用フラグ';
comment on column kaisha_info.sgcd_shiyou_flg is 'セグメントコード使用フラグ';
comment on column kaisha_info.saimu_shiyou_flg is '債務使用フラグ';
comment on column kaisha_info.kamoku_cd_type is '科目コードタイプ';
comment on column kaisha_info.kamoku_edaban_cd_type is '科目枝番コードタイプ';
comment on column kaisha_info.futan_bumon_cd_type is '負担部門コードタイプ';
comment on column kaisha_info.torihikisaki_cd_type is '取引先コードタイプ';
comment on column kaisha_info.segment_cd_type is 'セグメントコードタイプ';
comment on column kaisha_info.houjin_bangou is '法人番号';
comment on column kaisha_info.keigen_umu_flg is '軽減有無フラグ';

comment on table kamoku_edaban_zandaka is '科目枝番残高';
comment on column kamoku_edaban_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column kamoku_edaban_zandaka.kamoku_edaban_cd is '科目枝番コード';
comment on column kamoku_edaban_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column kamoku_edaban_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column kamoku_edaban_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column kamoku_edaban_zandaka.edaban_name is '枝番名';
comment on column kamoku_edaban_zandaka.kessanki_bangou is '決算期番号';
comment on column kamoku_edaban_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column kamoku_edaban_zandaka.taishaku_zokusei is '貸借属性';
comment on column kamoku_edaban_zandaka.kishu_zandaka is '期首残高';

comment on table kamoku_master is '科目マスター';
comment on column kamoku_master.kamoku_gaibu_cd is '科目外部コード';
comment on column kamoku_master.kamoku_naibu_cd is '科目内部コード';
comment on column kamoku_master.kamoku_name_ryakushiki is '科目名（略式）';
comment on column kamoku_master.kamoku_name_seishiki is '科目名（正式）';
comment on column kamoku_master.kessanki_bangou is '決算期番号';
comment on column kamoku_master.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column kamoku_master.taishaku_zokusei is '貸借属性';
comment on column kamoku_master.kamoku_group_kbn is '科目グループ区分';
comment on column kamoku_master.kamoku_group_bangou is '科目グループ番号';
comment on column kamoku_master.shori_group is '処理グループ';
comment on column kamoku_master.taikakingaku_nyuuryoku_flg is '対価金額入力フラグ';
comment on column kamoku_master.kazei_kbn is '課税区分';
comment on column kamoku_master.bunri_kbn is '分離区分';
comment on column kamoku_master.shiire_kbn is '仕入区分';
comment on column kamoku_master.gyousha_kbn is '業種区分';
comment on column kamoku_master.zeiritsu_kbn is '税率区分';
comment on column kamoku_master.tokusha_hyouji_kbn is '特殊表示区分';
comment on column kamoku_master.edaban_minyuuryoku_check is '枝番未入力チェック';
comment on column kamoku_master.torihikisaki_minyuuryoku_check is '取引先未入力チェック';
comment on column kamoku_master.bumon_minyuuryoku_check is '部門未入力チェック';
comment on column kamoku_master.bumon_edaban_flg is '部門科目枝番使用フラグ';
comment on column kamoku_master.segment_minyuuryoku_check is 'セグメント未入力チェック';
comment on column kamoku_master.project_minyuuryoku_check is 'プロジェクト未入力チェック';
comment on column kamoku_master.uf1_minyuuryoku_check is 'ユニバーサルフィールド１未入力チェック';
comment on column kamoku_master.uf2_minyuuryoku_check is 'ユニバーサルフィールド２未入力チェック';
comment on column kamoku_master.uf3_minyuuryoku_check is 'ユニバーサルフィールド３未入力チェック';
comment on column kamoku_master.uf4_minyuuryoku_check is 'ユニバーサルフィールド４未入力チェック';
comment on column kamoku_master.uf5_minyuuryoku_check is 'ユニバーサルフィールド５未入力チェック';
comment on column kamoku_master.uf6_minyuuryoku_check is 'ユニバーサルフィールド６未入力チェック';
comment on column kamoku_master.uf7_minyuuryoku_check is 'ユニバーサルフィールド７未入力チェック';
comment on column kamoku_master.uf8_minyuuryoku_check is 'ユニバーサルフィールド８未入力チェック';
comment on column kamoku_master.uf9_minyuuryoku_check is 'ユニバーサルフィールド９未入力チェック';
comment on column kamoku_master.uf10_minyuuryoku_check is 'ユニバーサルフィールド１０未入力チェック';
comment on column kamoku_master.uf_kotei1_minyuuryoku_check is 'ユニバーサルフィールド１未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei2_minyuuryoku_check is 'ユニバーサルフィールド２未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei3_minyuuryoku_check is 'ユニバーサルフィールド３未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei4_minyuuryoku_check is 'ユニバーサルフィールド４未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei5_minyuuryoku_check is 'ユニバーサルフィールド５未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei6_minyuuryoku_check is 'ユニバーサルフィールド６未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei7_minyuuryoku_check is 'ユニバーサルフィールド７未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei8_minyuuryoku_check is 'ユニバーサルフィールド８未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei9_minyuuryoku_check is 'ユニバーサルフィールド９未入力チェック(固定値)';
comment on column kamoku_master.uf_kotei10_minyuuryoku_check is 'ユニバーサルフィールド１０未入力チェック(固定値)';
comment on column kamoku_master.kouji_minyuuryoku_check is '工事未入力チェック';
comment on column kamoku_master.kousha_minyuuryoku_check is '工種未入力チェック';
comment on column kamoku_master.tekiyou_cd_minyuuryoku_check is '摘要コード未入力チェック';
comment on column kamoku_master.bumon_torhikisaki_kamoku_flg is '部門取引先科目使用フラグ';
comment on column kamoku_master.bumon_torihikisaki_edaban_shiyou_flg is '部門取引先科目枝番使用フラグ';
comment on column kamoku_master.torihikisaki_kamoku_edaban_flg is '取引先科目枝番使用フラグ';
comment on column kamoku_master.segment_torihikisaki_kamoku_flg is 'セグメント取引先科目使用フラグ';
comment on column kamoku_master.karikanjou_keshikomi_no_flg is '仮勘定消込No使用フラグ';
comment on column kamoku_master.gaika_flg is '外貨使用フラグ';

comment on table kani_todoke is '届出ジェネレータ';
comment on column kani_todoke.denpyou_id is '伝票ID';
comment on column kani_todoke.denpyou_kbn is '伝票区分';
comment on column kani_todoke.version is 'バージョン';
comment on column kani_todoke.item_name is '項目名';
comment on column kani_todoke.value1 is '値１';
comment on column kani_todoke.value2 is '値２';
comment on column kani_todoke.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke.touroku_time is '登録日時';
comment on column kani_todoke.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke.koushin_time is '更新日時';

comment on table kani_todoke_checkbox is '届出ジェネレータ項目チェックボックス';
comment on column kani_todoke_checkbox.denpyou_kbn is '伝票区分';
comment on column kani_todoke_checkbox.version is 'バージョン';
comment on column kani_todoke_checkbox.area_kbn is 'エリア区分';
comment on column kani_todoke_checkbox.item_name is '項目名';
comment on column kani_todoke_checkbox.label_name is 'ラベル名';
comment on column kani_todoke_checkbox.hissu_flg is '必須フラグ';
comment on column kani_todoke_checkbox.checkbox_label_name is 'チェックボックスラベル名';
comment on column kani_todoke_checkbox.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_checkbox.touroku_time is '登録日時';
comment on column kani_todoke_checkbox.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_checkbox.koushin_time is '更新日時';

comment on table kani_todoke_ichiran is '届出ジェネレータ一覧';
comment on column kani_todoke_ichiran.denpyou_id is '伝票ID';
comment on column kani_todoke_ichiran.shinsei01 is '申請内容01';
comment on column kani_todoke_ichiran.shinsei02 is '申請内容02';
comment on column kani_todoke_ichiran.shinsei03 is '申請内容03';
comment on column kani_todoke_ichiran.shinsei04 is '申請内容04';
comment on column kani_todoke_ichiran.shinsei05 is '申請内容05';
comment on column kani_todoke_ichiran.shinsei06 is '申請内容06';
comment on column kani_todoke_ichiran.shinsei07 is '申請内容07';
comment on column kani_todoke_ichiran.shinsei08 is '申請内容08';
comment on column kani_todoke_ichiran.shinsei09 is '申請内容09';
comment on column kani_todoke_ichiran.shinsei10 is '申請内容10';
comment on column kani_todoke_ichiran.shinsei11 is '申請内容11';
comment on column kani_todoke_ichiran.shinsei12 is '申請内容12';
comment on column kani_todoke_ichiran.shinsei13 is '申請内容13';
comment on column kani_todoke_ichiran.shinsei14 is '申請内容14';
comment on column kani_todoke_ichiran.shinsei15 is '申請内容15';
comment on column kani_todoke_ichiran.shinsei16 is '申請内容16';
comment on column kani_todoke_ichiran.shinsei17 is '申請内容17';
comment on column kani_todoke_ichiran.shinsei18 is '申請内容18';
comment on column kani_todoke_ichiran.shinsei19 is '申請内容19';
comment on column kani_todoke_ichiran.shinsei20 is '申請内容20';
comment on column kani_todoke_ichiran.shinsei21 is '申請内容21';
comment on column kani_todoke_ichiran.shinsei22 is '申請内容22';
comment on column kani_todoke_ichiran.shinsei23 is '申請内容23';
comment on column kani_todoke_ichiran.shinsei24 is '申請内容24';
comment on column kani_todoke_ichiran.shinsei25 is '申請内容25';
comment on column kani_todoke_ichiran.shinsei26 is '申請内容26';
comment on column kani_todoke_ichiran.shinsei27 is '申請内容27';
comment on column kani_todoke_ichiran.shinsei28 is '申請内容28';
comment on column kani_todoke_ichiran.shinsei29 is '申請内容29';
comment on column kani_todoke_ichiran.shinsei30 is '申請内容30';

comment on table kani_todoke_koumoku is '届出ジェネレータ項目定義';
comment on column kani_todoke_koumoku.denpyou_kbn is '伝票区分';
comment on column kani_todoke_koumoku.version is 'バージョン';
comment on column kani_todoke_koumoku.area_kbn is 'エリア区分';
comment on column kani_todoke_koumoku.item_name is '項目名';
comment on column kani_todoke_koumoku.hyouji_jun is '表示順';
comment on column kani_todoke_koumoku.buhin_type is '部品タイプ';
comment on column kani_todoke_koumoku.default_value1 is 'デフォルト値１';
comment on column kani_todoke_koumoku.default_value2 is 'デフォルト値２';
comment on column kani_todoke_koumoku.yosan_shikkou_koumoku_id is '予算執行項目ID';
comment on column kani_todoke_koumoku.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_koumoku.touroku_time is '登録日時';
comment on column kani_todoke_koumoku.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_koumoku.koushin_time is '更新日時';

comment on table kani_todoke_list_ko is '届出ジェネレータ項目リスト子';
comment on column kani_todoke_list_ko.denpyou_kbn is '伝票区分';
comment on column kani_todoke_list_ko.version is 'バージョン';
comment on column kani_todoke_list_ko.area_kbn is 'エリア区分';
comment on column kani_todoke_list_ko.item_name is '項目名';
comment on column kani_todoke_list_ko.hyouji_jun is '表示順';
comment on column kani_todoke_list_ko.text is 'テキスト';
comment on column kani_todoke_list_ko.value is '値';
comment on column kani_todoke_list_ko.select_item is '選択項目';
comment on column kani_todoke_list_ko.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_list_ko.touroku_time is '登録日時';
comment on column kani_todoke_list_ko.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_list_ko.koushin_time is '更新日時';

comment on table kani_todoke_list_oya is '届出ジェネレータ項目リスト親';
comment on column kani_todoke_list_oya.denpyou_kbn is '伝票区分';
comment on column kani_todoke_list_oya.version is 'バージョン';
comment on column kani_todoke_list_oya.area_kbn is 'エリア区分';
comment on column kani_todoke_list_oya.item_name is '項目名';
comment on column kani_todoke_list_oya.label_name is 'ラベル名';
comment on column kani_todoke_list_oya.hissu_flg is '必須フラグ';
comment on column kani_todoke_list_oya.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_list_oya.touroku_time is '登録日時';
comment on column kani_todoke_list_oya.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_list_oya.koushin_time is '更新日時';

comment on table kani_todoke_master is '届出ジェネレータ項目マスター';
comment on column kani_todoke_master.denpyou_kbn is '伝票区分';
comment on column kani_todoke_master.version is 'バージョン';
comment on column kani_todoke_master.area_kbn is 'エリア区分';
comment on column kani_todoke_master.item_name is '項目名';
comment on column kani_todoke_master.label_name is 'ラベル名';
comment on column kani_todoke_master.hissu_flg is '必須フラグ';
comment on column kani_todoke_master.master_kbn is 'マスター区分';
comment on column kani_todoke_master.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_master.touroku_time is '登録日時';
comment on column kani_todoke_master.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_master.koushin_time is '更新日時';

comment on table kani_todoke_meisai is '届出ジェネレータ明細';
comment on column kani_todoke_meisai.denpyou_id is '伝票ID';
comment on column kani_todoke_meisai.denpyou_edano is '伝票枝番号';
comment on column kani_todoke_meisai.item_name is '項目名';
comment on column kani_todoke_meisai.value1 is '値１';
comment on column kani_todoke_meisai.value2 is '値２';
comment on column kani_todoke_meisai.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_meisai.touroku_time is '登録日時';
comment on column kani_todoke_meisai.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_meisai.koushin_time is '更新日時';

comment on table kani_todoke_meisai_ichiran is '届出ジェネレータ明細一覧';
comment on column kani_todoke_meisai_ichiran.denpyou_id is '伝票ID';
comment on column kani_todoke_meisai_ichiran.denpyou_edano is '伝票枝番号';
comment on column kani_todoke_meisai_ichiran.meisai01 is '明細01';
comment on column kani_todoke_meisai_ichiran.meisai02 is '明細02';
comment on column kani_todoke_meisai_ichiran.meisai03 is '明細03';
comment on column kani_todoke_meisai_ichiran.meisai04 is '明細04';
comment on column kani_todoke_meisai_ichiran.meisai05 is '明細05';
comment on column kani_todoke_meisai_ichiran.meisai06 is '明細06';
comment on column kani_todoke_meisai_ichiran.meisai07 is '明細07';
comment on column kani_todoke_meisai_ichiran.meisai08 is '明細08';
comment on column kani_todoke_meisai_ichiran.meisai09 is '明細09';
comment on column kani_todoke_meisai_ichiran.meisai10 is '明細10';
comment on column kani_todoke_meisai_ichiran.meisai11 is '明細11';
comment on column kani_todoke_meisai_ichiran.meisai12 is '明細12';
comment on column kani_todoke_meisai_ichiran.meisai13 is '明細13';
comment on column kani_todoke_meisai_ichiran.meisai14 is '明細14';
comment on column kani_todoke_meisai_ichiran.meisai15 is '明細15';
comment on column kani_todoke_meisai_ichiran.meisai16 is '明細16';
comment on column kani_todoke_meisai_ichiran.meisai17 is '明細17';
comment on column kani_todoke_meisai_ichiran.meisai18 is '明細18';
comment on column kani_todoke_meisai_ichiran.meisai19 is '明細19';
comment on column kani_todoke_meisai_ichiran.meisai20 is '明細20';
comment on column kani_todoke_meisai_ichiran.meisai21 is '明細21';
comment on column kani_todoke_meisai_ichiran.meisai22 is '明細22';
comment on column kani_todoke_meisai_ichiran.meisai23 is '明細23';
comment on column kani_todoke_meisai_ichiran.meisai24 is '明細24';
comment on column kani_todoke_meisai_ichiran.meisai25 is '明細25';
comment on column kani_todoke_meisai_ichiran.meisai26 is '明細26';
comment on column kani_todoke_meisai_ichiran.meisai27 is '明細27';
comment on column kani_todoke_meisai_ichiran.meisai28 is '明細28';
comment on column kani_todoke_meisai_ichiran.meisai29 is '明細29';
comment on column kani_todoke_meisai_ichiran.meisai30 is '明細30';

comment on table kani_todoke_meta is '届出ジェネレータメタ';
comment on column kani_todoke_meta.denpyou_kbn is '伝票区分';
comment on column kani_todoke_meta.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_meta.touroku_time is '登録日時';
comment on column kani_todoke_meta.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_meta.koushin_time is '更新日時';

comment on table kani_todoke_select_item is '届出ジェネレータ選択項目';
comment on column kani_todoke_select_item.select_item is '選択項目';
comment on column kani_todoke_select_item.cd is 'コード';
comment on column kani_todoke_select_item.name is '名称';

comment on table kani_todoke_summary is '届出ジェネレータサマリ';
comment on column kani_todoke_summary.denpyou_id is '伝票ID';
comment on column kani_todoke_summary.ringi_kingaku is '稟議金額';
comment on column kani_todoke_summary.shishutsu_kingaku_goukei is '支出金額合計';
comment on column kani_todoke_summary.shuunyuu_kingaku_goukei is '収入金額合計';
comment on column kani_todoke_summary.kenmei is '件名';
comment on column kani_todoke_summary.naiyou is '内容';

comment on table kani_todoke_text is '届出ジェネレータ項目テキスト';
comment on column kani_todoke_text.denpyou_kbn is '伝票区分';
comment on column kani_todoke_text.version is 'バージョン';
comment on column kani_todoke_text.area_kbn is 'エリア区分';
comment on column kani_todoke_text.item_name is '項目名';
comment on column kani_todoke_text.label_name is 'ラベル名';
comment on column kani_todoke_text.hissu_flg is '必須フラグ';
comment on column kani_todoke_text.buhin_format is '部品形式';
comment on column kani_todoke_text.buhin_width is '部品幅';
comment on column kani_todoke_text.max_length is '最大桁数';
comment on column kani_todoke_text.decimal_point is '小数点以下桁数';
comment on column kani_todoke_text.kotei_hyouji is '固定表示フラグ';
comment on column kani_todoke_text.min_value is '最小値';
comment on column kani_todoke_text.max_value is '最大値';
comment on column kani_todoke_text.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_text.touroku_time is '登録日時';
comment on column kani_todoke_text.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_text.koushin_time is '更新日時';

comment on table kani_todoke_textarea is '届出ジェネレータ項目テキストエリア';
comment on column kani_todoke_textarea.denpyou_kbn is '伝票区分';
comment on column kani_todoke_textarea.version is 'バージョン';
comment on column kani_todoke_textarea.area_kbn is 'エリア区分';
comment on column kani_todoke_textarea.item_name is '項目名';
comment on column kani_todoke_textarea.label_name is 'ラベル名';
comment on column kani_todoke_textarea.hissu_flg is '必須フラグ';
comment on column kani_todoke_textarea.buhin_width is '部品幅';
comment on column kani_todoke_textarea.buhin_height is '部品高さ';
comment on column kani_todoke_textarea.max_length is '最大桁数';
comment on column kani_todoke_textarea.kotei_hyouji is '固定表示フラグ';
comment on column kani_todoke_textarea.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_textarea.touroku_time is '登録日時';
comment on column kani_todoke_textarea.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_textarea.koushin_time is '更新日時';

comment on table kani_todoke_version is '届出ジェネレータバージョン';
comment on column kani_todoke_version.denpyou_kbn is '伝票区分';
comment on column kani_todoke_version.version is 'バージョン';
comment on column kani_todoke_version.denpyou_shubetsu is '伝票種別';
comment on column kani_todoke_version.naiyou is '内容（伝票）';
comment on column kani_todoke_version.touroku_user_id is '登録ユーザーID';
comment on column kani_todoke_version.touroku_time is '登録日時';
comment on column kani_todoke_version.koushin_user_id is '更新ユーザーID';
comment on column kani_todoke_version.koushin_time is '更新日時';

comment on table kanren_denpyou is '関連伝票';
comment on column kanren_denpyou.denpyou_id is '伝票ID';
comment on column kanren_denpyou.kanren_denpyou is '関連伝票';
comment on column kanren_denpyou.kanren_denpyou_kbn is '関連伝票区分';
comment on column kanren_denpyou.kanren_denpyou_kihyoubi is '関連伝票起票日';
comment on column kanren_denpyou.kanren_denpyou_shouninbi is '関連伝票承認日';

comment on table karibarai is '仮払';
comment on column karibarai.denpyou_id is '伝票ID';
comment on column karibarai.seisan_yoteibi is '精算予定日';
comment on column karibarai.seisan_kanryoubi is '精算完了日';
comment on column karibarai.shiharaibi is '支払日';
comment on column karibarai.karibarai_on is '仮払申請フラグ';
comment on column karibarai.shiharaikiboubi is '支払希望日';
comment on column karibarai.shiharaihouhou is '支払方法';
comment on column karibarai.tekiyou is '摘要';
comment on column karibarai.kingaku is '金額';
comment on column karibarai.karibarai_kingaku is '仮払金額';
comment on column karibarai.hf1_cd is 'HF1コード';
comment on column karibarai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column karibarai.hf2_cd is 'HF2コード';
comment on column karibarai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column karibarai.hf3_cd is 'HF3コード';
comment on column karibarai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column karibarai.hf4_cd is 'HF4コード';
comment on column karibarai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column karibarai.hf5_cd is 'HF5コード';
comment on column karibarai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column karibarai.hf6_cd is 'HF6コード';
comment on column karibarai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column karibarai.hf7_cd is 'HF7コード';
comment on column karibarai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column karibarai.hf8_cd is 'HF8コード';
comment on column karibarai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column karibarai.hf9_cd is 'HF9コード';
comment on column karibarai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column karibarai.hf10_cd is 'HF10コード';
comment on column karibarai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column karibarai.hosoku is '補足';
comment on column karibarai.shiwake_edano is '仕訳枝番号';
comment on column karibarai.torihiki_name is '取引名';
comment on column karibarai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column karibarai.kari_futan_bumon_name is '借方負担部門名';
comment on column karibarai.torihikisaki_cd is '取引先コード';
comment on column karibarai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column karibarai.kari_kamoku_cd is '借方科目コード';
comment on column karibarai.kari_kamoku_name is '借方科目名';
comment on column karibarai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column karibarai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column karibarai.kari_kazei_kbn is '借方課税区分';
comment on column karibarai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column karibarai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column karibarai.kashi_kamoku_cd is '貸方科目コード';
comment on column karibarai.kashi_kamoku_name is '貸方科目名';
comment on column karibarai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column karibarai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column karibarai.kashi_kazei_kbn is '貸方課税区分';
comment on column karibarai.uf1_cd is 'UF1コード';
comment on column karibarai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column karibarai.uf2_cd is 'UF2コード';
comment on column karibarai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column karibarai.uf3_cd is 'UF3コード';
comment on column karibarai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column karibarai.uf4_cd is 'UF4コード';
comment on column karibarai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column karibarai.uf5_cd is 'UF5コード';
comment on column karibarai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column karibarai.uf6_cd is 'UF6コード';
comment on column karibarai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column karibarai.uf7_cd is 'UF7コード';
comment on column karibarai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column karibarai.uf8_cd is 'UF8コード';
comment on column karibarai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column karibarai.uf9_cd is 'UF9コード';
comment on column karibarai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column karibarai.uf10_cd is 'UF10コード';
comment on column karibarai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column karibarai.project_cd is 'プロジェクトコード';
comment on column karibarai.project_name is 'プロジェクト名';
comment on column karibarai.segment_cd is 'セグメントコード';
comment on column karibarai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column karibarai.tekiyou_cd is '摘要コード';
comment on column karibarai.touroku_user_id is '登録ユーザーID';
comment on column karibarai.touroku_time is '登録日時';
comment on column karibarai.koushin_user_id is '更新ユーザーID';
comment on column karibarai.koushin_time is '更新日時';

comment on table keihiseisan is '経費精算';
comment on column keihiseisan.denpyou_id is '伝票ID';
comment on column keihiseisan.karibarai_denpyou_id is '仮払伝票ID';
comment on column keihiseisan.karibarai_on is '仮払申請フラグ';
comment on column keihiseisan.karibarai_mishiyou_flg is '仮払金未使用フラグ';
comment on column keihiseisan.dairiflg is '代理フラグ';
comment on column keihiseisan.keijoubi is '計上日';
comment on column keihiseisan.shiharaibi is '支払日';
comment on column keihiseisan.shiharaikiboubi is '支払希望日';
comment on column keihiseisan.shiharaihouhou is '支払方法';
comment on column keihiseisan.hontai_kingaku_goukei is '本体金額合計';
comment on column keihiseisan.shouhizeigaku_goukei is '消費税額合計';
comment on column keihiseisan.shiharai_kingaku_goukei is '支払金額合計';
comment on column keihiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column keihiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column keihiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column keihiseisan.hf1_cd is 'HF1コード';
comment on column keihiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column keihiseisan.hf2_cd is 'HF2コード';
comment on column keihiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column keihiseisan.hf3_cd is 'HF3コード';
comment on column keihiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column keihiseisan.hf4_cd is 'HF4コード';
comment on column keihiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column keihiseisan.hf5_cd is 'HF5コード';
comment on column keihiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column keihiseisan.hf6_cd is 'HF6コード';
comment on column keihiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column keihiseisan.hf7_cd is 'HF7コード';
comment on column keihiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column keihiseisan.hf8_cd is 'HF8コード';
comment on column keihiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column keihiseisan.hf9_cd is 'HF9コード';
comment on column keihiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column keihiseisan.hf10_cd is 'HF10コード';
comment on column keihiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column keihiseisan.hosoku is '補足';
comment on column keihiseisan.touroku_user_id is '登録ユーザーID';
comment on column keihiseisan.touroku_time is '登録日時';
comment on column keihiseisan.koushin_user_id is '更新ユーザーID';
comment on column keihiseisan.koushin_time is '更新日時';
comment on column keihiseisan.invoice_denpyou is 'インボイス対応伝票';

comment on table keihiseisan_meisai is '経費精算明細';
comment on column keihiseisan_meisai.denpyou_id is '伝票ID';
comment on column keihiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column keihiseisan_meisai.shiwake_edano is '仕訳枝番号';
comment on column keihiseisan_meisai.user_id is 'ユーザーID';
comment on column keihiseisan_meisai.shain_no is '社員番号';
comment on column keihiseisan_meisai.user_sei is 'ユーザー姓';
comment on column keihiseisan_meisai.user_mei is 'ユーザー名';
comment on column keihiseisan_meisai.shiyoubi is '使用日';
comment on column keihiseisan_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column keihiseisan_meisai.torihiki_name is '取引名';
comment on column keihiseisan_meisai.tekiyou is '摘要';
comment on column keihiseisan_meisai.zeiritsu is '税率';
comment on column keihiseisan_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column keihiseisan_meisai.shiharai_kingaku is '支払金額';
comment on column keihiseisan_meisai.hontai_kingaku is '本体金額';
comment on column keihiseisan_meisai.shouhizeigaku is '消費税額';
comment on column keihiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column keihiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column keihiseisan_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column keihiseisan_meisai.kousaihi_shousai is '交際費詳細';
comment on column keihiseisan_meisai.kousaihi_ninzuu is '交際費人数';
comment on column keihiseisan_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column keihiseisan_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column keihiseisan_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column keihiseisan_meisai.torihikisaki_cd is '取引先コード';
comment on column keihiseisan_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column keihiseisan_meisai.kari_kamoku_cd is '借方科目コード';
comment on column keihiseisan_meisai.kari_kamoku_name is '借方科目名';
comment on column keihiseisan_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column keihiseisan_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column keihiseisan_meisai.kari_kazei_kbn is '借方課税区分';
comment on column keihiseisan_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column keihiseisan_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column keihiseisan_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column keihiseisan_meisai.kashi_kamoku_name is '貸方科目名';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column keihiseisan_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column keihiseisan_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column keihiseisan_meisai.uf1_cd is 'UF1コード';
comment on column keihiseisan_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column keihiseisan_meisai.uf2_cd is 'UF2コード';
comment on column keihiseisan_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column keihiseisan_meisai.uf3_cd is 'UF3コード';
comment on column keihiseisan_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column keihiseisan_meisai.uf4_cd is 'UF4コード';
comment on column keihiseisan_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column keihiseisan_meisai.uf5_cd is 'UF5コード';
comment on column keihiseisan_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column keihiseisan_meisai.uf6_cd is 'UF6コード';
comment on column keihiseisan_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column keihiseisan_meisai.uf7_cd is 'UF7コード';
comment on column keihiseisan_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column keihiseisan_meisai.uf8_cd is 'UF8コード';
comment on column keihiseisan_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column keihiseisan_meisai.uf9_cd is 'UF9コード';
comment on column keihiseisan_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column keihiseisan_meisai.uf10_cd is 'UF10コード';
comment on column keihiseisan_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column keihiseisan_meisai.project_cd is 'プロジェクトコード';
comment on column keihiseisan_meisai.project_name is 'プロジェクト名';
comment on column keihiseisan_meisai.segment_cd is 'セグメントコード';
comment on column keihiseisan_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column keihiseisan_meisai.tekiyou_cd is '摘要コード';
comment on column keihiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column keihiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column keihiseisan_meisai.touroku_time is '登録日時';
comment on column keihiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column keihiseisan_meisai.koushin_time is '更新日時';
comment on column keihiseisan_meisai.jigyousha_kbn is '事業者区分';
comment on column keihiseisan_meisai.shiharaisaki_name is '支払先名';
comment on column keihiseisan_meisai.bunri_kbn is '分離区分';
comment on column keihiseisan_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column keihiseisan_meisai.kashi_shiire_kbn is '貸方仕入区分';

comment on table keijoubi_shimebi is '計上日締日';
comment on column keijoubi_shimebi.denpyou_kbn is '伝票区分';
comment on column keijoubi_shimebi.shimebi is '締日';
comment on column keijoubi_shimebi.touroku_user_id is '登録ユーザーID';
comment on column keijoubi_shimebi.touroku_time is '登録日時';
comment on column keijoubi_shimebi.koushin_user_id is '更新ユーザーID';
comment on column keijoubi_shimebi.koushin_time is '更新日時';

comment on table ki_bumon is '（期別）部門';
comment on column ki_bumon.kesn is '内部決算期';
comment on column ki_bumon.futan_bumon_cd is '負担部門コード';
comment on column ki_bumon.futan_bumon_name is '負担部門名';
comment on column ki_bumon.oya_syuukei_bumon_cd is '親集計部門コード';
comment on column ki_bumon.shiire_kbn is '仕入区分';
comment on column ki_bumon.nyuryoku_from_date is '入力開始日';
comment on column ki_bumon.nyuryoku_to_date is '入力終了日';

comment on table ki_bumon_security is '（期別）部門セキュリティ';
comment on column ki_bumon_security.kesn is '内部決算期';
comment on column ki_bumon_security.sptn is 'セキュリティパターン';
comment on column ki_bumon_security.futan_bumon_cd is '負担部門コード';

comment on table ki_busho_shiwake is '（期別）部署入出力仕訳';
comment on column ki_busho_shiwake.kesn is '内部決算期';
comment on column ki_busho_shiwake.dkei is '経過月';
comment on column ki_busho_shiwake.dseq is '伝票SEQ';
comment on column ki_busho_shiwake.sseq is '仕訳SEQ';
comment on column ki_busho_shiwake.dymd is '（オープン２１）伝票日付';
comment on column ki_busho_shiwake.dcno is '（オープン２１）伝票番号';
comment on column ki_busho_shiwake.valu is '（オープン２１）金額';
comment on column ki_busho_shiwake.rkmk is '（オープン２１）借方　科目内部コード';
comment on column ki_busho_shiwake.reda is '（オープン２１）借方　枝番コード';
comment on column ki_busho_shiwake.rbmn is '（オープン２１）借方　部門コード';
comment on column ki_busho_shiwake.skmk is '（オープン２１）貸方　科目内部コード';
comment on column ki_busho_shiwake.seda is '（オープン２１）貸方　枝番コード';
comment on column ki_busho_shiwake.sbmn is '（オープン２１）貸方　部門コード';

comment on table ki_kamoku is '（期別）科目';
comment on column ki_kamoku.kesn is '内部決算期';
comment on column ki_kamoku.kamoku_naibu_cd is '科目内部コード';
comment on column ki_kamoku.kamoku_gaibu_cd is '科目外部コード';
comment on column ki_kamoku.kamoku_name_ryakushiki is '科目名（略式）';
comment on column ki_kamoku.taishaku_zokusei is '貸借属性';

comment on table ki_kamoku_edaban is '（期別）科目枝番';
comment on column ki_kamoku_edaban.kesn is '内部決算期';
comment on column ki_kamoku_edaban.kamoku_naibu_cd is '科目内部コード';
comment on column ki_kamoku_edaban.kamoku_edaban_cd is '科目枝番コード';
comment on column ki_kamoku_edaban.edaban_name is '枝番名';

comment on table ki_kamoku_security is '（期別）科目セキュリティ';
comment on column ki_kamoku_security.kesn is '内部決算期';
comment on column ki_kamoku_security.sptn is 'セキュリティパターン';
comment on column ki_kamoku_security.kamoku_naibu_cd is '科目内部コード';

comment on table ki_kesn is '（期別）決算期';
comment on column ki_kesn.kesn is '内部決算期';
comment on column ki_kesn.kessanki_bangou is '決算期番号';
comment on column ki_kesn.from_date is '開始日';
comment on column ki_kesn.to_date is '終了日';

comment on table ki_shiwake is '（期別）財務仕訳';
comment on column ki_shiwake.kesn is '内部決算期';
comment on column ki_shiwake.dkei is '経過月';
comment on column ki_shiwake.dseq is '伝票SEQ';
comment on column ki_shiwake.sseq is '仕訳SEQ';
comment on column ki_shiwake.dymd is '（オープン２１）伝票日付';
comment on column ki_shiwake.dcno is '（オープン２１）伝票番号';
comment on column ki_shiwake.valu is '（オープン２１）金額';
comment on column ki_shiwake.rkmk is '（オープン２１）借方　科目内部コード';
comment on column ki_shiwake.reda is '（オープン２１）借方　枝番コード';
comment on column ki_shiwake.rbmn is '（オープン２１）借方　部門コード';
comment on column ki_shiwake.rtky is '（オープン２１）借方　摘要';
comment on column ki_shiwake.rtor is '（オープン２１）借方　取引先コード';
comment on column ki_shiwake.skmk is '（オープン２１）貸方　科目内部コード';
comment on column ki_shiwake.seda is '（オープン２１）貸方　枝番コード';
comment on column ki_shiwake.sbmn is '（オープン２１）貸方　部門コード';
comment on column ki_shiwake.stky is '（オープン２１）貸方　摘要';
comment on column ki_shiwake.stor is '（オープン２１）貸方　取引先コード';
comment on column ki_shiwake.fway is '入力手段';

comment on table ki_shouhizei_setting is '（期別）消費税設定';
comment on column ki_shouhizei_setting.kesn is '内部決算期';
comment on column ki_shouhizei_setting.shiire_zeigaku_anbun_flg is '仕入税額按分フラグ';
comment on column ki_shouhizei_setting.shouhizei_kbn is '消費税区分';
comment on column ki_shouhizei_setting.hasuu_shori_flg is '端数処理フラグ';
comment on column ki_shouhizei_setting.zeigaku_keisan_flg is '税額計算フラグ';
comment on column ki_shouhizei_setting.shouhizeitaishou_minyuryoku_flg is '消費税対象科目未入力フラグ';
comment on column ki_shouhizei_setting.shisan is '資産';
comment on column ki_shouhizei_setting.uriage is '売上';
comment on column ki_shouhizei_setting.shiire is '仕入';
comment on column ki_shouhizei_setting.keihi is '経費';
comment on column ki_shouhizei_setting.bumonbetsu_shori is '部門別処理';
comment on column ki_shouhizei_setting.tokuteishiire is '特定仕入取引等';
comment on column ki_shouhizei_setting.zero_shouhizei is '0円消費税作成';
comment on column ki_shouhizei_setting.shouhizei_bumon is '仮払・仮受消費税　部門';
comment on column ki_shouhizei_setting.shouhizei_torihikisaki is '取引先';
comment on column ki_shouhizei_setting.shouhizei_edaban is '枝番';
comment on column ki_shouhizei_setting.shouhizei_project is 'プロジェクト';
comment on column ki_shouhizei_setting.shouhizei_segment is 'セグメント';
comment on column ki_shouhizei_setting.shouhizei_uf1 is 'ユニバーサル１';
comment on column ki_shouhizei_setting.shouhizei_uf2 is 'ユニバーサル２';
comment on column ki_shouhizei_setting.shouhizei_uf3 is 'ユニバーサル３';
comment on column ki_shouhizei_setting.shouhizei_kouji is '工事';
comment on column ki_shouhizei_setting.shouhizei_koushu is '工種';
comment on column ki_shouhizei_setting.ukeshouhizei_uriage is '仮受消費税　売上';
comment on column ki_shouhizei_setting.ukeshouhizei_shisan is '資産';
comment on column ki_shouhizei_setting.haraishouhizei_shiire is '仮払消費税　仕入';
comment on column ki_shouhizei_setting.haraishouhizei_keihi is '経費';
comment on column ki_shouhizei_setting.haraisyouhizei_shisan is '資産';
comment on column ki_shouhizei_setting.uriagezeigaku_keisan is '売上税額計算方式';
comment on column ki_shouhizei_setting.shiirezeigaku_keisan is '仕入税額計算方式';
comment on column ki_shouhizei_setting.shiirezeigaku_keikasothi is '仕入税額控除経過措置適用';

comment on table ki_syuukei_bumon is '（期別）集計部門';
comment on column ki_syuukei_bumon.kesn is '内部決算期';
comment on column ki_syuukei_bumon.syuukei_bumon_cd is '集計部門コード';
comment on column ki_syuukei_bumon.futan_bumon_cd is '負担部門コード';
comment on column ki_syuukei_bumon.syuukei_bumon_name is '集計部門名';
comment on column ki_syuukei_bumon.futan_bumon_name is '負担部門名';

comment on table kian_bangou_bo is '起案番号簿';
comment on column kian_bangou_bo.bumon_cd is '部門コード';
comment on column kian_bangou_bo.nendo is '年度';
comment on column kian_bangou_bo.ryakugou is '略号';
comment on column kian_bangou_bo.kian_bangou_from is '開始起案番号';
comment on column kian_bangou_bo.kian_bangou_to is '終了起案番号';
comment on column kian_bangou_bo.kbn_naiyou is '区分内容';
comment on column kian_bangou_bo.kianbangou_bo_sentaku_hyouji_flg is '起案番号簿選択表示フラグ';
comment on column kian_bangou_bo.denpyou_kensaku_hyouji_flg is '伝票検索表示フラグ';
comment on column kian_bangou_bo.yuukou_kigen_from is '有効期限開始日';
comment on column kian_bangou_bo.yuukou_kigen_to is '有効期限終了日';
comment on column kian_bangou_bo.hyouji_jun is '表示順';

comment on table kian_bangou_saiban is '起案番号採番';
comment on column kian_bangou_saiban.bumon_cd is '部門コード';
comment on column kian_bangou_saiban.nendo is '年度';
comment on column kian_bangou_saiban.ryakugou is '略号';
comment on column kian_bangou_saiban.kian_bangou_from is '開始起案番号';
comment on column kian_bangou_saiban.kian_bangou_last is '最終起案番号';

comment on table kinou_seigyo is '機能制御';
comment on column kinou_seigyo.kinou_seigyo_cd is '機能制御コード';
comment on column kinou_seigyo.kinou_seigyo_kbn is '機能制御区分';

comment on table kinyuukikan is '金融機関';
comment on column kinyuukikan.kinyuukikan_cd is '金融機関コード';
comment on column kinyuukikan.kinyuukikan_shiten_cd is '金融機関支店コード';
comment on column kinyuukikan.kinyuukikan_name_hankana is '金融機関名（半角カナ）';
comment on column kinyuukikan.kinyuukikan_name_kana is '金融機関名';
comment on column kinyuukikan.shiten_name_hankana is '支店名（半角カナ）';
comment on column kinyuukikan.shiten_name_kana is '支店名';

comment on table koutsuu_shudan_master is '国内用交通手段マスター';
comment on column koutsuu_shudan_master.sort_jun is '並び順';
comment on column koutsuu_shudan_master.koutsuu_shudan is '交通手段';
comment on column koutsuu_shudan_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column koutsuu_shudan_master.zei_kubun is '税区分';
comment on column koutsuu_shudan_master.edaban is '枝番コード';
comment on column koutsuu_shudan_master.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column koutsuu_shudan_master.tanka is '単価';
comment on column koutsuu_shudan_master.suuryou_kigou is '数量記号';

comment on table koutsuuhiseisan is '交通費精算';
comment on column koutsuuhiseisan.denpyou_id is '伝票ID';
comment on column koutsuuhiseisan.mokuteki is '目的';
comment on column koutsuuhiseisan.seisankikan_from is '精算期間開始日';
comment on column koutsuuhiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column koutsuuhiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column koutsuuhiseisan.seisankikan_to is '精算期間終了日';
comment on column koutsuuhiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column koutsuuhiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column koutsuuhiseisan.keijoubi is '計上日';
comment on column koutsuuhiseisan.shiharaibi is '支払日';
comment on column koutsuuhiseisan.shiharaikiboubi is '支払希望日';
comment on column koutsuuhiseisan.shiharaihouhou is '支払方法';
comment on column koutsuuhiseisan.tekiyou is '摘要';
comment on column koutsuuhiseisan.zeiritsu is '税率';
comment on column koutsuuhiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column koutsuuhiseisan.goukei_kingaku is '合計金額';
comment on column koutsuuhiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column koutsuuhiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column koutsuuhiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column koutsuuhiseisan.hf1_cd is 'HF1コード';
comment on column koutsuuhiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column koutsuuhiseisan.hf2_cd is 'HF2コード';
comment on column koutsuuhiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column koutsuuhiseisan.hf3_cd is 'HF3コード';
comment on column koutsuuhiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column koutsuuhiseisan.hf4_cd is 'HF4コード';
comment on column koutsuuhiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column koutsuuhiseisan.hf5_cd is 'HF5コード';
comment on column koutsuuhiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column koutsuuhiseisan.hf6_cd is 'HF6コード';
comment on column koutsuuhiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column koutsuuhiseisan.hf7_cd is 'HF7コード';
comment on column koutsuuhiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column koutsuuhiseisan.hf8_cd is 'HF8コード';
comment on column koutsuuhiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column koutsuuhiseisan.hf9_cd is 'HF9コード';
comment on column koutsuuhiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column koutsuuhiseisan.hf10_cd is 'HF10コード';
comment on column koutsuuhiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column koutsuuhiseisan.hosoku is '補足';
comment on column koutsuuhiseisan.shiwake_edano is '仕訳枝番号';
comment on column koutsuuhiseisan.torihiki_name is '取引名';
comment on column koutsuuhiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column koutsuuhiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column koutsuuhiseisan.torihikisaki_cd is '取引先コード';
comment on column koutsuuhiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column koutsuuhiseisan.kari_kamoku_cd is '借方科目コード';
comment on column koutsuuhiseisan.kari_kamoku_name is '借方科目名';
comment on column koutsuuhiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column koutsuuhiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column koutsuuhiseisan.kari_kazei_kbn is '借方課税区分';
comment on column koutsuuhiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column koutsuuhiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column koutsuuhiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column koutsuuhiseisan.kashi_kamoku_name is '貸方科目名';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column koutsuuhiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column koutsuuhiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column koutsuuhiseisan.uf1_cd is 'UF1コード';
comment on column koutsuuhiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column koutsuuhiseisan.uf2_cd is 'UF2コード';
comment on column koutsuuhiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column koutsuuhiseisan.uf3_cd is 'UF3コード';
comment on column koutsuuhiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column koutsuuhiseisan.uf4_cd is 'UF4コード';
comment on column koutsuuhiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column koutsuuhiseisan.uf5_cd is 'UF5コード';
comment on column koutsuuhiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column koutsuuhiseisan.uf6_cd is 'UF6コード';
comment on column koutsuuhiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column koutsuuhiseisan.uf7_cd is 'UF7コード';
comment on column koutsuuhiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column koutsuuhiseisan.uf8_cd is 'UF8コード';
comment on column koutsuuhiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column koutsuuhiseisan.uf9_cd is 'UF9コード';
comment on column koutsuuhiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column koutsuuhiseisan.uf10_cd is 'UF10コード';
comment on column koutsuuhiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column koutsuuhiseisan.project_cd is 'プロジェクトコード';
comment on column koutsuuhiseisan.project_name is 'プロジェクト名';
comment on column koutsuuhiseisan.segment_cd is 'セグメントコード';
comment on column koutsuuhiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column koutsuuhiseisan.tekiyou_cd is '摘要コード';
comment on column koutsuuhiseisan.touroku_user_id is '登録ユーザーID';
comment on column koutsuuhiseisan.touroku_time is '登録日時';
comment on column koutsuuhiseisan.koushin_user_id is '更新ユーザーID';
comment on column koutsuuhiseisan.koushin_time is '更新日時';
comment on column koutsuuhiseisan.bunri_kbn is '分離区分';
comment on column koutsuuhiseisan.kari_shiire_kbn is '借方仕入区分';
comment on column koutsuuhiseisan.kashi_shiire_kbn is '貸方仕入区分';
comment on column koutsuuhiseisan.invoice_denpyou is 'インボイス対応伝票';

comment on table koutsuuhiseisan_meisai is '交通費精算明細';
comment on column koutsuuhiseisan_meisai.denpyou_id is '伝票ID';
comment on column koutsuuhiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column koutsuuhiseisan_meisai.kikan_from is '期間開始日';
comment on column koutsuuhiseisan_meisai.shubetsu_cd is '種別コード';
comment on column koutsuuhiseisan_meisai.shubetsu1 is '種別１';
comment on column koutsuuhiseisan_meisai.shubetsu2 is '種別２';
comment on column koutsuuhiseisan_meisai.haya_flg is '早フラグ';
comment on column koutsuuhiseisan_meisai.yasu_flg is '安フラグ';
comment on column koutsuuhiseisan_meisai.raku_flg is '楽フラグ';
comment on column koutsuuhiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column koutsuuhiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column koutsuuhiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column koutsuuhiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column koutsuuhiseisan_meisai.bikou is '備考（旅費精算）';
comment on column koutsuuhiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column koutsuuhiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column koutsuuhiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column koutsuuhiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column koutsuuhiseisan_meisai.tanka is '単価';
comment on column koutsuuhiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column koutsuuhiseisan_meisai.suuryou is '数量';
comment on column koutsuuhiseisan_meisai.suuryou_kigou is '数量記号';
comment on column koutsuuhiseisan_meisai.meisai_kingaku is '明細金額';
comment on column koutsuuhiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column koutsuuhiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column koutsuuhiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column koutsuuhiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column koutsuuhiseisan_meisai.touroku_time is '登録日時';
comment on column koutsuuhiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column koutsuuhiseisan_meisai.koushin_time is '更新日時';
comment on column koutsuuhiseisan_meisai.shiharaisaki_name is '支払先名';
comment on column koutsuuhiseisan_meisai.jigyousha_kbn is '事業者区分';
comment on column koutsuuhiseisan_meisai.zeinuki_kingaku is '税抜金額';
comment on column koutsuuhiseisan_meisai.shouhizeigaku is '消費税額';
comment on column koutsuuhiseisan_meisai.zeigaku_fix_flg is '税額修正フラグ';

comment on table list_item_control is '一覧表示項目制御';
comment on column list_item_control.kbn is '表示項目区分';
comment on column list_item_control.user_id is 'ユーザーID';
comment on column list_item_control.gyoumu_role_id is '業務ロールID';
comment on column list_item_control.index is '項番';
comment on column list_item_control.name is '項目名';
comment on column list_item_control.display_flg is '表示フラグ';

comment on table mail_settei is 'メール設定';
comment on column mail_settei.smtp_server_name is 'SMTPサーバー名';
comment on column mail_settei.port_no is 'ポート番号';
comment on column mail_settei.ninshou_houhou is '認証方法';
comment on column mail_settei.angouka_houhou is '暗号化方法';
comment on column mail_settei.mail_address is 'メールアドレス';
comment on column mail_settei.mail_id is 'メールID';
comment on column mail_settei.mail_password is 'メールパスワード';

comment on table mail_tsuuchi is 'メール通知設定';
comment on column mail_tsuuchi.user_id is 'ユーザーID';
comment on column mail_tsuuchi.tsuuchi_kbn is '通知区分';
comment on column mail_tsuuchi.soushinumu is '送信有無';

comment on table master_kanri_hansuu is 'マスター管理版数';
comment on column master_kanri_hansuu.master_id is 'マスターID';
comment on column master_kanri_hansuu.version is 'ヴァージョン';
comment on column master_kanri_hansuu.delete_flg is '削除フラグ';
comment on column master_kanri_hansuu.file_name is 'ファイル名';
comment on column master_kanri_hansuu.file_size is 'ファイルサイズ';
comment on column master_kanri_hansuu.content_type is 'コンテンツタイプ';
comment on column master_kanri_hansuu.binary_data is 'バイナリーデータ';
comment on column master_kanri_hansuu.touroku_user_id is '登録ユーザーID';
comment on column master_kanri_hansuu.touroku_time is '登録日時';
comment on column master_kanri_hansuu.koushin_user_id is '更新ユーザーID';
comment on column master_kanri_hansuu.koushin_time is '更新日時';

comment on table master_kanri_ichiran is 'マスター管理一覧';
comment on column master_kanri_ichiran.master_id is 'マスターID';
comment on column master_kanri_ichiran.master_name is 'マスター名';
comment on column master_kanri_ichiran.henkou_kahi_flg is '変更可否フラグ';
comment on column master_kanri_ichiran.touroku_user_id is '登録ユーザーID';
comment on column master_kanri_ichiran.touroku_time is '登録日時';
comment on column master_kanri_ichiran.koushin_user_id is '更新ユーザーID';
comment on column master_kanri_ichiran.koushin_time is '更新日時';

comment on table master_torikomi_ichiran_de3 is 'マスター取込一覧(de3)';
comment on column master_torikomi_ichiran_de3.master_id is 'マスターID';
comment on column master_torikomi_ichiran_de3.master_name is 'マスター名';
comment on column master_torikomi_ichiran_de3.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_ichiran_de3.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_ichiran_de3.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_ichiran_mk2 is 'マスター取込一覧(SIAS_mk2)';
comment on column master_torikomi_ichiran_mk2.master_id is 'マスターID';
comment on column master_torikomi_ichiran_mk2.master_name is 'マスター名';
comment on column master_torikomi_ichiran_mk2.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_ichiran_mk2.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_ichiran_mk2.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_ichiran_sias is 'マスター取込一覧(SIAS)';
comment on column master_torikomi_ichiran_sias.master_id is 'マスターID';
comment on column master_torikomi_ichiran_sias.master_name is 'マスター名';
comment on column master_torikomi_ichiran_sias.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_ichiran_sias.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_ichiran_sias.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_shousai_de3 is 'マスター取込詳細(de3)';
comment on column master_torikomi_shousai_de3.master_id is 'マスターID';
comment on column master_torikomi_shousai_de3.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_shousai_de3.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_shousai_de3.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_shousai_de3.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_shousai_de3.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_shousai_de3.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_shousai_de3.entry_order is '登録順';
comment on column master_torikomi_shousai_de3.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_shousai_mk2 is 'マスター取込詳細(SIAS_mk2)';
comment on column master_torikomi_shousai_mk2.master_id is 'マスターID';
comment on column master_torikomi_shousai_mk2.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_shousai_mk2.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_shousai_mk2.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_shousai_mk2.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_shousai_mk2.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_shousai_mk2.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_shousai_mk2.entry_order is '登録順';
comment on column master_torikomi_shousai_mk2.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_shousai_sias is 'マスター取込詳細(SIAS)';
comment on column master_torikomi_shousai_sias.master_id is 'マスターID';
comment on column master_torikomi_shousai_sias.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_shousai_sias.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_shousai_sias.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_shousai_sias.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_shousai_sias.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_shousai_sias.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_shousai_sias.entry_order is '登録順';
comment on column master_torikomi_shousai_sias.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_term_ichiran_de3 is 'マスター取込期間一覧(de3)';
comment on column master_torikomi_term_ichiran_de3.master_id is 'マスターID';
comment on column master_torikomi_term_ichiran_de3.master_name is 'マスター名';
comment on column master_torikomi_term_ichiran_de3.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_term_ichiran_de3.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_term_ichiran_de3.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_term_ichiran_mk2 is 'マスター取込期間一覧(SIAS_mk2)';
comment on column master_torikomi_term_ichiran_mk2.master_id is 'マスターID';
comment on column master_torikomi_term_ichiran_mk2.master_name is 'マスター名';
comment on column master_torikomi_term_ichiran_mk2.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_term_ichiran_mk2.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_term_ichiran_mk2.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_term_ichiran_sias is 'マスター取込期間一覧(SIAS)';
comment on column master_torikomi_term_ichiran_sias.master_id is 'マスターID';
comment on column master_torikomi_term_ichiran_sias.master_name is 'マスター名';
comment on column master_torikomi_term_ichiran_sias.op_master_id is 'OPEN21マスターID';
comment on column master_torikomi_term_ichiran_sias.op_master_name is 'OPEN21マスター名';
comment on column master_torikomi_term_ichiran_sias.torikomi_kahi_flg is '取込可否フラグ';

comment on table master_torikomi_term_shousai_de3 is 'マスター取込期間詳細(de3)';
comment on column master_torikomi_term_shousai_de3.master_id is 'マスターID';
comment on column master_torikomi_term_shousai_de3.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_term_shousai_de3.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_term_shousai_de3.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_term_shousai_de3.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_term_shousai_de3.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_term_shousai_de3.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_term_shousai_de3.entry_order is '登録順';
comment on column master_torikomi_term_shousai_de3.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_term_shousai_mk2 is 'マスター取込期間詳細(SIAS_mk2)';
comment on column master_torikomi_term_shousai_mk2.master_id is 'マスターID';
comment on column master_torikomi_term_shousai_mk2.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_term_shousai_mk2.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_term_shousai_mk2.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_term_shousai_mk2.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_term_shousai_mk2.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_term_shousai_mk2.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_term_shousai_mk2.entry_order is '登録順';
comment on column master_torikomi_term_shousai_mk2.pk_flg is 'プライマリーキーフラグ';

comment on table master_torikomi_term_shousai_sias is 'マスター取込期間詳細(SIAS)';
comment on column master_torikomi_term_shousai_sias.master_id is 'マスターID';
comment on column master_torikomi_term_shousai_sias.et_column_id is 'eTeamカラムID';
comment on column master_torikomi_term_shousai_sias.et_column_name is 'eTeamカラム名';
comment on column master_torikomi_term_shousai_sias.et_data_type is 'eTeamデータ型';
comment on column master_torikomi_term_shousai_sias.op_colume_id is 'OPEN21カラムID';
comment on column master_torikomi_term_shousai_sias.op_column_name is 'OPEN21カラム名';
comment on column master_torikomi_term_shousai_sias.op_data_type is 'OPEN21データ型';
comment on column master_torikomi_term_shousai_sias.entry_order is '登録順';
comment on column master_torikomi_term_shousai_sias.pk_flg is 'プライマリーキーフラグ';

comment on table midashi is '見出し';
comment on column midashi.midashi_id is '見出しID';
comment on column midashi.midashi_name is '見出し名';
comment on column midashi.hyouji_jun is '表示順';
comment on column midashi.touroku_user_id is '登録ユーザーID';
comment on column midashi.touroku_time is '登録日時';
comment on column midashi.koushin_user_id is '更新ユーザーID';
comment on column midashi.koushin_time is '更新日時';

comment on table moto_kouza is '振込元口座';
comment on column moto_kouza.moto_kinyuukikan_cd is '振込元金融機関コード';
comment on column moto_kouza.moto_kinyuukikan_shiten_cd is '振込元金融機関支店コード';
comment on column moto_kouza.moto_yokinshubetsu is '振込元預金種別';
comment on column moto_kouza.moto_kouza_bangou is '振込元口座番号';
comment on column moto_kouza.moto_kinyuukikan_name_hankana is '振込元金融機関名（半角カナ）';
comment on column moto_kouza.moto_kinyuukikan_shiten_name_hankana is '振込元金融機関支店名（半角カナ）';
comment on column moto_kouza.shubetsu_cd is '種別コード';
comment on column moto_kouza.cd_kbn is 'コード区分';
comment on column moto_kouza.kaisha_cd is '会社コード';
comment on column moto_kouza.kaisha_name_hankana is '会社名（半角カナ）';
comment on column moto_kouza.shinki_cd is '新規コード';
comment on column moto_kouza.furikomi_kbn is '振込区分';

comment on table moto_kouza_shiharaiirai is '振込元口座（支払依頼）';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_cd is '振込元金融機関コード';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_shiten_cd is '振込元金融機関支店コード';
comment on column moto_kouza_shiharaiirai.moto_yokinshubetsu is '振込元預金種別';
comment on column moto_kouza_shiharaiirai.moto_kouza_bangou is '振込元口座番号';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_name_hankana is '振込元金融機関名（半角カナ）';
comment on column moto_kouza_shiharaiirai.moto_kinyuukikan_shiten_name_hankana is '振込元金融機関支店名（半角カナ）';
comment on column moto_kouza_shiharaiirai.shubetsu_cd is '種別コード';
comment on column moto_kouza_shiharaiirai.cd_kbn is 'コード区分';
comment on column moto_kouza_shiharaiirai.kaisha_cd is '会社コード';
comment on column moto_kouza_shiharaiirai.kaisha_name_hankana is '会社名（半角カナ）';
comment on column moto_kouza_shiharaiirai.shinki_cd is '新規コード';
comment on column moto_kouza_shiharaiirai.furikomi_kbn is '振込区分';

comment on table naibu_cd_setting is '内部コード設定';
comment on column naibu_cd_setting.naibu_cd_name is '内部コード名';
comment on column naibu_cd_setting.naibu_cd is '内部コード';
comment on column naibu_cd_setting.name is '名称';
comment on column naibu_cd_setting.hyouji_jun is '表示順';
comment on column naibu_cd_setting.option1 is 'オプション１';
comment on column naibu_cd_setting.option2 is 'オプション２';
comment on column naibu_cd_setting.option3 is 'オプション３';

comment on table nini_comment is '任意メモ';
comment on column nini_comment.denpyou_id is '伝票ID';
comment on column nini_comment.edano is '枝番号';
comment on column nini_comment.user_id is 'ユーザーID';
comment on column nini_comment.user_full_name is 'ユーザーフル名';
comment on column nini_comment.comment is 'コメント';
comment on column nini_comment.touroku_user_id is '登録ユーザーID';
comment on column nini_comment.touroku_time is '登録日時';
comment on column nini_comment.koushin_user_id is '更新ユーザーID';
comment on column nini_comment.koushin_time is '更新日時';

comment on table nittou_nado_master is '国内用日当等マスター';
comment on column nittou_nado_master.shubetsu1 is '種別１';
comment on column nittou_nado_master.shubetsu2 is '種別２';
comment on column nittou_nado_master.yakushoku_cd is '役職コード';
comment on column nittou_nado_master.tanka is '単価';
comment on column nittou_nado_master.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column nittou_nado_master.nittou_shukuhakuhi_flg is '日当・宿泊費フラグ';
comment on column nittou_nado_master.zei_kubun is '税区分';
comment on column nittou_nado_master.edaban is '枝番コード';

comment on table open21_kinyuukikan is '金融機関';
comment on column open21_kinyuukikan.kinyuukikan_cd is '金融機関コード';
comment on column open21_kinyuukikan.kinyuukikan_shiten_cd is '金融機関支店コード';
comment on column open21_kinyuukikan.kinyuukikan_name_hankana is '金融機関名（半角カナ）';
comment on column open21_kinyuukikan.kinyuukikan_name_kana is '金融機関名';
comment on column open21_kinyuukikan.shiten_name_hankana is '支店名（半角カナ）';
comment on column open21_kinyuukikan.shiten_name_kana is '支店名';

comment on table password is 'パスワード';
comment on column password.user_id is 'ユーザーID';
comment on column password.password is 'パスワード';

comment on table project_master is 'プロジェクトマスター';
comment on column project_master.project_cd is 'プロジェクトコード';
comment on column project_master.project_name is 'プロジェクト名';
comment on column project_master.shuuryou_kbn is '終了区分';

comment on table rate_master is '幣種別レートマスター';
comment on column rate_master.heishu_cd is '幣種コード';
comment on column rate_master.start_date is '適用開始日時';
comment on column rate_master.rate is '適用レート';
comment on column rate_master.rate1 is '適用レート(予備)';
comment on column rate_master.rate2 is '適用レート(予備)';
comment on column rate_master.rate3 is '適用レート(予備)';
comment on column rate_master.availability_flg is '初期値使用可否';

comment on table ryohi_karibarai is '旅費仮払';
comment on column ryohi_karibarai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai.karibarai_on is '仮払申請フラグ';
comment on column ryohi_karibarai.dairiflg is '代理フラグ';
comment on column ryohi_karibarai.user_id is 'ユーザーID';
comment on column ryohi_karibarai.shain_no is '社員番号';
comment on column ryohi_karibarai.user_sei is 'ユーザー姓';
comment on column ryohi_karibarai.user_mei is 'ユーザー名';
comment on column ryohi_karibarai.houmonsaki is '訪問先';
comment on column ryohi_karibarai.mokuteki is '目的';
comment on column ryohi_karibarai.seisankikan_from is '精算期間開始日';
comment on column ryohi_karibarai.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column ryohi_karibarai.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column ryohi_karibarai.seisankikan_to is '精算期間終了日';
comment on column ryohi_karibarai.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column ryohi_karibarai.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column ryohi_karibarai.shiharaibi is '支払日';
comment on column ryohi_karibarai.shiharaikiboubi is '支払希望日';
comment on column ryohi_karibarai.shiharaihouhou is '支払方法';
comment on column ryohi_karibarai.tekiyou is '摘要';
comment on column ryohi_karibarai.kingaku is '金額';
comment on column ryohi_karibarai.karibarai_kingaku is '仮払金額';
comment on column ryohi_karibarai.sashihiki_num is '差引回数';
comment on column ryohi_karibarai.sashihiki_tanka is '差引単価';
comment on column ryohi_karibarai.hf1_cd is 'HF1コード';
comment on column ryohi_karibarai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column ryohi_karibarai.hf2_cd is 'HF2コード';
comment on column ryohi_karibarai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column ryohi_karibarai.hf3_cd is 'HF3コード';
comment on column ryohi_karibarai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column ryohi_karibarai.hf4_cd is 'HF4コード';
comment on column ryohi_karibarai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column ryohi_karibarai.hf5_cd is 'HF5コード';
comment on column ryohi_karibarai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column ryohi_karibarai.hf6_cd is 'HF6コード';
comment on column ryohi_karibarai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column ryohi_karibarai.hf7_cd is 'HF7コード';
comment on column ryohi_karibarai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column ryohi_karibarai.hf8_cd is 'HF8コード';
comment on column ryohi_karibarai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column ryohi_karibarai.hf9_cd is 'HF9コード';
comment on column ryohi_karibarai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column ryohi_karibarai.hf10_cd is 'HF10コード';
comment on column ryohi_karibarai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column ryohi_karibarai.hosoku is '補足';
comment on column ryohi_karibarai.shiwake_edano is '仕訳枝番号';
comment on column ryohi_karibarai.torihiki_name is '取引名';
comment on column ryohi_karibarai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohi_karibarai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohi_karibarai.torihikisaki_cd is '取引先コード';
comment on column ryohi_karibarai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohi_karibarai.kari_kamoku_cd is '借方科目コード';
comment on column ryohi_karibarai.kari_kamoku_name is '借方科目名';
comment on column ryohi_karibarai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohi_karibarai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohi_karibarai.kari_kazei_kbn is '借方課税区分';
comment on column ryohi_karibarai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohi_karibarai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohi_karibarai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohi_karibarai.kashi_kamoku_name is '貸方科目名';
comment on column ryohi_karibarai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohi_karibarai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohi_karibarai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohi_karibarai.uf1_cd is 'UF1コード';
comment on column ryohi_karibarai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohi_karibarai.uf2_cd is 'UF2コード';
comment on column ryohi_karibarai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohi_karibarai.uf3_cd is 'UF3コード';
comment on column ryohi_karibarai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohi_karibarai.uf4_cd is 'UF4コード';
comment on column ryohi_karibarai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohi_karibarai.uf5_cd is 'UF5コード';
comment on column ryohi_karibarai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohi_karibarai.uf6_cd is 'UF6コード';
comment on column ryohi_karibarai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohi_karibarai.uf7_cd is 'UF7コード';
comment on column ryohi_karibarai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohi_karibarai.uf8_cd is 'UF8コード';
comment on column ryohi_karibarai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohi_karibarai.uf9_cd is 'UF9コード';
comment on column ryohi_karibarai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohi_karibarai.uf10_cd is 'UF10コード';
comment on column ryohi_karibarai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohi_karibarai.project_cd is 'プロジェクトコード';
comment on column ryohi_karibarai.project_name is 'プロジェクト名';
comment on column ryohi_karibarai.segment_cd is 'セグメントコード';
comment on column ryohi_karibarai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohi_karibarai.tekiyou_cd is '摘要コード';
comment on column ryohi_karibarai.seisan_kanryoubi is '精算完了日';
comment on column ryohi_karibarai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai.touroku_time is '登録日時';
comment on column ryohi_karibarai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai.koushin_time is '更新日時';

comment on table ryohi_karibarai_keihi_meisai is '旅費仮払経費明細';
comment on column ryohi_karibarai_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohi_karibarai_keihi_meisai.shiyoubi is '使用日';
comment on column ryohi_karibarai_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohi_karibarai_keihi_meisai.torihiki_name is '取引名';
comment on column ryohi_karibarai_keihi_meisai.tekiyou is '摘要';
comment on column ryohi_karibarai_keihi_meisai.zeiritsu is '税率';
comment on column ryohi_karibarai_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohi_karibarai_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohi_karibarai_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohi_karibarai_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column ryohi_karibarai_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohi_karibarai_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohi_karibarai_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohi_karibarai_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohi_karibarai_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohi_karibarai_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohi_karibarai_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohi_karibarai_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohi_karibarai_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohi_karibarai_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohi_karibarai_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohi_karibarai_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohi_karibarai_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohi_karibarai_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohi_karibarai_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohi_karibarai_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohi_karibarai_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohi_karibarai_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohi_karibarai_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohi_karibarai_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohi_karibarai_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohi_karibarai_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.touroku_time is '登録日時';
comment on column ryohi_karibarai_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai_keihi_meisai.koushin_time is '更新日時';

comment on table ryohi_karibarai_meisai is '旅費仮払明細';
comment on column ryohi_karibarai_meisai.denpyou_id is '伝票ID';
comment on column ryohi_karibarai_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohi_karibarai_meisai.kikan_from is '期間開始日';
comment on column ryohi_karibarai_meisai.kikan_to is '期間終了日';
comment on column ryohi_karibarai_meisai.kyuujitsu_nissuu is '休日日数';
comment on column ryohi_karibarai_meisai.shubetsu_cd is '種別コード';
comment on column ryohi_karibarai_meisai.shubetsu1 is '種別１';
comment on column ryohi_karibarai_meisai.shubetsu2 is '種別２';
comment on column ryohi_karibarai_meisai.haya_flg is '早フラグ';
comment on column ryohi_karibarai_meisai.yasu_flg is '安フラグ';
comment on column ryohi_karibarai_meisai.raku_flg is '楽フラグ';
comment on column ryohi_karibarai_meisai.koutsuu_shudan is '交通手段';
comment on column ryohi_karibarai_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column ryohi_karibarai_meisai.naiyou is '内容（旅費精算）';
comment on column ryohi_karibarai_meisai.bikou is '備考（旅費精算）';
comment on column ryohi_karibarai_meisai.oufuku_flg is '往復フラグ';
comment on column ryohi_karibarai_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column ryohi_karibarai_meisai.nissuu is '日数';
comment on column ryohi_karibarai_meisai.tanka is '単価';
comment on column ryohi_karibarai_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column ryohi_karibarai_meisai.suuryou is '数量';
comment on column ryohi_karibarai_meisai.suuryou_kigou is '数量記号';
comment on column ryohi_karibarai_meisai.meisai_kingaku is '明細金額';
comment on column ryohi_karibarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohi_karibarai_meisai.touroku_time is '登録日時';
comment on column ryohi_karibarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohi_karibarai_meisai.koushin_time is '更新日時';

comment on table ryohiseisan is '旅費精算';
comment on column ryohiseisan.denpyou_id is '伝票ID';
comment on column ryohiseisan.karibarai_denpyou_id is '仮払伝票ID';
comment on column ryohiseisan.karibarai_on is '仮払申請フラグ';
comment on column ryohiseisan.karibarai_mishiyou_flg is '仮払金未使用フラグ';
comment on column ryohiseisan.shucchou_chuushi_flg is '出張中止フラグ';
comment on column ryohiseisan.dairiflg is '代理フラグ';
comment on column ryohiseisan.user_id is 'ユーザーID';
comment on column ryohiseisan.shain_no is '社員番号';
comment on column ryohiseisan.user_sei is 'ユーザー姓';
comment on column ryohiseisan.user_mei is 'ユーザー名';
comment on column ryohiseisan.houmonsaki is '訪問先';
comment on column ryohiseisan.mokuteki is '目的';
comment on column ryohiseisan.seisankikan_from is '精算期間開始日';
comment on column ryohiseisan.seisankikan_from_hour is '精算期間開始時刻（時）';
comment on column ryohiseisan.seisankikan_from_min is '精算期間開始時刻（分）';
comment on column ryohiseisan.seisankikan_to is '精算期間終了日';
comment on column ryohiseisan.seisankikan_to_hour is '精算期間終了時刻（時）';
comment on column ryohiseisan.seisankikan_to_min is '精算期間終了時刻（分）';
comment on column ryohiseisan.keijoubi is '計上日';
comment on column ryohiseisan.shiharaibi is '支払日';
comment on column ryohiseisan.shiharaikiboubi is '支払希望日';
comment on column ryohiseisan.shiharaihouhou is '支払方法';
comment on column ryohiseisan.tekiyou is '摘要';
comment on column ryohiseisan.zeiritsu is '税率';
comment on column ryohiseisan.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohiseisan.goukei_kingaku is '合計金額';
comment on column ryohiseisan.houjin_card_riyou_kingaku is '内法人カード利用合計';
comment on column ryohiseisan.kaisha_tehai_kingaku is '会社手配合計';
comment on column ryohiseisan.sashihiki_shikyuu_kingaku is '差引支給金額';
comment on column ryohiseisan.sashihiki_num is '差引回数';
comment on column ryohiseisan.sashihiki_tanka is '差引単価';
comment on column ryohiseisan.hf1_cd is 'HF1コード';
comment on column ryohiseisan.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column ryohiseisan.hf2_cd is 'HF2コード';
comment on column ryohiseisan.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column ryohiseisan.hf3_cd is 'HF3コード';
comment on column ryohiseisan.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column ryohiseisan.hf4_cd is 'HF4コード';
comment on column ryohiseisan.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column ryohiseisan.hf5_cd is 'HF5コード';
comment on column ryohiseisan.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column ryohiseisan.hf6_cd is 'HF6コード';
comment on column ryohiseisan.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column ryohiseisan.hf7_cd is 'HF7コード';
comment on column ryohiseisan.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column ryohiseisan.hf8_cd is 'HF8コード';
comment on column ryohiseisan.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column ryohiseisan.hf9_cd is 'HF9コード';
comment on column ryohiseisan.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column ryohiseisan.hf10_cd is 'HF10コード';
comment on column ryohiseisan.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column ryohiseisan.hosoku is '補足';
comment on column ryohiseisan.shiwake_edano is '仕訳枝番号';
comment on column ryohiseisan.torihiki_name is '取引名';
comment on column ryohiseisan.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohiseisan.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohiseisan.torihikisaki_cd is '取引先コード';
comment on column ryohiseisan.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohiseisan.kari_kamoku_cd is '借方科目コード';
comment on column ryohiseisan.kari_kamoku_name is '借方科目名';
comment on column ryohiseisan.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohiseisan.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohiseisan.kari_kazei_kbn is '借方課税区分';
comment on column ryohiseisan.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohiseisan.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohiseisan.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohiseisan.kashi_kamoku_name is '貸方科目名';
comment on column ryohiseisan.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohiseisan.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohiseisan.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohiseisan.uf1_cd is 'UF1コード';
comment on column ryohiseisan.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohiseisan.uf2_cd is 'UF2コード';
comment on column ryohiseisan.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohiseisan.uf3_cd is 'UF3コード';
comment on column ryohiseisan.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohiseisan.uf4_cd is 'UF4コード';
comment on column ryohiseisan.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohiseisan.uf5_cd is 'UF5コード';
comment on column ryohiseisan.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohiseisan.uf6_cd is 'UF6コード';
comment on column ryohiseisan.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohiseisan.uf7_cd is 'UF7コード';
comment on column ryohiseisan.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohiseisan.uf8_cd is 'UF8コード';
comment on column ryohiseisan.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohiseisan.uf9_cd is 'UF9コード';
comment on column ryohiseisan.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohiseisan.uf10_cd is 'UF10コード';
comment on column ryohiseisan.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohiseisan.project_cd is 'プロジェクトコード';
comment on column ryohiseisan.project_name is 'プロジェクト名';
comment on column ryohiseisan.segment_cd is 'セグメントコード';
comment on column ryohiseisan.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohiseisan.tekiyou_cd is '摘要コード';
comment on column ryohiseisan.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan.touroku_time is '登録日時';
comment on column ryohiseisan.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan.koushin_time is '更新日時';
comment on column ryohiseisan.bunri_kbn is '分離区分';
comment on column ryohiseisan.kari_shiire_kbn is '借方仕入区分';
comment on column ryohiseisan.kashi_shiire_kbn is '貸方仕入区分';
comment on column ryohiseisan.invoice_denpyou is 'インボイス対応伝票';

comment on table ryohiseisan_keihi_meisai is '旅費精算経費明細';
comment on column ryohiseisan_keihi_meisai.denpyou_id is '伝票ID';
comment on column ryohiseisan_keihi_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohiseisan_keihi_meisai.shiwake_edano is '仕訳枝番号';
comment on column ryohiseisan_keihi_meisai.shiyoubi is '使用日';
comment on column ryohiseisan_keihi_meisai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column ryohiseisan_keihi_meisai.torihiki_name is '取引名';
comment on column ryohiseisan_keihi_meisai.tekiyou is '摘要';
comment on column ryohiseisan_keihi_meisai.zeiritsu is '税率';
comment on column ryohiseisan_keihi_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column ryohiseisan_keihi_meisai.shiharai_kingaku is '支払金額';
comment on column ryohiseisan_keihi_meisai.hontai_kingaku is '本体金額';
comment on column ryohiseisan_keihi_meisai.shouhizeigaku is '消費税額';
comment on column ryohiseisan_keihi_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column ryohiseisan_keihi_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column ryohiseisan_keihi_meisai.kousaihi_shousai is '交際費詳細';
comment on column ryohiseisan_keihi_meisai.kousaihi_ninzuu is '交際費人数';
comment on column ryohiseisan_keihi_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column ryohiseisan_keihi_meisai.torihikisaki_cd is '取引先コード';
comment on column ryohiseisan_keihi_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_cd is '借方科目コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_name is '借方科目名';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kari_kazei_kbn is '借方課税区分';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column ryohiseisan_keihi_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_name is '貸方科目名';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column ryohiseisan_keihi_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column ryohiseisan_keihi_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column ryohiseisan_keihi_meisai.uf1_cd is 'UF1コード';
comment on column ryohiseisan_keihi_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column ryohiseisan_keihi_meisai.uf2_cd is 'UF2コード';
comment on column ryohiseisan_keihi_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column ryohiseisan_keihi_meisai.uf3_cd is 'UF3コード';
comment on column ryohiseisan_keihi_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column ryohiseisan_keihi_meisai.uf4_cd is 'UF4コード';
comment on column ryohiseisan_keihi_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column ryohiseisan_keihi_meisai.uf5_cd is 'UF5コード';
comment on column ryohiseisan_keihi_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column ryohiseisan_keihi_meisai.uf6_cd is 'UF6コード';
comment on column ryohiseisan_keihi_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column ryohiseisan_keihi_meisai.uf7_cd is 'UF7コード';
comment on column ryohiseisan_keihi_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column ryohiseisan_keihi_meisai.uf8_cd is 'UF8コード';
comment on column ryohiseisan_keihi_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column ryohiseisan_keihi_meisai.uf9_cd is 'UF9コード';
comment on column ryohiseisan_keihi_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column ryohiseisan_keihi_meisai.uf10_cd is 'UF10コード';
comment on column ryohiseisan_keihi_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column ryohiseisan_keihi_meisai.project_cd is 'プロジェクトコード';
comment on column ryohiseisan_keihi_meisai.project_name is 'プロジェクト名';
comment on column ryohiseisan_keihi_meisai.segment_cd is 'セグメントコード';
comment on column ryohiseisan_keihi_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column ryohiseisan_keihi_meisai.tekiyou_cd is '摘要コード';
comment on column ryohiseisan_keihi_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column ryohiseisan_keihi_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan_keihi_meisai.touroku_time is '登録日時';
comment on column ryohiseisan_keihi_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan_keihi_meisai.koushin_time is '更新日時';
comment on column ryohiseisan_keihi_meisai.shiharaisaki_name is '支払先名';
comment on column ryohiseisan_keihi_meisai.jigyousha_kbn is '事業者区分';
comment on column ryohiseisan_keihi_meisai.bunri_kbn is '分離区分';
comment on column ryohiseisan_keihi_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column ryohiseisan_keihi_meisai.kashi_shiire_kbn is '貸方仕入区分';

comment on table ryohiseisan_meisai is '旅費精算明細';
comment on column ryohiseisan_meisai.denpyou_id is '伝票ID';
comment on column ryohiseisan_meisai.denpyou_edano is '伝票枝番号';
comment on column ryohiseisan_meisai.kikan_from is '期間開始日';
comment on column ryohiseisan_meisai.kikan_to is '期間終了日';
comment on column ryohiseisan_meisai.kyuujitsu_nissuu is '休日日数';
comment on column ryohiseisan_meisai.shubetsu_cd is '種別コード';
comment on column ryohiseisan_meisai.shubetsu1 is '種別１';
comment on column ryohiseisan_meisai.shubetsu2 is '種別２';
comment on column ryohiseisan_meisai.haya_flg is '早フラグ';
comment on column ryohiseisan_meisai.yasu_flg is '安フラグ';
comment on column ryohiseisan_meisai.raku_flg is '楽フラグ';
comment on column ryohiseisan_meisai.koutsuu_shudan is '交通手段';
comment on column ryohiseisan_meisai.shouhyou_shorui_hissu_flg is '証憑書類必須フラグ';
comment on column ryohiseisan_meisai.ryoushuusho_seikyuusho_tou_flg is '領収書・請求書等フラグ';
comment on column ryohiseisan_meisai.naiyou is '内容（旅費精算）';
comment on column ryohiseisan_meisai.bikou is '備考（旅費精算）';
comment on column ryohiseisan_meisai.oufuku_flg is '往復フラグ';
comment on column ryohiseisan_meisai.houjin_card_riyou_flg is '法人カード利用フラグ';
comment on column ryohiseisan_meisai.kaisha_tehai_flg is '会社手配フラグ';
comment on column ryohiseisan_meisai.jidounyuuryoku_flg is '自動入力フラグ';
comment on column ryohiseisan_meisai.nissuu is '日数';
comment on column ryohiseisan_meisai.tanka is '単価';
comment on column ryohiseisan_meisai.suuryou_nyuryoku_type is '数量入力タイプ';
comment on column ryohiseisan_meisai.suuryou is '数量';
comment on column ryohiseisan_meisai.suuryou_kigou is '数量記号';
comment on column ryohiseisan_meisai.meisai_kingaku is '明細金額';
comment on column ryohiseisan_meisai.ic_card_no is 'ICカード番号';
comment on column ryohiseisan_meisai.ic_card_sequence_no is 'ICカードシーケンス番号';
comment on column ryohiseisan_meisai.himoduke_card_meisai is '紐付元カード明細';
comment on column ryohiseisan_meisai.touroku_user_id is '登録ユーザーID';
comment on column ryohiseisan_meisai.touroku_time is '登録日時';
comment on column ryohiseisan_meisai.koushin_user_id is '更新ユーザーID';
comment on column ryohiseisan_meisai.koushin_time is '更新日時';
comment on column ryohiseisan_meisai.shiharaisaki_name is '支払先名';
comment on column ryohiseisan_meisai.jigyousha_kbn is '事業者区分';
comment on column ryohiseisan_meisai.zeinuki_kingaku is '税抜金額';
comment on column ryohiseisan_meisai.shouhizeigaku is '消費税額';
comment on column ryohiseisan_meisai.zeigaku_fix_flg is '税額修正フラグ';

comment on table saiban_kanri is '採番管理';
comment on column saiban_kanri.saiban_kbn is '採番区分';
comment on column saiban_kanri.sequence_val is 'シーケンス値';

comment on table saishuu_syounin_route_ko is '最終承認ルート子';
comment on column saishuu_syounin_route_ko.denpyou_kbn is '伝票区分';
comment on column saishuu_syounin_route_ko.edano is '枝番号';
comment on column saishuu_syounin_route_ko.edaedano is '枝枝番号';
comment on column saishuu_syounin_route_ko.gyoumu_role_id is '業務ロールID';
comment on column saishuu_syounin_route_ko.saishuu_shounin_shori_kengen_name is '最終承認処理権限名';
comment on column saishuu_syounin_route_ko.touroku_user_id is '登録ユーザーID';
comment on column saishuu_syounin_route_ko.touroku_time is '登録日時';
comment on column saishuu_syounin_route_ko.koushin_user_id is '更新ユーザーID';
comment on column saishuu_syounin_route_ko.koushin_time is '更新日時';

comment on table saishuu_syounin_route_oya is '最終承認ルート親';
comment on column saishuu_syounin_route_oya.denpyou_kbn is '伝票区分';
comment on column saishuu_syounin_route_oya.edano is '枝番号';
comment on column saishuu_syounin_route_oya.chuuki_mongon is '注記文言';
comment on column saishuu_syounin_route_oya.yuukou_kigen_from is '有効期限開始日';
comment on column saishuu_syounin_route_oya.yuukou_kigen_to is '有効期限終了日';
comment on column saishuu_syounin_route_oya.touroku_user_id is '登録ユーザーID';
comment on column saishuu_syounin_route_oya.touroku_time is '登録日時';
comment on column saishuu_syounin_route_oya.koushin_user_id is '更新ユーザーID';
comment on column saishuu_syounin_route_oya.koushin_time is '更新日時';

comment on table security_log is 'セキュリティログ';
comment on column security_log.serial_no is 'シリアル番号';
comment on column security_log.event_time is 'イベント時刻';
comment on column security_log.ip is '接続元IPアドレス';
comment on column security_log.ip_xforwarded is 'IPアドレス(X-FORWARDED-FOR)';
comment on column security_log.user_id is 'ユーザーID';
comment on column security_log.gyoumu_role_id is '業務ロールID';
comment on column security_log.target is '操作対象';
comment on column security_log.type is 'ログ種別';
comment on column security_log.detail is 'ログ詳細';

comment on table segment_kamoku_zandaka is 'セグメント科目残高';
comment on column segment_kamoku_zandaka.segment_cd is 'セグメントコード';
comment on column segment_kamoku_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column segment_kamoku_zandaka.kessanki_bangou is '決算期番号';
comment on column segment_kamoku_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column segment_kamoku_zandaka.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column segment_kamoku_zandaka.torihikisaki_name_seishiki is '取引先名（正式）';
comment on column segment_kamoku_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column segment_kamoku_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column segment_kamoku_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column segment_kamoku_zandaka.taishaku_zokusei is '貸借属性';
comment on column segment_kamoku_zandaka.kishu_zandaka is '期首残高';

comment on table segment_master is 'セグメントマスター';
comment on column segment_master.segment_cd is 'セグメントコード';
comment on column segment_master.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column segment_master.segment_name_seishiki is 'セグメント名（正式）';

comment on table seikyuushobarai is '請求書払い';
comment on column seikyuushobarai.denpyou_id is '伝票ID';
comment on column seikyuushobarai.keijoubi is '計上日';
comment on column seikyuushobarai.shiharai_kigen is '支払期限';
comment on column seikyuushobarai.shiharaibi is '支払日';
comment on column seikyuushobarai.masref_flg is 'マスター参照フラグ';
comment on column seikyuushobarai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column seikyuushobarai.kake_flg is '掛けフラグ';
comment on column seikyuushobarai.hontai_kingaku_goukei is '本体金額合計';
comment on column seikyuushobarai.shouhizeigaku_goukei is '消費税額合計';
comment on column seikyuushobarai.shiharai_kingaku_goukei is '支払金額合計';
comment on column seikyuushobarai.hf1_cd is 'HF1コード';
comment on column seikyuushobarai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column seikyuushobarai.hf2_cd is 'HF2コード';
comment on column seikyuushobarai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column seikyuushobarai.hf3_cd is 'HF3コード';
comment on column seikyuushobarai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column seikyuushobarai.hf4_cd is 'HF4コード';
comment on column seikyuushobarai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column seikyuushobarai.hf5_cd is 'HF5コード';
comment on column seikyuushobarai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column seikyuushobarai.hf6_cd is 'HF6コード';
comment on column seikyuushobarai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column seikyuushobarai.hf7_cd is 'HF7コード';
comment on column seikyuushobarai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column seikyuushobarai.hf8_cd is 'HF8コード';
comment on column seikyuushobarai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column seikyuushobarai.hf9_cd is 'HF9コード';
comment on column seikyuushobarai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column seikyuushobarai.hf10_cd is 'HF10コード';
comment on column seikyuushobarai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column seikyuushobarai.hosoku is '補足';
comment on column seikyuushobarai.touroku_user_id is '登録ユーザーID';
comment on column seikyuushobarai.touroku_time is '登録日時';
comment on column seikyuushobarai.koushin_user_id is '更新ユーザーID';
comment on column seikyuushobarai.koushin_time is '更新日時';
comment on column seikyuushobarai.nyuryoku_houshiki is '入力方式';
comment on column seikyuushobarai.invoice_denpyou is 'インボイス対応伝票';

comment on table seikyuushobarai_meisai is '請求書払い明細';
comment on column seikyuushobarai_meisai.denpyou_id is '伝票ID';
comment on column seikyuushobarai_meisai.denpyou_edano is '伝票枝番号';
comment on column seikyuushobarai_meisai.shiwake_edano is '仕訳枝番号';
comment on column seikyuushobarai_meisai.torihiki_name is '取引名';
comment on column seikyuushobarai_meisai.tekiyou is '摘要';
comment on column seikyuushobarai_meisai.zeiritsu is '税率';
comment on column seikyuushobarai_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column seikyuushobarai_meisai.shiharai_kingaku is '支払金額';
comment on column seikyuushobarai_meisai.hontai_kingaku is '本体金額';
comment on column seikyuushobarai_meisai.shouhizeigaku is '消費税額';
comment on column seikyuushobarai_meisai.kousaihi_shousai_hyouji_flg is '交際費詳細表示フラグ';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column seikyuushobarai_meisai.kousaihi_shousai is '交際費詳細';
comment on column seikyuushobarai_meisai.kousaihi_ninzuu is '交際費人数';
comment on column seikyuushobarai_meisai.kousaihi_hitori_kingaku is '交際費一人あたり金額';
comment on column seikyuushobarai_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column seikyuushobarai_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column seikyuushobarai_meisai.torihikisaki_cd is '取引先コード';
comment on column seikyuushobarai_meisai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column seikyuushobarai_meisai.furikomisaki_jouhou is '振込先情報';
comment on column seikyuushobarai_meisai.kari_kamoku_cd is '借方科目コード';
comment on column seikyuushobarai_meisai.kari_kamoku_name is '借方科目名';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column seikyuushobarai_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column seikyuushobarai_meisai.kari_kazei_kbn is '借方課税区分';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column seikyuushobarai_meisai.kashi_futan_bumon_name is '貸方負担部門名';
comment on column seikyuushobarai_meisai.kashi_kamoku_cd is '貸方科目コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_name is '貸方科目名';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column seikyuushobarai_meisai.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column seikyuushobarai_meisai.kashi_kazei_kbn is '貸方課税区分';
comment on column seikyuushobarai_meisai.uf1_cd is 'UF1コード';
comment on column seikyuushobarai_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column seikyuushobarai_meisai.uf2_cd is 'UF2コード';
comment on column seikyuushobarai_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column seikyuushobarai_meisai.uf3_cd is 'UF3コード';
comment on column seikyuushobarai_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column seikyuushobarai_meisai.uf4_cd is 'UF4コード';
comment on column seikyuushobarai_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column seikyuushobarai_meisai.uf5_cd is 'UF5コード';
comment on column seikyuushobarai_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column seikyuushobarai_meisai.uf6_cd is 'UF6コード';
comment on column seikyuushobarai_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column seikyuushobarai_meisai.uf7_cd is 'UF7コード';
comment on column seikyuushobarai_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column seikyuushobarai_meisai.uf8_cd is 'UF8コード';
comment on column seikyuushobarai_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column seikyuushobarai_meisai.uf9_cd is 'UF9コード';
comment on column seikyuushobarai_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column seikyuushobarai_meisai.uf10_cd is 'UF10コード';
comment on column seikyuushobarai_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column seikyuushobarai_meisai.project_cd is 'プロジェクトコード';
comment on column seikyuushobarai_meisai.project_name is 'プロジェクト名';
comment on column seikyuushobarai_meisai.segment_cd is 'セグメントコード';
comment on column seikyuushobarai_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column seikyuushobarai_meisai.tekiyou_cd is '摘要コード';
comment on column seikyuushobarai_meisai.touroku_user_id is '登録ユーザーID';
comment on column seikyuushobarai_meisai.touroku_time is '登録日時';
comment on column seikyuushobarai_meisai.koushin_user_id is '更新ユーザーID';
comment on column seikyuushobarai_meisai.koushin_time is '更新日時';
comment on column seikyuushobarai_meisai.jigyousha_kbn is '事業者区分';
comment on column seikyuushobarai_meisai.bunri_kbn is '分離区分';
comment on column seikyuushobarai_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column seikyuushobarai_meisai.kashi_shiire_kbn is '貸方仕入区分';

comment on table setting_info is '設定情報';
comment on column setting_info.setting_name is '設定名';
comment on column setting_info.setting_name_wa is '設定名（和名）';
comment on column setting_info.setting_val is '設定値';
comment on column setting_info.category is 'カテゴリ';
comment on column setting_info.hyouji_jun is '表示順';
comment on column setting_info.editable_flg is '変更可能フラグ';
comment on column setting_info.hissu_flg is '必須フラグ';
comment on column setting_info.format_regex is 'フォーマット正規表現';
comment on column setting_info.description is '説明';

comment on table shain is '社員';
comment on column shain.shain_no is '社員番号';
comment on column shain.user_full_name is 'ユーザーフル名';
comment on column shain.daihyou_futan_bumon_cd is '代表負担部門コード';
comment on column shain.yakushoku_cd is '役職コード';

comment on table shain_kouza is '社員口座';
comment on column shain_kouza.shain_no is '社員番号';
comment on column shain_kouza.saki_kinyuukikan_cd is '振込先金融機関銀行コード';
comment on column shain_kouza.saki_ginkou_shiten_cd is '振込先銀行支店コード';
comment on column shain_kouza.saki_yokin_shabetsu is '振込先預金種別';
comment on column shain_kouza.saki_kouza_bangou is '振込先口座番号';
comment on column shain_kouza.saki_kouza_meigi_kanji is '振込先口座名義漢字';
comment on column shain_kouza.saki_kouza_meigi_kana is '振込先口座名義（半角カナ）';
comment on column shain_kouza.moto_kinyuukikan_cd is '振込元金融機関コード';
comment on column shain_kouza.moto_kinyuukikan_shiten_cd is '振込元金融機関支店コード';
comment on column shain_kouza.moto_yokinshubetsu is '振込元預金種別';
comment on column shain_kouza.moto_kouza_bangou is '振込元口座番号';
comment on column shain_kouza.zaimu_edaban_cd is '財務枝番コード';

comment on table shiharai_irai is '支払依頼';
comment on column shiharai_irai.denpyou_id is '伝票ID';
comment on column shiharai_irai.keijoubi is '計上日';
comment on column shiharai_irai.yoteibi is '予定日';
comment on column shiharai_irai.shiharaibi is '支払日';
comment on column shiharai_irai.shiharai_kijitsu is '支払期日';
comment on column shiharai_irai.torihikisaki_cd is '取引先コード';
comment on column shiharai_irai.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column shiharai_irai.ichigensaki_torihikisaki_name is '一見先取引先名';
comment on column shiharai_irai.edi is 'EDI';
comment on column shiharai_irai.shiharai_goukei is '支払合計';
comment on column shiharai_irai.sousai_goukei is '相殺合計';
comment on column shiharai_irai.sashihiki_shiharai is '差引支払額';
comment on column shiharai_irai.manekin_gensen is '控除項目';
comment on column shiharai_irai.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column shiharai_irai.hf1_cd is 'HF1コード';
comment on column shiharai_irai.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column shiharai_irai.hf2_cd is 'HF2コード';
comment on column shiharai_irai.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column shiharai_irai.hf3_cd is 'HF3コード';
comment on column shiharai_irai.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column shiharai_irai.hf4_cd is 'HF4コード';
comment on column shiharai_irai.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column shiharai_irai.hf5_cd is 'HF5コード';
comment on column shiharai_irai.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column shiharai_irai.hf6_cd is 'HF6コード';
comment on column shiharai_irai.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column shiharai_irai.hf7_cd is 'HF7コード';
comment on column shiharai_irai.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column shiharai_irai.hf8_cd is 'HF8コード';
comment on column shiharai_irai.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column shiharai_irai.hf9_cd is 'HF9コード';
comment on column shiharai_irai.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column shiharai_irai.hf10_cd is 'HF10コード';
comment on column shiharai_irai.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column shiharai_irai.shiharai_houhou is '支払方法';
comment on column shiharai_irai.shiharai_shubetsu is '支払種別';
comment on column shiharai_irai.furikomi_ginkou_cd is '振込銀行コード';
comment on column shiharai_irai.furikomi_ginkou_name is '振込銀行名称';
comment on column shiharai_irai.furikomi_ginkou_shiten_cd is '振込銀行支店コード';
comment on column shiharai_irai.furikomi_ginkou_shiten_name is '振込銀行支店名称';
comment on column shiharai_irai.yokin_shubetsu is '預金種別';
comment on column shiharai_irai.kouza_bangou is '口座番号';
comment on column shiharai_irai.kouza_meiginin is '口座名義人';
comment on column shiharai_irai.tesuuryou is '手数料';
comment on column shiharai_irai.hosoku is '補足';
comment on column shiharai_irai.gyaku_shiwake_flg is '逆仕訳フラグ';
comment on column shiharai_irai.shutsuryoku_flg is '出力フラグ';
comment on column shiharai_irai.csv_upload_flg is 'CSVアップロードフラグ';
comment on column shiharai_irai.hassei_shubetsu is '発生種別';
comment on column shiharai_irai.saimu_made_flg is '債務支払データ作成済フラグ';
comment on column shiharai_irai.fb_made_flg is 'FBデータ作成済フラグ';
comment on column shiharai_irai.touroku_user_id is '登録ユーザーID';
comment on column shiharai_irai.touroku_time is '登録日時';
comment on column shiharai_irai.koushin_user_id is '更新ユーザーID';
comment on column shiharai_irai.koushin_time is '更新日時';
comment on column shiharai_irai.nyuryoku_houshiki is '入力方式';
comment on column shiharai_irai.jigyousha_kbn is '事業者区分';
comment on column shiharai_irai.jigyousha_no is '事業者登録番号';
comment on column shiharai_irai.invoice_denpyou is 'インボイス対応伝票';

comment on table shiharai_irai_meisai is '支払依頼明細';
comment on column shiharai_irai_meisai.denpyou_id is '伝票ID';
comment on column shiharai_irai_meisai.denpyou_edano is '伝票枝番号';
comment on column shiharai_irai_meisai.shiwake_edano is '仕訳枝番号';
comment on column shiharai_irai_meisai.torihiki_name is '取引名';
comment on column shiharai_irai_meisai.tekiyou is '摘要';
comment on column shiharai_irai_meisai.shiharai_kingaku is '支払金額';
comment on column shiharai_irai_meisai.kari_futan_bumon_cd is '借方負担部門コード';
comment on column shiharai_irai_meisai.kari_futan_bumon_name is '借方負担部門名';
comment on column shiharai_irai_meisai.kari_kamoku_cd is '借方科目コード';
comment on column shiharai_irai_meisai.kari_kamoku_name is '借方科目名';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column shiharai_irai_meisai.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column shiharai_irai_meisai.kari_kazei_kbn is '借方課税区分';
comment on column shiharai_irai_meisai.zeiritsu is '税率';
comment on column shiharai_irai_meisai.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column shiharai_irai_meisai.uf1_cd is 'UF1コード';
comment on column shiharai_irai_meisai.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column shiharai_irai_meisai.uf2_cd is 'UF2コード';
comment on column shiharai_irai_meisai.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column shiharai_irai_meisai.uf3_cd is 'UF3コード';
comment on column shiharai_irai_meisai.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column shiharai_irai_meisai.uf4_cd is 'UF4コード';
comment on column shiharai_irai_meisai.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column shiharai_irai_meisai.uf5_cd is 'UF5コード';
comment on column shiharai_irai_meisai.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column shiharai_irai_meisai.uf6_cd is 'UF6コード';
comment on column shiharai_irai_meisai.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column shiharai_irai_meisai.uf7_cd is 'UF7コード';
comment on column shiharai_irai_meisai.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column shiharai_irai_meisai.uf8_cd is 'UF8コード';
comment on column shiharai_irai_meisai.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column shiharai_irai_meisai.uf9_cd is 'UF9コード';
comment on column shiharai_irai_meisai.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column shiharai_irai_meisai.uf10_cd is 'UF10コード';
comment on column shiharai_irai_meisai.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column shiharai_irai_meisai.project_cd is 'プロジェクトコード';
comment on column shiharai_irai_meisai.project_name is 'プロジェクト名';
comment on column shiharai_irai_meisai.segment_cd is 'セグメントコード';
comment on column shiharai_irai_meisai.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column shiharai_irai_meisai.tekiyou_cd is '摘要コード';
comment on column shiharai_irai_meisai.touroku_user_id is '登録ユーザーID';
comment on column shiharai_irai_meisai.touroku_time is '登録日時';
comment on column shiharai_irai_meisai.koushin_user_id is '更新ユーザーID';
comment on column shiharai_irai_meisai.koushin_time is '更新日時';
comment on column shiharai_irai_meisai.bunri_kbn is '分離区分';
comment on column shiharai_irai_meisai.kari_shiire_kbn is '借方仕入区分';
comment on column shiharai_irai_meisai.zeinuki_kingaku is '税抜金額';
comment on column shiharai_irai_meisai.shouhizeigaku is '消費税額';

comment on table shikkou_joukyou_ichiran_jouhou is '執行状況一覧情報';
comment on column shikkou_joukyou_ichiran_jouhou.user_id is 'ユーザーID';
comment on column shikkou_joukyou_ichiran_jouhou.yosan_tani is '予算単位';

comment on table shiwake_de3 is '仕訳抽出(de3)';
comment on column shiwake_de3.serial_no is 'シリアル番号';
comment on column shiwake_de3.denpyou_id is '伝票ID';
comment on column shiwake_de3.shiwake_status is '仕訳抽出状態';
comment on column shiwake_de3.touroku_time is '登録日時';
comment on column shiwake_de3.koushin_time is '更新日時';
comment on column shiwake_de3.dymd is '（オープン２１）伝票日付';
comment on column shiwake_de3.seiri is '（オープン２１）整理月フラグ';
comment on column shiwake_de3.dcno is '（オープン２１）伝票番号';
comment on column shiwake_de3.rbmn is '（オープン２１）借方　部門コード';
comment on column shiwake_de3.rtor is '（オープン２１）借方　取引先コード';
comment on column shiwake_de3.rkmk is '（オープン２１）借方　科目コード';
comment on column shiwake_de3.reda is '（オープン２１）借方　枝番コード';
comment on column shiwake_de3.rkoj is '（オープン２１）借方　工事コード';
comment on column shiwake_de3.rkos is '（オープン２１）借方　工種コード';
comment on column shiwake_de3.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column shiwake_de3.rseg is '（オープン２１）借方　セグメントコード';
comment on column shiwake_de3.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column shiwake_de3.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column shiwake_de3.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column shiwake_de3.tky is '（オープン２１）摘要';
comment on column shiwake_de3.tno is '（オープン２１）摘要コード';
comment on column shiwake_de3.sbmn is '（オープン２１）貸方　部門コード';
comment on column shiwake_de3.stor is '（オープン２１）貸方　取引先コード';
comment on column shiwake_de3.skmk is '（オープン２１）貸方　科目コード';
comment on column shiwake_de3.seda is '（オープン２１）貸方　枝番コード';
comment on column shiwake_de3.skoj is '（オープン２１）貸方　工事コード';
comment on column shiwake_de3.skos is '（オープン２１）貸方　工種コード';
comment on column shiwake_de3.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column shiwake_de3.sseg is '（オープン２１）貸方　セグメントコード';
comment on column shiwake_de3.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column shiwake_de3.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column shiwake_de3.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column shiwake_de3.exvl is '（オープン２１）対価金額';
comment on column shiwake_de3.valu is '（オープン２１）金額';
comment on column shiwake_de3.zkmk is '（オープン２１）消費税対象科目コード';
comment on column shiwake_de3.zrit is '（オープン２１）消費税対象科目税率';
comment on column shiwake_de3.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column shiwake_de3.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column shiwake_de3.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column shiwake_de3.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column shiwake_de3.rrit is '（オープン２１）借方　税率';
comment on column shiwake_de3.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column shiwake_de3.srit is '（オープン２１）貸方　税率';
comment on column shiwake_de3.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column shiwake_de3.rzkb is '（オープン２１）借方　課税区分';
comment on column shiwake_de3.rgyo is '（オープン２１）借方　業種区分';
comment on column shiwake_de3.rsre is '（オープン２１）借方　仕入区分';
comment on column shiwake_de3.szkb is '（オープン２１）貸方　課税区分';
comment on column shiwake_de3.sgyo is '（オープン２１）貸方　業種区分';
comment on column shiwake_de3.ssre is '（オープン２１）貸方　仕入区分';
comment on column shiwake_de3.symd is '（オープン２１）支払日';
comment on column shiwake_de3.skbn is '（オープン２１）支払区分';
comment on column shiwake_de3.skiz is '（オープン２１）支払期日';
comment on column shiwake_de3.uymd is '（オープン２１）回収日';
comment on column shiwake_de3.ukbn is '（オープン２１）入金区分';
comment on column shiwake_de3.ukiz is '（オープン２１）回収期日';
comment on column shiwake_de3.sten is '（オープン２１）店券フラグ';
comment on column shiwake_de3.dkec is '（オープン２１）消込コード';
comment on column shiwake_de3.kymd is '（オープン２１）起票年月日';
comment on column shiwake_de3.kbmn is '（オープン２１）起票部門コード';
comment on column shiwake_de3.kusr is '（オープン２１）起票者コード';
comment on column shiwake_de3.fusr is '（オープン２１）入力者コード';
comment on column shiwake_de3.fsen is '（オープン２１）付箋番号';
comment on column shiwake_de3.sgno is '（オープン２１）承認グループNo.';
comment on column shiwake_de3.bunri is '（オープン２１）分離区分';
comment on column shiwake_de3.rate is '（オープン２１）レート';
comment on column shiwake_de3.gexvl is '（オープン２１）外貨対価金額';
comment on column shiwake_de3.gvalu is '（オープン２１）外貨金額';
comment on column shiwake_de3.gsep is '（オープン２１）行区切り';
comment on column shiwake_de3.rurizeikeisan is '（オープン２１）借方　併用売上税額計算方式';
comment on column shiwake_de3.surizeikeisan is '（オープン２１）貸方　併用売上税額計算方式';
comment on column shiwake_de3.zurizeikeisan is '（オープン２１）税対象科目　併用売上税額計算方式';
comment on column shiwake_de3.rmenzeikeika is '（オープン２１）借方　仕入税額控除経過措置割合';
comment on column shiwake_de3.smenzeikeika is '（オープン２１）貸方　仕入税額控除経過措置割合';
comment on column shiwake_de3.zmenzeikeika is '（オープン２１）税対象科目　仕入税額控除経過措置割合';

comment on table shiwake_pattern_master is '仕訳パターンマスター';
comment on column shiwake_pattern_master.denpyou_kbn is '伝票区分';
comment on column shiwake_pattern_master.shiwake_edano is '仕訳枝番号';
comment on column shiwake_pattern_master.delete_flg is '削除フラグ';
comment on column shiwake_pattern_master.yuukou_kigen_from is '有効期限開始日';
comment on column shiwake_pattern_master.yuukou_kigen_to is '有効期限終了日';
comment on column shiwake_pattern_master.bunrui1 is '分類1';
comment on column shiwake_pattern_master.bunrui2 is '分類2';
comment on column shiwake_pattern_master.bunrui3 is '分類3';
comment on column shiwake_pattern_master.torihiki_name is '取引名';
comment on column shiwake_pattern_master.tekiyou_flg is '摘要フラグ';
comment on column shiwake_pattern_master.tekiyou is '摘要';
comment on column shiwake_pattern_master.default_hyouji_flg is 'デフォルト表示フラグ';
comment on column shiwake_pattern_master.kousaihi_hyouji_flg is '交際費表示フラグ';
comment on column shiwake_pattern_master.kousaihi_ninzuu_riyou_flg is '人数項目表示フラグ';
comment on column shiwake_pattern_master.kousaihi_kijun_gaku is '交際費基準額';
comment on column shiwake_pattern_master.kousaihi_check_houhou is '交際費チェック方法';
comment on column shiwake_pattern_master.kousaihi_check_result is '交際費チェック後登録許可';
comment on column shiwake_pattern_master.kake_flg is '掛けフラグ';
comment on column shiwake_pattern_master.hyouji_jun is '表示順';
comment on column shiwake_pattern_master.shain_cd_renkei_flg is '社員コード連携フラグ';
comment on column shiwake_pattern_master.edaban_renkei_flg is '財務枝番コード連携フラグ';
comment on column shiwake_pattern_master.kari_futan_bumon_cd is '借方負担部門コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_cd is '借方科目コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_kamoku_edaban_cd is '借方科目枝番コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_torihikisaki_cd is '借方取引先コード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_project_cd is '借方プロジェクトコード（仕訳パターン）';
comment on column shiwake_pattern_master.kari_segment_cd is '借方セグメントコード';
comment on column shiwake_pattern_master.kari_uf1_cd is '借方UF1コード';
comment on column shiwake_pattern_master.kari_uf2_cd is '借方UF2コード';
comment on column shiwake_pattern_master.kari_uf3_cd is '借方UF3コード';
comment on column shiwake_pattern_master.kari_uf4_cd is '借方UF4コード';
comment on column shiwake_pattern_master.kari_uf5_cd is '借方UF5コード';
comment on column shiwake_pattern_master.kari_uf6_cd is '借方UF6コード';
comment on column shiwake_pattern_master.kari_uf7_cd is '借方UF7コード';
comment on column shiwake_pattern_master.kari_uf8_cd is '借方UF8コード';
comment on column shiwake_pattern_master.kari_uf9_cd is '借方UF9コード';
comment on column shiwake_pattern_master.kari_uf10_cd is '借方UF10コード';
comment on column shiwake_pattern_master.kari_uf_kotei1_cd is '借方UF1コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei2_cd is '借方UF2コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei3_cd is '借方UF3コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei4_cd is '借方UF4コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei5_cd is '借方UF5コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei6_cd is '借方UF6コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei7_cd is '借方UF7コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei8_cd is '借方UF8コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei9_cd is '借方UF9コード（固定）';
comment on column shiwake_pattern_master.kari_uf_kotei10_cd is '借方UF10コード（固定）';
comment on column shiwake_pattern_master.kari_kazei_kbn is '借方課税区分（仕訳パターン）';
comment on column shiwake_pattern_master.kari_zeiritsu is '借方消費税率（仕訳パターン）';
comment on column shiwake_pattern_master.kari_keigen_zeiritsu_kbn is '借方軽減税率区分（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd1 is '貸方負担部門コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd1 is '貸方科目コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd1 is '貸方科目枝番コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd1 is '貸方取引先コード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd1 is '貸方プロジェクトコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd1 is '貸方セグメントコード１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd1 is '貸方UF1コード１';
comment on column shiwake_pattern_master.kashi_uf2_cd1 is '貸方UF2コード１';
comment on column shiwake_pattern_master.kashi_uf3_cd1 is '貸方UF3コード１';
comment on column shiwake_pattern_master.kashi_uf4_cd1 is '貸方UF4コード１';
comment on column shiwake_pattern_master.kashi_uf5_cd1 is '貸方UF5コード１';
comment on column shiwake_pattern_master.kashi_uf6_cd1 is '貸方UF6コード１';
comment on column shiwake_pattern_master.kashi_uf7_cd1 is '貸方UF7コード１';
comment on column shiwake_pattern_master.kashi_uf8_cd1 is '貸方UF8コード１';
comment on column shiwake_pattern_master.kashi_uf9_cd1 is '貸方UF9コード１';
comment on column shiwake_pattern_master.kashi_uf10_cd1 is '貸方UF10コード１';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd1 is '貸方UF1コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd1 is '貸方UF2コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd1 is '貸方UF3コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd1 is '貸方UF4コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd1 is '貸方UF5コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd1 is '貸方UF6コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd1 is '貸方UF7コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd1 is '貸方UF8コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd1 is '貸方UF9コード１（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd1 is '貸方UF10コード１（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn1 is '貸方課税区分１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd2 is '貸方負担部門コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd2 is '貸方取引先コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd2 is '貸方科目コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd2 is '貸方科目枝番コード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd2 is '貸方プロジェクトコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd2 is '貸方セグメントコード２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd2 is '貸方UF1コード２';
comment on column shiwake_pattern_master.kashi_uf2_cd2 is '貸方UF2コード２';
comment on column shiwake_pattern_master.kashi_uf3_cd2 is '貸方UF3コード２';
comment on column shiwake_pattern_master.kashi_uf4_cd2 is '貸方UF4コード２';
comment on column shiwake_pattern_master.kashi_uf5_cd2 is '貸方UF5コード２';
comment on column shiwake_pattern_master.kashi_uf6_cd2 is '貸方UF6コード２';
comment on column shiwake_pattern_master.kashi_uf7_cd2 is '貸方UF7コード２';
comment on column shiwake_pattern_master.kashi_uf8_cd2 is '貸方UF8コード２';
comment on column shiwake_pattern_master.kashi_uf9_cd2 is '貸方UF9コード２';
comment on column shiwake_pattern_master.kashi_uf10_cd2 is '貸方UF10コード２';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd2 is '貸方UF1コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd2 is '貸方UF2コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd2 is '貸方UF3コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd2 is '貸方UF4コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd2 is '貸方UF5コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd2 is '貸方UF6コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd2 is '貸方UF7コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd2 is '貸方UF8コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd2 is '貸方UF9コード２（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd2 is '貸方UF10コード２（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn2 is '貸方課税区分２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd3 is '貸方負担部門コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd3 is '貸方取引先コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd3 is '貸方科目コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd3 is '貸方科目枝番コード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd3 is '貸方プロジェクトコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd3 is '貸方セグメントコード３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd3 is '貸方UF1コード３';
comment on column shiwake_pattern_master.kashi_uf2_cd3 is '貸方UF2コード３';
comment on column shiwake_pattern_master.kashi_uf3_cd3 is '貸方UF3コード３';
comment on column shiwake_pattern_master.kashi_uf4_cd3 is '貸方UF4コード３';
comment on column shiwake_pattern_master.kashi_uf5_cd3 is '貸方UF5コード３';
comment on column shiwake_pattern_master.kashi_uf6_cd3 is '貸方UF6コード３';
comment on column shiwake_pattern_master.kashi_uf7_cd3 is '貸方UF7コード３';
comment on column shiwake_pattern_master.kashi_uf8_cd3 is '貸方UF8コード３';
comment on column shiwake_pattern_master.kashi_uf9_cd3 is '貸方UF9コード３';
comment on column shiwake_pattern_master.kashi_uf10_cd3 is '貸方UF10コード３';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd3 is '貸方UF1コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd3 is '貸方UF2コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd3 is '貸方UF3コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd3 is '貸方UF4コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd3 is '貸方UF5コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd3 is '貸方UF6コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd3 is '貸方UF7コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd3 is '貸方UF8コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd3 is '貸方UF9コード３（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd3 is '貸方UF10コード３（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn3 is '貸方課税区分３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd4 is '貸方負担部門コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd4 is '貸方取引先コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd4 is '貸方科目コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd4 is '貸方科目枝番コード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd4 is '貸方プロジェクトコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd4 is '貸方セグメントコード４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd4 is '貸方UF1コード４';
comment on column shiwake_pattern_master.kashi_uf2_cd4 is '貸方UF2コード４';
comment on column shiwake_pattern_master.kashi_uf3_cd4 is '貸方UF3コード４';
comment on column shiwake_pattern_master.kashi_uf4_cd4 is '貸方UF4コード４';
comment on column shiwake_pattern_master.kashi_uf5_cd4 is '貸方UF5コード４';
comment on column shiwake_pattern_master.kashi_uf6_cd4 is '貸方UF6コード４';
comment on column shiwake_pattern_master.kashi_uf7_cd4 is '貸方UF7コード４';
comment on column shiwake_pattern_master.kashi_uf8_cd4 is '貸方UF8コード４';
comment on column shiwake_pattern_master.kashi_uf9_cd4 is '貸方UF9コード４';
comment on column shiwake_pattern_master.kashi_uf10_cd4 is '貸方UF10コード４';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd4 is '貸方UF1コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd4 is '貸方UF2コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd4 is '貸方UF3コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd4 is '貸方UF4コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd4 is '貸方UF5コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd4 is '貸方UF6コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd4 is '貸方UF7コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd4 is '貸方UF8コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd4 is '貸方UF9コード４（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd4 is '貸方UF10コード４（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn4 is '貸方課税区分４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_futan_bumon_cd5 is '貸方負担部門コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_torihikisaki_cd5 is '貸方取引先コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_cd5 is '貸方科目コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_kamoku_edaban_cd5 is '貸方科目枝番コード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_project_cd5 is '貸方プロジェクトコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_segment_cd5 is '貸方セグメントコード５（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_uf1_cd5 is '貸方UF1コード５';
comment on column shiwake_pattern_master.kashi_uf2_cd5 is '貸方UF2コード５';
comment on column shiwake_pattern_master.kashi_uf3_cd5 is '貸方UF3コード５';
comment on column shiwake_pattern_master.kashi_uf4_cd5 is '貸方UF4コード５';
comment on column shiwake_pattern_master.kashi_uf5_cd5 is '貸方UF5コード５';
comment on column shiwake_pattern_master.kashi_uf6_cd5 is '貸方UF6コード５';
comment on column shiwake_pattern_master.kashi_uf7_cd5 is '貸方UF7コード５';
comment on column shiwake_pattern_master.kashi_uf8_cd5 is '貸方UF8コード５';
comment on column shiwake_pattern_master.kashi_uf9_cd5 is '貸方UF9コード５';
comment on column shiwake_pattern_master.kashi_uf10_cd5 is '貸方UF10コード５';
comment on column shiwake_pattern_master.kashi_uf_kotei1_cd5 is '貸方UF1コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei2_cd5 is '貸方UF2コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei3_cd5 is '貸方UF3コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei4_cd5 is '貸方UF4コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei5_cd5 is '貸方UF5コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei6_cd5 is '貸方UF6コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei7_cd5 is '貸方UF7コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei8_cd5 is '貸方UF8コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei9_cd5 is '貸方UF9コード５（固定）';
comment on column shiwake_pattern_master.kashi_uf_kotei10_cd5 is '貸方UF10コード５（固定）';
comment on column shiwake_pattern_master.kashi_kazei_kbn5 is '貸方課税区分５（仕訳パターン）';
comment on column shiwake_pattern_master.touroku_user_id is '登録ユーザーID';
comment on column shiwake_pattern_master.touroku_time is '登録日時';
comment on column shiwake_pattern_master.koushin_user_id is '更新ユーザーID';
comment on column shiwake_pattern_master.koushin_time is '更新日時';
comment on column shiwake_pattern_master.old_kari_kazei_kbn is '旧借方課税区分（仕訳パターン）';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn1 is '旧貸方課税区分１（仕訳パターン）';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn2 is '旧貸方課税区分２（仕訳パターン）';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn3 is '旧貸方課税区分３（仕訳パターン）';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn4 is '旧貸方課税区分４（仕訳パターン）';
comment on column shiwake_pattern_master.old_kashi_kazei_kbn5 is '旧貸方課税区分５（仕訳パターン）';
comment on column shiwake_pattern_master.kari_bunri_kbn is '借方分離区分（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_bunri_kbn1 is '貸方分離区分１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_bunri_kbn2 is '貸方分離区分２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_bunri_kbn3 is '貸方分離区分３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_bunri_kbn4 is '貸方分離区分４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_bunri_kbn5 is '貸方分離区分５（仕訳パターン）';
comment on column shiwake_pattern_master.kari_shiire_kbn is '借方仕入区分（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_shiire_kbn1 is '貸方仕入区分１（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_shiire_kbn2 is '貸方仕入区分２（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_shiire_kbn3 is '貸方仕入区分３（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_shiire_kbn4 is '貸方仕入区分４（仕訳パターン）';
comment on column shiwake_pattern_master.kashi_shiire_kbn5 is '貸方仕入区分５（仕訳パターン）';

comment on table shiwake_pattern_setting is '仕訳パターン設定';
comment on column shiwake_pattern_setting.denpyou_kbn is '伝票区分';
comment on column shiwake_pattern_setting.setting_kbn is '設定区分';
comment on column shiwake_pattern_setting.setting_item is '設定項目';
comment on column shiwake_pattern_setting.default_value is 'デフォルト値';
comment on column shiwake_pattern_setting.hyouji_flg is '表示フラグ';
comment on column shiwake_pattern_setting.shiwake_pattern_var1 is '仕訳パターン変数1';
comment on column shiwake_pattern_setting.shiwake_pattern_var2 is '仕訳パターン変数2';
comment on column shiwake_pattern_setting.shiwake_pattern_var3 is '仕訳パターン変数3';
comment on column shiwake_pattern_setting.shiwake_pattern_var4 is '仕訳パターン変数4';
comment on column shiwake_pattern_setting.shiwake_pattern_var5 is '仕訳パターン変数5';

comment on table shiwake_pattern_var_setting is '仕訳パターン変数設定';
comment on column shiwake_pattern_var_setting.shiwake_pattern_var is '仕訳パターン変数';
comment on column shiwake_pattern_var_setting.shiwake_pattern_var_name is '仕訳パターン変数名称';
comment on column shiwake_pattern_var_setting.var_setsumei is '変数説明';
comment on column shiwake_pattern_var_setting.session_var is 'セッション変数名';
comment on column shiwake_pattern_var_setting.column_name is 'カラム名';
comment on column shiwake_pattern_var_setting.hyouji_jun is '表示順';

comment on table shiwake_sias is '仕訳抽出(SIAS)';
comment on column shiwake_sias.serial_no is 'シリアル番号';
comment on column shiwake_sias.denpyou_id is '伝票ID';
comment on column shiwake_sias.shiwake_status is '仕訳抽出状態';
comment on column shiwake_sias.touroku_time is '登録日時';
comment on column shiwake_sias.koushin_time is '更新日時';
comment on column shiwake_sias.dymd is '（オープン２１）伝票日付';
comment on column shiwake_sias.seiri is '（オープン２１）整理月フラグ';
comment on column shiwake_sias.dcno is '（オープン２１）伝票番号';
comment on column shiwake_sias.kymd is '（オープン２１）起票年月日';
comment on column shiwake_sias.kbmn is '（オープン２１）起票部門コード';
comment on column shiwake_sias.kusr is '（オープン２１）起票者コード';
comment on column shiwake_sias.sgno is '（オープン２１）承認グループNo.';
comment on column shiwake_sias.hf1 is '（オープン２１）ヘッダーフィールド１';
comment on column shiwake_sias.hf2 is '（オープン２１）ヘッダーフィールド２';
comment on column shiwake_sias.hf3 is '（オープン２１）ヘッダーフィールド３';
comment on column shiwake_sias.hf4 is '（オープン２１）ヘッダーフィールド４';
comment on column shiwake_sias.hf5 is '（オープン２１）ヘッダーフィールド５';
comment on column shiwake_sias.hf6 is '（オープン２１）ヘッダーフィールド６';
comment on column shiwake_sias.hf7 is '（オープン２１）ヘッダーフィールド７';
comment on column shiwake_sias.hf8 is '（オープン２１）ヘッダーフィールド８';
comment on column shiwake_sias.hf9 is '（オープン２１）ヘッダーフィールド９';
comment on column shiwake_sias.hf10 is '（オープン２１）ヘッダーフィールド１０';
comment on column shiwake_sias.rbmn is '（オープン２１）借方　部門コード';
comment on column shiwake_sias.rtor is '（オープン２１）借方　取引先コード';
comment on column shiwake_sias.rkmk is '（オープン２１）借方　科目コード';
comment on column shiwake_sias.reda is '（オープン２１）借方　枝番コード';
comment on column shiwake_sias.rkoj is '（オープン２１）借方　工事コード';
comment on column shiwake_sias.rkos is '（オープン２１）借方　工種コード';
comment on column shiwake_sias.rprj is '（オープン２１）借方　プロジェクトコード';
comment on column shiwake_sias.rseg is '（オープン２１）借方　セグメントコード';
comment on column shiwake_sias.rdm1 is '（オープン２１）借方　ユニバーサルフィールド１';
comment on column shiwake_sias.rdm2 is '（オープン２１）借方　ユニバーサルフィールド２';
comment on column shiwake_sias.rdm3 is '（オープン２１）借方　ユニバーサルフィールド３';
comment on column shiwake_sias.rdm4 is '（オープン２１）借方　ユニバーサルフィールド４';
comment on column shiwake_sias.rdm5 is '（オープン２１）借方　ユニバーサルフィールド５';
comment on column shiwake_sias.rdm6 is '（オープン２１）借方　ユニバーサルフィールド６';
comment on column shiwake_sias.rdm7 is '（オープン２１）借方　ユニバーサルフィールド７';
comment on column shiwake_sias.rdm8 is '（オープン２１）借方　ユニバーサルフィールド８';
comment on column shiwake_sias.rdm9 is '（オープン２１）借方　ユニバーサルフィールド９';
comment on column shiwake_sias.rdm10 is '（オープン２１）借方　ユニバーサルフィールド１０';
comment on column shiwake_sias.rdm11 is '（オープン２１）借方　ユニバーサルフィールド１１';
comment on column shiwake_sias.rdm12 is '（オープン２１）借方　ユニバーサルフィールド１２';
comment on column shiwake_sias.rdm13 is '（オープン２１）借方　ユニバーサルフィールド１３';
comment on column shiwake_sias.rdm14 is '（オープン２１）借方　ユニバーサルフィールド１４';
comment on column shiwake_sias.rdm15 is '（オープン２１）借方　ユニバーサルフィールド１５';
comment on column shiwake_sias.rdm16 is '（オープン２１）借方　ユニバーサルフィールド１６';
comment on column shiwake_sias.rdm17 is '（オープン２１）借方　ユニバーサルフィールド１７';
comment on column shiwake_sias.rdm18 is '（オープン２１）借方　ユニバーサルフィールド１８';
comment on column shiwake_sias.rdm19 is '（オープン２１）借方　ユニバーサルフィールド１９';
comment on column shiwake_sias.rdm20 is '（オープン２１）借方　ユニバーサルフィールド２０';
comment on column shiwake_sias.rrit is '（オープン２１）借方　税率';
comment on column shiwake_sias.rkeigen is '（オープン２１）借方　軽減税率区分';
comment on column shiwake_sias.rzkb is '（オープン２１）借方　課税区分';
comment on column shiwake_sias.rgyo is '（オープン２１）借方　業種区分';
comment on column shiwake_sias.rsre is '（オープン２１）借方　仕入区分';
comment on column shiwake_sias.rtky is '（オープン２１）借方　摘要';
comment on column shiwake_sias.rtno is '（オープン２１）借方　摘要コード';
comment on column shiwake_sias.sbmn is '（オープン２１）貸方　部門コード';
comment on column shiwake_sias.stor is '（オープン２１）貸方　取引先コード';
comment on column shiwake_sias.skmk is '（オープン２１）貸方　科目コード';
comment on column shiwake_sias.seda is '（オープン２１）貸方　枝番コード';
comment on column shiwake_sias.skoj is '（オープン２１）貸方　工事コード';
comment on column shiwake_sias.skos is '（オープン２１）貸方　工種コード';
comment on column shiwake_sias.sprj is '（オープン２１）貸方　プロジェクトコード';
comment on column shiwake_sias.sseg is '（オープン２１）貸方　セグメントコード';
comment on column shiwake_sias.sdm1 is '（オープン２１）貸方　ユニバーサルフィールド１';
comment on column shiwake_sias.sdm2 is '（オープン２１）貸方　ユニバーサルフィールド２';
comment on column shiwake_sias.sdm3 is '（オープン２１）貸方　ユニバーサルフィールド３';
comment on column shiwake_sias.sdm4 is '（オープン２１）貸方　ユニバーサルフィールド４';
comment on column shiwake_sias.sdm5 is '（オープン２１）貸方　ユニバーサルフィールド５';
comment on column shiwake_sias.sdm6 is '（オープン２１）貸方　ユニバーサルフィールド６';
comment on column shiwake_sias.sdm7 is '（オープン２１）貸方　ユニバーサルフィールド７';
comment on column shiwake_sias.sdm8 is '（オープン２１）貸方　ユニバーサルフィールド８';
comment on column shiwake_sias.sdm9 is '（オープン２１）貸方　ユニバーサルフィールド９';
comment on column shiwake_sias.sdm10 is '（オープン２１）貸方　ユニバーサルフィールド１０';
comment on column shiwake_sias.sdm11 is '（オープン２１）貸方　ユニバーサルフィールド１１';
comment on column shiwake_sias.sdm12 is '（オープン２１）貸方　ユニバーサルフィールド１２';
comment on column shiwake_sias.sdm13 is '（オープン２１）貸方　ユニバーサルフィールド１３';
comment on column shiwake_sias.sdm14 is '（オープン２１）貸方　ユニバーサルフィールド１４';
comment on column shiwake_sias.sdm15 is '（オープン２１）貸方　ユニバーサルフィールド１５';
comment on column shiwake_sias.sdm16 is '（オープン２１）貸方　ユニバーサルフィールド１６';
comment on column shiwake_sias.sdm17 is '（オープン２１）貸方　ユニバーサルフィールド１７';
comment on column shiwake_sias.sdm18 is '（オープン２１）貸方　ユニバーサルフィールド１８';
comment on column shiwake_sias.sdm19 is '（オープン２１）貸方　ユニバーサルフィールド１９';
comment on column shiwake_sias.sdm20 is '（オープン２１）貸方　ユニバーサルフィールド２０';
comment on column shiwake_sias.srit is '（オープン２１）貸方　税率';
comment on column shiwake_sias.skeigen is '（オープン２１）貸方　軽減税率区分';
comment on column shiwake_sias.szkb is '（オープン２１）貸方　課税区分';
comment on column shiwake_sias.sgyo is '（オープン２１）貸方　業種区分';
comment on column shiwake_sias.ssre is '（オープン２１）貸方　仕入区分';
comment on column shiwake_sias.stky is '（オープン２１）貸方　摘要';
comment on column shiwake_sias.stno is '（オープン２１）貸方　摘要コード';
comment on column shiwake_sias.zkmk is '（オープン２１）消費税対象科目コード';
comment on column shiwake_sias.zrit is '（オープン２１）消費税対象科目税率';
comment on column shiwake_sias.zkeigen is '（オープン２１）消費税対象科目　軽減税率区分';
comment on column shiwake_sias.zzkb is '（オープン２１）消費税対象科目　課税区分';
comment on column shiwake_sias.zgyo is '（オープン２１）消費税対象科目　業種区分';
comment on column shiwake_sias.zsre is '（オープン２１）消費税対象科目　仕入区分';
comment on column shiwake_sias.exvl is '（オープン２１）対価金額';
comment on column shiwake_sias.valu is '（オープン２１）金額';
comment on column shiwake_sias.symd is '（オープン２１）支払日';
comment on column shiwake_sias.skbn is '（オープン２１）支払区分';
comment on column shiwake_sias.skiz is '（オープン２１）支払期日';
comment on column shiwake_sias.uymd is '（オープン２１）回収日';
comment on column shiwake_sias.ukbn is '（オープン２１）入金区分';
comment on column shiwake_sias.ukiz is '（オープン２１）回収期日';
comment on column shiwake_sias.dkec is '（オープン２１）消込コード';
comment on column shiwake_sias.fusr is '（オープン２１）入力者コード';
comment on column shiwake_sias.fsen is '（オープン２１）付箋番号';
comment on column shiwake_sias.tkflg is '（オープン２１）貸借別摘要フラグ';
comment on column shiwake_sias.bunri is '（オープン２１）分離区分';
comment on column shiwake_sias.heic is '（オープン２１）幣種';
comment on column shiwake_sias.rate is '（オープン２１）レート';
comment on column shiwake_sias.gexvl is '（オープン２１）外貨対価金額';
comment on column shiwake_sias.gvalu is '（オープン２１）外貨金額';
comment on column shiwake_sias.gsep is '（オープン２１）行区切り';
comment on column shiwake_sias.rurizeikeisan is '（オープン２１）借方　併用売上税額計算方式';
comment on column shiwake_sias.surizeikeisan is '（オープン２１）貸方　併用売上税額計算方式';
comment on column shiwake_sias.zurizeikeisan is '（オープン２１）税対象科目　併用売上税額計算方式';
comment on column shiwake_sias.rmenzeikeika is '（オープン２１）借方　仕入税額控除経過措置割合';
comment on column shiwake_sias.smenzeikeika is '（オープン２１）貸方　仕入税額控除経過措置割合';
comment on column shiwake_sias.zmenzeikeika is '（オープン２１）税対象科目　仕入税額控除経過措置割合';

comment on table shouhizeiritsu is '消費税率';
comment on column shouhizeiritsu.sort_jun is '並び順';
comment on column shouhizeiritsu.zeiritsu is '税率';
comment on column shouhizeiritsu.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column shouhizeiritsu.hasuu_keisan_kbn is '端数計算区分';
comment on column shouhizeiritsu.yuukou_kigen_from is '有効期限開始日';
comment on column shouhizeiritsu.yuukou_kigen_to is '有効期限終了日';

comment on table shounin_joukyou is '承認状況';
comment on column shounin_joukyou.denpyou_id is '伝票ID';
comment on column shounin_joukyou.edano is '枝番号';
comment on column shounin_joukyou.user_id is 'ユーザーID';
comment on column shounin_joukyou.user_full_name is 'ユーザーフル名';
comment on column shounin_joukyou.bumon_cd is '部門コード';
comment on column shounin_joukyou.bumon_full_name is '部門フル名';
comment on column shounin_joukyou.bumon_role_id is '部門ロールID';
comment on column shounin_joukyou.bumon_role_name is '部門ロール名';
comment on column shounin_joukyou.gyoumu_role_id is '業務ロールID';
comment on column shounin_joukyou.gyoumu_role_name is '業務ロール名';
comment on column shounin_joukyou.joukyou_cd is '状況コード';
comment on column shounin_joukyou.joukyou is '状況';
comment on column shounin_joukyou.comment is 'コメント';
comment on column shounin_joukyou.shounin_route_edano is '承認ルート枝番号';
comment on column shounin_joukyou.shounin_route_gougi_edano is '承認ルート合議枝番号';
comment on column shounin_joukyou.shounin_route_gougi_edaedano is '承認ルート合議枝々番号';
comment on column shounin_joukyou.shounin_shori_kengen_no is '承認処理権限番号';
comment on column shounin_joukyou.shounin_shori_kengen_name is '承認処理権限名';
comment on column shounin_joukyou.touroku_user_id is '登録ユーザーID';
comment on column shounin_joukyou.touroku_time is '登録日時';
comment on column shounin_joukyou.koushin_user_id is '更新ユーザーID';
comment on column shounin_joukyou.koushin_time is '更新日時';

comment on table shounin_route is '承認ルート';
comment on column shounin_route.denpyou_id is '伝票ID';
comment on column shounin_route.edano is '枝番号';
comment on column shounin_route.user_id is 'ユーザーID';
comment on column shounin_route.user_full_name is 'ユーザーフル名';
comment on column shounin_route.bumon_cd is '部門コード';
comment on column shounin_route.bumon_full_name is '部門フル名';
comment on column shounin_route.bumon_role_id is '部門ロールID';
comment on column shounin_route.bumon_role_name is '部門ロール名';
comment on column shounin_route.gyoumu_role_id is '業務ロールID';
comment on column shounin_route.gyoumu_role_name is '業務ロール名';
comment on column shounin_route.genzai_flg is '現在フラグ';
comment on column shounin_route.saishu_shounin_flg is '最終承認フラグ';
comment on column shounin_route.joukyou_edano is '承認状況枝番';
comment on column shounin_route.shounin_shori_kengen_no is '承認処理権限番号';
comment on column shounin_route.shounin_shori_kengen_name is '承認処理権限名';
comment on column shounin_route.kihon_model_cd is '基本モデルコード';
comment on column shounin_route.shounin_hissu_flg is '承認必須フラグ';
comment on column shounin_route.shounin_ken_flg is '承認権フラグ';
comment on column shounin_route.henkou_flg is '変更フラグ';
comment on column shounin_route.touroku_user_id is '登録ユーザーID';
comment on column shounin_route.touroku_time is '登録日時';
comment on column shounin_route.koushin_user_id is '更新ユーザーID';
comment on column shounin_route.koushin_time is '更新日時';

comment on table shounin_route_gougi_ko is '承認ルート合議子';
comment on column shounin_route_gougi_ko.denpyou_id is '伝票ID';
comment on column shounin_route_gougi_ko.edano is '枝番号';
comment on column shounin_route_gougi_ko.gougi_edano is '合議枝番号';
comment on column shounin_route_gougi_ko.gougi_edaedano is '合議枝々番号';
comment on column shounin_route_gougi_ko.user_id is 'ユーザーID';
comment on column shounin_route_gougi_ko.user_full_name is 'ユーザーフル名';
comment on column shounin_route_gougi_ko.bumon_cd is '部門コード';
comment on column shounin_route_gougi_ko.bumon_full_name is '部門フル名';
comment on column shounin_route_gougi_ko.bumon_role_id is '部門ロールID';
comment on column shounin_route_gougi_ko.bumon_role_name is '部門ロール名';
comment on column shounin_route_gougi_ko.gougi_genzai_flg is '合議現在フラグ';
comment on column shounin_route_gougi_ko.shounin_shori_kengen_no is '承認処理権限番号';
comment on column shounin_route_gougi_ko.shounin_shori_kengen_name is '承認処理権限名';
comment on column shounin_route_gougi_ko.kihon_model_cd is '基本モデルコード';
comment on column shounin_route_gougi_ko.shounin_hissu_flg is '承認必須フラグ';
comment on column shounin_route_gougi_ko.shounin_ken_flg is '承認権フラグ';
comment on column shounin_route_gougi_ko.henkou_flg is '変更フラグ';
comment on column shounin_route_gougi_ko.shounin_ninzuu_cd is '承認人数コード';
comment on column shounin_route_gougi_ko.shounin_ninzuu_hiritsu is '承認人数比率';
comment on column shounin_route_gougi_ko.syori_cd is '処理コード';
comment on column shounin_route_gougi_ko.gouginai_group is '合議内グループ';
comment on column shounin_route_gougi_ko.joukyou_edano is '承認状況枝番';

comment on table shounin_route_gougi_oya is '承認ルート合議親';
comment on column shounin_route_gougi_oya.denpyou_id is '伝票ID';
comment on column shounin_route_gougi_oya.edano is '枝番号';
comment on column shounin_route_gougi_oya.gougi_edano is '合議枝番号';
comment on column shounin_route_gougi_oya.gougi_pattern_no is '合議パターン番号';
comment on column shounin_route_gougi_oya.gougi_name is '合議名';
comment on column shounin_route_gougi_oya.syori_cd is '処理コード';

comment on table shounin_shori_kengen is '承認処理権限';
comment on column shounin_shori_kengen.shounin_shori_kengen_no is '承認処理権限番号';
comment on column shounin_shori_kengen.shounin_shori_kengen_name is '承認処理権限名';
comment on column shounin_shori_kengen.kihon_model_cd is '基本モデルコード';
comment on column shounin_shori_kengen.shounin_hissu_flg is '承認必須フラグ';
comment on column shounin_shori_kengen.shounin_ken_flg is '承認権フラグ';
comment on column shounin_shori_kengen.henkou_flg is '変更フラグ';
comment on column shounin_shori_kengen.setsumei is '説明';
comment on column shounin_shori_kengen.shounin_mongon is '承認文言';
comment on column shounin_shori_kengen.hanrei_hyouji_cd is '凡例表示コード';
comment on column shounin_shori_kengen.hyouji_jun is '表示順';
comment on column shounin_shori_kengen.touroku_user_id is '登録ユーザーID';
comment on column shounin_shori_kengen.touroku_time is '登録日時';
comment on column shounin_shori_kengen.koushin_user_id is '更新ユーザーID';
comment on column shounin_shori_kengen.koushin_time is '更新日時';

comment on table shozoku_bumon is '所属部門';
comment on column shozoku_bumon.bumon_cd is '部門コード';
comment on column shozoku_bumon.bumon_name is '部門名';
comment on column shozoku_bumon.oya_bumon_cd is '親所属部門コード';
comment on column shozoku_bumon.yuukou_kigen_from is '有効期限開始日';
comment on column shozoku_bumon.yuukou_kigen_to is '有効期限終了日';
comment on column shozoku_bumon.security_pattern is 'セキュリティパターン';
comment on column shozoku_bumon.touroku_user_id is '登録ユーザーID';
comment on column shozoku_bumon.touroku_time is '登録日時';
comment on column shozoku_bumon.koushin_user_id is '更新ユーザーID';
comment on column shozoku_bumon.koushin_time is '更新日時';

comment on table shozoku_bumon_shiwake_pattern_master is '所属部門仕訳パターンマスター';
comment on column shozoku_bumon_shiwake_pattern_master.bumon_cd is '部門コード';
comment on column shozoku_bumon_shiwake_pattern_master.denpyou_kbn is '伝票区分';
comment on column shozoku_bumon_shiwake_pattern_master.shiwake_edano is '仕訳枝番号';
comment on column shozoku_bumon_shiwake_pattern_master.touroku_user_id is '登録ユーザーID';
comment on column shozoku_bumon_shiwake_pattern_master.touroku_time is '登録日時';
comment on column shozoku_bumon_shiwake_pattern_master.koushin_user_id is '更新ユーザーID';
comment on column shozoku_bumon_shiwake_pattern_master.koushin_time is '更新日時';

comment on table shozoku_bumon_wariate is '所属部門割り当て';
comment on column shozoku_bumon_wariate.bumon_cd is '部門コード';
comment on column shozoku_bumon_wariate.bumon_role_id is '部門ロールID';
comment on column shozoku_bumon_wariate.user_id is 'ユーザーID';
comment on column shozoku_bumon_wariate.daihyou_futan_bumon_cd is '代表負担部門コード';
comment on column shozoku_bumon_wariate.yuukou_kigen_from is '有効期限開始日';
comment on column shozoku_bumon_wariate.yuukou_kigen_to is '有効期限終了日';
comment on column shozoku_bumon_wariate.hyouji_jun is '表示順';
comment on column shozoku_bumon_wariate.touroku_user_id is '登録ユーザーID';
comment on column shozoku_bumon_wariate.touroku_time is '登録日時';
comment on column shozoku_bumon_wariate.koushin_user_id is '更新ユーザーID';
comment on column shozoku_bumon_wariate.koushin_time is '更新日時';

comment on table shukujitsu_master is '祝日マスター';
comment on column shukujitsu_master.shukujitsu is '祝日';
comment on column shukujitsu_master.shukujitsu_name is '祝日名';

comment on table syuukei_bumon is '集計部門マスター';
comment on column syuukei_bumon.syuukei_bumon_cd is '集計部門コード';
comment on column syuukei_bumon.syuukei_bumon_name is '集計部門名';
comment on column syuukei_bumon.kessanki_bangou is '決算期番号';
comment on column syuukei_bumon.shaukei_bumon_flg is '集計部門フラグ';

comment on table teiki_jouhou is '定期券情報';
comment on column teiki_jouhou.user_id is 'ユーザーID';
comment on column teiki_jouhou.shiyou_kaishibi is '使用開始日';
comment on column teiki_jouhou.shiyou_shuuryoubi is '使用終了日';
comment on column teiki_jouhou.intra_teiki_kukan is 'イントラ版定期区間';
comment on column teiki_jouhou.intra_restoreroute is 'イントラ版方向性付き定期経路文字列';
comment on column teiki_jouhou.web_teiki_kukan is '定期区間情報';
comment on column teiki_jouhou.web_teiki_serialize_data is '定期区間シリアライズデータ';
comment on column teiki_jouhou.touroku_user_id is '登録ユーザーID';
comment on column teiki_jouhou.touroku_time is '登録日時';
comment on column teiki_jouhou.koushin_user_id is '更新ユーザーID';
comment on column teiki_jouhou.koushin_time is '更新日時';

comment on table temp_csvmake is 'CSV作成用一時テーブル';
comment on column temp_csvmake.row_num is '行番号';
comment on column temp_csvmake.data_rowbinary is '行バイナリデータ';

comment on table tenpu_denpyou_jyogai is '添付伝票除外';
comment on column tenpu_denpyou_jyogai.denpyou_id is '伝票ID';

comment on table tenpu_file is '添付ファイル';
comment on column tenpu_file.denpyou_id is '伝票ID';
comment on column tenpu_file.edano is '枝番号';
comment on column tenpu_file.file_name is 'ファイル名';
comment on column tenpu_file.file_size is 'ファイルサイズ';
comment on column tenpu_file.content_type is 'コンテンツタイプ';
comment on column tenpu_file.binary_data is 'バイナリーデータ';
comment on column tenpu_file.touroku_user_id is '登録ユーザーID';
comment on column tenpu_file.touroku_time is '登録日時';
comment on column tenpu_file.koushin_user_id is '更新ユーザーID';
comment on column tenpu_file.koushin_time is '更新日時';

comment on table torihikisaki is '取引先';
comment on column torihikisaki.torihikisaki_cd is '取引先コード';
comment on column torihikisaki.yuubin_bangou is '郵便番号';
comment on column torihikisaki.juusho1 is '住所１';
comment on column torihikisaki.juusho2 is '住所２';
comment on column torihikisaki.telno is '電話番号';
comment on column torihikisaki.faxno is 'FAX番号';
comment on column torihikisaki.kouza_meiginin is '口座名義人';
comment on column torihikisaki.kouza_meiginin_furigana is '口座名義人ふり仮名';
comment on column torihikisaki.shiharai_shubetsu is '支払種別';
comment on column torihikisaki.yokin_shubetsu is '預金種別';
comment on column torihikisaki.kouza_bangou is '口座番号';
comment on column torihikisaki.tesuuryou_futan_kbn is '手数料負担区分';
comment on column torihikisaki.furikomi_kbn is '振込区分';
comment on column torihikisaki.furikomi_ginkou_cd is '振込銀行コード';
comment on column torihikisaki.furikomi_ginkou_shiten_cd is '振込銀行支店コード';
comment on column torihikisaki.shiharaibi is '支払日';
comment on column torihikisaki.shiharai_kijitsu is '支払期日';
comment on column torihikisaki.yakujou_kingaku is '約定金額';
comment on column torihikisaki.torihikisaki_name_hankana is '取引先名（半角カナ）';
comment on column torihikisaki.sbusyo is '相手先部署';
comment on column torihikisaki.stanto is '相手先担当者';
comment on column torihikisaki.keicd is '敬称';
comment on column torihikisaki.nayose is '名寄せフラグ';
comment on column torihikisaki.f_setuin is '節印実行フラグ';
comment on column torihikisaki.stan is '主担当者';
comment on column torihikisaki.sbcod is '部門コード';
comment on column torihikisaki.skicd is '科目コード';
comment on column torihikisaki.f_soufu is '送付案内';
comment on column torihikisaki.annai is '案内文';
comment on column torihikisaki.tsokbn is '送料負担区分';
comment on column torihikisaki.f_shitu is '支払通知';
comment on column torihikisaki.cdm2 is '仕入先番号';
comment on column torihikisaki.dm1 is '補助コード１';
comment on column torihikisaki.dm2 is '補助コード２';
comment on column torihikisaki.dm3 is '補助コード３';
comment on column torihikisaki.gendo is '負担限度額';

comment on table torihikisaki_furikomisaki is '取引先振込先';
comment on column torihikisaki_furikomisaki.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_furikomisaki.ginkou_id is '銀行ID';
comment on column torihikisaki_furikomisaki.kinyuukikan_cd is '金融機関コード';
comment on column torihikisaki_furikomisaki.kinyuukikan_shiten_cd is '金融機関支店コード';
comment on column torihikisaki_furikomisaki.yokin_shubetsu is '預金種別';
comment on column torihikisaki_furikomisaki.kouza_bangou is '口座番号';
comment on column torihikisaki_furikomisaki.kouza_meiginin is '口座名義人';

comment on table torihikisaki_hojo is '取引先補助';
comment on column torihikisaki_hojo.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_hojo.dm1 is '補助コード1';
comment on column torihikisaki_hojo.dm2 is '補助コード2';
comment on column torihikisaki_hojo.dm3 is '補助コード3';
comment on column torihikisaki_hojo.stflg is '取引停止';

comment on table torihikisaki_kamoku_zandaka is '取引先科目残高';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_kamoku_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column torihikisaki_kamoku_zandaka.kessanki_bangou is '決算期番号';
comment on column torihikisaki_kamoku_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column torihikisaki_kamoku_zandaka.torihikisaki_name_seishiki is '取引先名（正式）';
comment on column torihikisaki_kamoku_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column torihikisaki_kamoku_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column torihikisaki_kamoku_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column torihikisaki_kamoku_zandaka.taishaku_zokusei is '貸借属性';
comment on column torihikisaki_kamoku_zandaka.kishu_zandaka is '期首残高';

comment on table torihikisaki_master is '取引先マスター';
comment on column torihikisaki_master.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_master.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column torihikisaki_master.torihikisaki_name_seishiki is '取引先名（正式）';
comment on column torihikisaki_master.torihikisaki_name_hankana is '取引先名（半角カナ）';
comment on column torihikisaki_master.nyuryoku_from_date is '入力許可開始年月日';
comment on column torihikisaki_master.nyuryoku_to_date is '入力許可終了年月日';
comment on column torihikisaki_master.tekikaku_no is '適格請求書発行事業者登録番号';
comment on column torihikisaki_master.menzei_jigyousha_flg is '免税事業者等フラグ';

comment on table torihikisaki_shiharaihouhou is '取引先支払方法';
comment on column torihikisaki_shiharaihouhou.torihikisaki_cd is '取引先コード';
comment on column torihikisaki_shiharaihouhou.shiharai_id is '支払ID';
comment on column torihikisaki_shiharaihouhou.shimebi is '締日';
comment on column torihikisaki_shiharaihouhou.shiharaibi_mm is '支払日（MM）';
comment on column torihikisaki_shiharaihouhou.shiharaibi_dd is '支払日（DD）';
comment on column torihikisaki_shiharaihouhou.shiharai_kbn is '支払区分';
comment on column torihikisaki_shiharaihouhou.shiharaikijitsu_mm is '支払期日（MM）';
comment on column torihikisaki_shiharaihouhou.shiharaikijitsu_dd is '支払期日（DD）';
comment on column torihikisaki_shiharaihouhou.harai_h is '休日補正(支払日)';
comment on column torihikisaki_shiharaihouhou.kijitu_h is '休日補正(支払期日)';
comment on column torihikisaki_shiharaihouhou.shiharai_houhou is '支払方法';

comment on table tsukekae is '付替';
comment on column tsukekae.denpyou_id is '伝票ID';
comment on column tsukekae.denpyou_date is '伝票日付';
comment on column tsukekae.shouhyou_shorui_flg is '証憑書類フラグ';
comment on column tsukekae.zeiritsu is '税率';
comment on column tsukekae.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column tsukekae.kingaku_goukei is '金額合計';
comment on column tsukekae.hf1_cd is 'HF1コード';
comment on column tsukekae.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column tsukekae.hf2_cd is 'HF2コード';
comment on column tsukekae.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column tsukekae.hf3_cd is 'HF3コード';
comment on column tsukekae.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column tsukekae.hf4_cd is 'HF4コード';
comment on column tsukekae.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column tsukekae.hf5_cd is 'HF5コード';
comment on column tsukekae.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column tsukekae.hf6_cd is 'HF6コード';
comment on column tsukekae.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column tsukekae.hf7_cd is 'HF7コード';
comment on column tsukekae.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column tsukekae.hf8_cd is 'HF8コード';
comment on column tsukekae.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column tsukekae.hf9_cd is 'HF9コード';
comment on column tsukekae.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column tsukekae.hf10_cd is 'HF10コード';
comment on column tsukekae.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column tsukekae.hosoku is '補足';
comment on column tsukekae.tsukekae_kbn is '付替区分';
comment on column tsukekae.moto_kamoku_cd is '付替元科目コード';
comment on column tsukekae.moto_kamoku_name is '付替元科目名';
comment on column tsukekae.moto_kamoku_edaban_cd is '付替元科目枝番コード';
comment on column tsukekae.moto_kamoku_edaban_name is '付替元科目枝番名';
comment on column tsukekae.moto_futan_bumon_cd is '付替元負担部門コード';
comment on column tsukekae.moto_futan_bumon_name is '付替元負担部門名';
comment on column tsukekae.moto_torihikisaki_cd is '付替元取引先コード';
comment on column tsukekae.moto_torihikisaki_name_ryakushiki is '付替元取引先名（略式）';
comment on column tsukekae.moto_kazei_kbn is '付替元課税区分';
comment on column tsukekae.moto_uf1_cd is '付替元UF1コード';
comment on column tsukekae.moto_uf1_name_ryakushiki is '付替元UF1名（略式）';
comment on column tsukekae.moto_uf2_cd is '付替元UF2コード';
comment on column tsukekae.moto_uf2_name_ryakushiki is '付替元UF2名（略式）';
comment on column tsukekae.moto_uf3_cd is '付替元UF3コード';
comment on column tsukekae.moto_uf3_name_ryakushiki is '付替元UF3名（略式）';
comment on column tsukekae.moto_uf4_cd is '付替元UF4コード';
comment on column tsukekae.moto_uf4_name_ryakushiki is '付替元UF4名（略式）';
comment on column tsukekae.moto_uf5_cd is '付替元UF5コード';
comment on column tsukekae.moto_uf5_name_ryakushiki is '付替元UF5名（略式）';
comment on column tsukekae.moto_uf6_cd is '付替元UF6コード';
comment on column tsukekae.moto_uf6_name_ryakushiki is '付替元UF6名（略式）';
comment on column tsukekae.moto_uf7_cd is '付替元UF7コード';
comment on column tsukekae.moto_uf7_name_ryakushiki is '付替元UF7名（略式）';
comment on column tsukekae.moto_uf8_cd is '付替元UF8コード';
comment on column tsukekae.moto_uf8_name_ryakushiki is '付替元UF8名（略式）';
comment on column tsukekae.moto_uf9_cd is '付替元UF9コード';
comment on column tsukekae.moto_uf9_name_ryakushiki is '付替元UF9名（略式）';
comment on column tsukekae.moto_uf10_cd is '付替元UF10コード';
comment on column tsukekae.moto_uf10_name_ryakushiki is '付替元UF10名（略式）';
comment on column tsukekae.moto_project_cd is '付替元プロジェクトコード';
comment on column tsukekae.moto_project_name is '付替元プロジェクト名';
comment on column tsukekae.moto_segment_cd is '付替元セグメントコード';
comment on column tsukekae.moto_segment_name_ryakushiki is '付替元セグメント名（略式）';
comment on column tsukekae.touroku_user_id is '登録ユーザーID';
comment on column tsukekae.touroku_time is '登録日時';
comment on column tsukekae.koushin_user_id is '更新ユーザーID';
comment on column tsukekae.koushin_time is '更新日時';
comment on column tsukekae.moto_jigyousha_kbn is '付替元事業者区分';
comment on column tsukekae.moto_bunri_kbn is '付替元分離区分';
comment on column tsukekae.moto_shiire_kbn is '付替元仕入区分';
comment on column tsukekae.moto_zeigaku_houshiki is '付替元税額計算方式';
comment on column tsukekae.invoice_denpyou is 'インボイス対応伝票';

comment on table tsukekae_meisai is '付替明細';
comment on column tsukekae_meisai.denpyou_id is '伝票ID';
comment on column tsukekae_meisai.denpyou_edano is '伝票枝番号';
comment on column tsukekae_meisai.tekiyou is '摘要';
comment on column tsukekae_meisai.kingaku is '金額';
comment on column tsukekae_meisai.hontai_kingaku is '本体金額';
comment on column tsukekae_meisai.shouhizeigaku is '消費税額';
comment on column tsukekae_meisai.bikou is '備考（会計伝票）';
comment on column tsukekae_meisai.saki_kamoku_cd is '付替先科目コード';
comment on column tsukekae_meisai.saki_kamoku_name is '付替先科目名';
comment on column tsukekae_meisai.saki_kamoku_edaban_cd is '付替先科目枝番コード';
comment on column tsukekae_meisai.saki_kamoku_edaban_name is '付替先科目枝番名';
comment on column tsukekae_meisai.saki_futan_bumon_cd is '付替先負担部門コード';
comment on column tsukekae_meisai.saki_futan_bumon_name is '付替先負担部門名';
comment on column tsukekae_meisai.saki_torihikisaki_cd is '付替先取引先コード';
comment on column tsukekae_meisai.saki_torihikisaki_name_ryakushiki is '付替先取引先名（略式）';
comment on column tsukekae_meisai.saki_kazei_kbn is '付替先課税区分';
comment on column tsukekae_meisai.saki_uf1_cd is '付替先UF1コード';
comment on column tsukekae_meisai.saki_uf1_name_ryakushiki is '付替先UF1名（略式）';
comment on column tsukekae_meisai.saki_uf2_cd is '付替先UF2コード';
comment on column tsukekae_meisai.saki_uf2_name_ryakushiki is '付替先UF2名（略式）';
comment on column tsukekae_meisai.saki_uf3_cd is '付替先UF3コード';
comment on column tsukekae_meisai.saki_uf3_name_ryakushiki is '付替先UF3名（略式）';
comment on column tsukekae_meisai.saki_uf4_cd is '付替先UF4コード';
comment on column tsukekae_meisai.saki_uf4_name_ryakushiki is '付替先UF4名（略式）';
comment on column tsukekae_meisai.saki_uf5_cd is '付替先UF5コード';
comment on column tsukekae_meisai.saki_uf5_name_ryakushiki is '付替先UF5名（略式）';
comment on column tsukekae_meisai.saki_uf6_cd is '付替先UF6コード';
comment on column tsukekae_meisai.saki_uf6_name_ryakushiki is '付替先UF6名（略式）';
comment on column tsukekae_meisai.saki_uf7_cd is '付替先UF7コード';
comment on column tsukekae_meisai.saki_uf7_name_ryakushiki is '付替先UF7名（略式）';
comment on column tsukekae_meisai.saki_uf8_cd is '付替先UF8コード';
comment on column tsukekae_meisai.saki_uf8_name_ryakushiki is '付替先UF8名（略式）';
comment on column tsukekae_meisai.saki_uf9_cd is '付替先UF9コード';
comment on column tsukekae_meisai.saki_uf9_name_ryakushiki is '付替先UF9名（略式）';
comment on column tsukekae_meisai.saki_uf10_cd is '付替先UF10コード';
comment on column tsukekae_meisai.saki_uf10_name_ryakushiki is '付替先UF10名（略式）';
comment on column tsukekae_meisai.saki_project_cd is '付替先プロジェクトコード';
comment on column tsukekae_meisai.saki_project_name is '付替先プロジェクト名';
comment on column tsukekae_meisai.saki_segment_cd is '付替先セグメントコード';
comment on column tsukekae_meisai.saki_segment_name_ryakushiki is '付替先セグメント名（略式）';
comment on column tsukekae_meisai.touroku_user_id is '登録ユーザーID';
comment on column tsukekae_meisai.touroku_time is '登録日時';
comment on column tsukekae_meisai.koushin_user_id is '更新ユーザーID';
comment on column tsukekae_meisai.koushin_time is '更新日時';
comment on column tsukekae_meisai.saki_jigyousha_kbn is '付替先事業者区分';
comment on column tsukekae_meisai.saki_bunri_kbn is '付替先分離区分';
comment on column tsukekae_meisai.saki_shiire_kbn is '付替先仕入区分';
comment on column tsukekae_meisai.saki_zeigaku_houshiki is '付替先税額計算方式';

comment on table tsuuchi is '通知';
comment on column tsuuchi.serial_no is 'シリアル番号';
comment on column tsuuchi.user_id is 'ユーザーID';
comment on column tsuuchi.denpyou_id is '伝票ID';
comment on column tsuuchi.edano is '枝番号';
comment on column tsuuchi.kidoku_flg is '既読フラグ';
comment on column tsuuchi.touroku_user_id is '登録ユーザーID';
comment on column tsuuchi.touroku_time is '登録日時';
comment on column tsuuchi.koushin_user_id is '更新ユーザーID';
comment on column tsuuchi.koushin_time is '更新日時';

comment on table tsuukinteiki is '通勤定期';
comment on column tsuukinteiki.denpyou_id is '伝票ID';
comment on column tsuukinteiki.shiyou_kikan_kbn is '使用期間区分';
comment on column tsuukinteiki.shiyou_kaishibi is '使用開始日';
comment on column tsuukinteiki.shiyou_shuuryoubi is '使用終了日';
comment on column tsuukinteiki.jyousha_kukan is '乗車区間';
comment on column tsuukinteiki.teiki_serialize_data is '定期区間シリアライズデータ';
comment on column tsuukinteiki.shiharaibi is '支払日';
comment on column tsuukinteiki.tekiyou is '摘要';
comment on column tsuukinteiki.zeiritsu is '税率';
comment on column tsuukinteiki.keigen_zeiritsu_kbn is '軽減税率区分';
comment on column tsuukinteiki.kingaku is '金額';
comment on column tsuukinteiki.tenyuuryoku_flg is '手入力フラグ';
comment on column tsuukinteiki.hf1_cd is 'HF1コード';
comment on column tsuukinteiki.hf1_name_ryakushiki is 'HF1名（略式）';
comment on column tsuukinteiki.hf2_cd is 'HF2コード';
comment on column tsuukinteiki.hf2_name_ryakushiki is 'HF2名（略式）';
comment on column tsuukinteiki.hf3_cd is 'HF3コード';
comment on column tsuukinteiki.hf3_name_ryakushiki is 'HF3名（略式）';
comment on column tsuukinteiki.hf4_cd is 'HF4コード';
comment on column tsuukinteiki.hf4_name_ryakushiki is 'HF4名（略式）';
comment on column tsuukinteiki.hf5_cd is 'HF5コード';
comment on column tsuukinteiki.hf5_name_ryakushiki is 'HF5名（略式）';
comment on column tsuukinteiki.hf6_cd is 'HF6コード';
comment on column tsuukinteiki.hf6_name_ryakushiki is 'HF6名（略式）';
comment on column tsuukinteiki.hf7_cd is 'HF7コード';
comment on column tsuukinteiki.hf7_name_ryakushiki is 'HF7名（略式）';
comment on column tsuukinteiki.hf8_cd is 'HF8コード';
comment on column tsuukinteiki.hf8_name_ryakushiki is 'HF8名（略式）';
comment on column tsuukinteiki.hf9_cd is 'HF9コード';
comment on column tsuukinteiki.hf9_name_ryakushiki is 'HF9名（略式）';
comment on column tsuukinteiki.hf10_cd is 'HF10コード';
comment on column tsuukinteiki.hf10_name_ryakushiki is 'HF10名（略式）';
comment on column tsuukinteiki.shiwake_edano is '仕訳枝番号';
comment on column tsuukinteiki.torihiki_name is '取引名';
comment on column tsuukinteiki.kari_futan_bumon_cd is '借方負担部門コード';
comment on column tsuukinteiki.kari_futan_bumon_name is '借方負担部門名';
comment on column tsuukinteiki.torihikisaki_cd is '取引先コード';
comment on column tsuukinteiki.torihikisaki_name_ryakushiki is '取引先名（略式）';
comment on column tsuukinteiki.kari_kamoku_cd is '借方科目コード';
comment on column tsuukinteiki.kari_kamoku_name is '借方科目名';
comment on column tsuukinteiki.kari_kamoku_edaban_cd is '借方科目枝番コード';
comment on column tsuukinteiki.kari_kamoku_edaban_name is '借方科目枝番名';
comment on column tsuukinteiki.kari_kazei_kbn is '借方課税区分';
comment on column tsuukinteiki.kashi_futan_bumon_cd is '貸方負担部門コード';
comment on column tsuukinteiki.kashi_futan_bumon_name is '貸方負担部門名';
comment on column tsuukinteiki.kashi_kamoku_cd is '貸方科目コード';
comment on column tsuukinteiki.kashi_kamoku_name is '貸方科目名';
comment on column tsuukinteiki.kashi_kamoku_edaban_cd is '貸方科目枝番コード';
comment on column tsuukinteiki.kashi_kamoku_edaban_name is '貸方科目枝番名';
comment on column tsuukinteiki.kashi_kazei_kbn is '貸方課税区分';
comment on column tsuukinteiki.uf1_cd is 'UF1コード';
comment on column tsuukinteiki.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column tsuukinteiki.uf2_cd is 'UF2コード';
comment on column tsuukinteiki.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column tsuukinteiki.uf3_cd is 'UF3コード';
comment on column tsuukinteiki.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column tsuukinteiki.uf4_cd is 'UF4コード';
comment on column tsuukinteiki.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column tsuukinteiki.uf5_cd is 'UF5コード';
comment on column tsuukinteiki.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column tsuukinteiki.uf6_cd is 'UF6コード';
comment on column tsuukinteiki.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column tsuukinteiki.uf7_cd is 'UF7コード';
comment on column tsuukinteiki.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column tsuukinteiki.uf8_cd is 'UF8コード';
comment on column tsuukinteiki.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column tsuukinteiki.uf9_cd is 'UF9コード';
comment on column tsuukinteiki.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column tsuukinteiki.uf10_cd is 'UF10コード';
comment on column tsuukinteiki.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column tsuukinteiki.project_cd is 'プロジェクトコード';
comment on column tsuukinteiki.project_name is 'プロジェクト名';
comment on column tsuukinteiki.segment_cd is 'セグメントコード';
comment on column tsuukinteiki.segment_name_ryakushiki is 'セグメント名（略式）';
comment on column tsuukinteiki.tekiyou_cd is '摘要コード';
comment on column tsuukinteiki.touroku_user_id is '登録ユーザーID';
comment on column tsuukinteiki.touroku_time is '登録日時';
comment on column tsuukinteiki.koushin_user_id is '更新ユーザーID';
comment on column tsuukinteiki.koushin_time is '更新日時';
comment on column tsuukinteiki.zeinuki_kingaku is '税抜金額';
comment on column tsuukinteiki.shouhizeigaku is '消費税額';
comment on column tsuukinteiki.shiharaisaki_name is '支払先名';
comment on column tsuukinteiki.jigyousha_kbn is '事業者区分';
comment on column tsuukinteiki.bunri_kbn is '分離区分';
comment on column tsuukinteiki.kari_shiire_kbn is '借方仕入区分';
comment on column tsuukinteiki.kashi_shiire_kbn is '貸方仕入区分';
comment on column tsuukinteiki.invoice_denpyou is 'インボイス対応伝票';

comment on table uf_kotei1_ichiran is 'ユニバーサルフィールド１一覧（固定値）';
comment on column uf_kotei1_ichiran.uf_kotei1_cd is 'UF1コード';
comment on column uf_kotei1_ichiran.uf_kotei1_name_ryakushiki is 'UF1名（略式）';
comment on column uf_kotei1_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei1_zandaka is 'ユニバーサルフィールド１残高（固定値）';
comment on column uf_kotei1_zandaka.uf_kotei1_cd is 'UF1コード';
comment on column uf_kotei1_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei1_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei1_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei1_zandaka.uf_kotei1_name_ryakushiki is 'UF1名（略式）';
comment on column uf_kotei1_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei1_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei1_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei1_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei1_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei10_ichiran is 'ユニバーサルフィールド１０一覧（固定値）';
comment on column uf_kotei10_ichiran.uf_kotei10_cd is 'UF10コード';
comment on column uf_kotei10_ichiran.uf_kotei10_name_ryakushiki is 'UF10名（略式）';
comment on column uf_kotei10_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei10_zandaka is 'ユニバーサルフィールド１０残高（固定値）';
comment on column uf_kotei10_zandaka.uf_kotei10_cd is 'UF10コード';
comment on column uf_kotei10_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei10_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei10_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei10_zandaka.uf_kotei10_name_ryakushiki is 'UF10名（略式）';
comment on column uf_kotei10_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei10_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei10_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei10_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei10_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei2_ichiran is 'ユニバーサルフィールド２一覧（固定値）';
comment on column uf_kotei2_ichiran.uf_kotei2_cd is 'UF2コード';
comment on column uf_kotei2_ichiran.uf_kotei2_name_ryakushiki is 'UF2名（略式）';
comment on column uf_kotei2_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei2_zandaka is 'ユニバーサルフィールド２残高（固定値）';
comment on column uf_kotei2_zandaka.uf_kotei2_cd is 'UF2コード';
comment on column uf_kotei2_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei2_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei2_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei2_zandaka.uf_kotei2_name_ryakushiki is 'UF2名（略式）';
comment on column uf_kotei2_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei2_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei2_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei2_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei2_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei3_ichiran is 'ユニバーサルフィールド３一覧（固定値）';
comment on column uf_kotei3_ichiran.uf_kotei3_cd is 'UF3コード';
comment on column uf_kotei3_ichiran.uf_kotei3_name_ryakushiki is 'UF3名（略式）';
comment on column uf_kotei3_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei3_zandaka is 'ユニバーサルフィールド３残高（固定値）';
comment on column uf_kotei3_zandaka.uf_kotei3_cd is 'UF3コード';
comment on column uf_kotei3_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei3_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei3_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei3_zandaka.uf_kotei3_name_ryakushiki is 'UF3名（略式）';
comment on column uf_kotei3_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei3_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei3_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei3_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei3_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei4_ichiran is 'ユニバーサルフィールド４一覧（固定値）';
comment on column uf_kotei4_ichiran.uf_kotei4_cd is 'UF4コード';
comment on column uf_kotei4_ichiran.uf_kotei4_name_ryakushiki is 'UF4名（略式）';
comment on column uf_kotei4_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei4_zandaka is 'ユニバーサルフィールド４残高（固定値）';
comment on column uf_kotei4_zandaka.uf_kotei4_cd is 'UF4コード';
comment on column uf_kotei4_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei4_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei4_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei4_zandaka.uf_kotei4_name_ryakushiki is 'UF4名（略式）';
comment on column uf_kotei4_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei4_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei4_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei4_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei4_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei5_ichiran is 'ユニバーサルフィールド５一覧（固定値）';
comment on column uf_kotei5_ichiran.uf_kotei5_cd is 'UF5コード';
comment on column uf_kotei5_ichiran.uf_kotei5_name_ryakushiki is 'UF5名（略式）';
comment on column uf_kotei5_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei5_zandaka is 'ユニバーサルフィールド５残高（固定値）';
comment on column uf_kotei5_zandaka.uf_kotei5_cd is 'UF5コード';
comment on column uf_kotei5_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei5_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei5_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei5_zandaka.uf_kotei5_name_ryakushiki is 'UF5名（略式）';
comment on column uf_kotei5_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei5_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei5_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei5_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei5_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei6_ichiran is 'ユニバーサルフィールド６一覧（固定値）';
comment on column uf_kotei6_ichiran.uf_kotei6_cd is 'UF6コード';
comment on column uf_kotei6_ichiran.uf_kotei6_name_ryakushiki is 'UF6名（略式）';
comment on column uf_kotei6_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei6_zandaka is 'ユニバーサルフィールド６残高（固定値）';
comment on column uf_kotei6_zandaka.uf_kotei6_cd is 'UF6コード';
comment on column uf_kotei6_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei6_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei6_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei6_zandaka.uf_kotei6_name_ryakushiki is 'UF6名（略式）';
comment on column uf_kotei6_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei6_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei6_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei6_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei6_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei7_ichiran is 'ユニバーサルフィールド７一覧（固定値）';
comment on column uf_kotei7_ichiran.uf_kotei7_cd is 'UF7コード';
comment on column uf_kotei7_ichiran.uf_kotei7_name_ryakushiki is 'UF7名（略式）';
comment on column uf_kotei7_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei7_zandaka is 'ユニバーサルフィールド７残高（固定値）';
comment on column uf_kotei7_zandaka.uf_kotei7_cd is 'UF7コード';
comment on column uf_kotei7_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei7_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei7_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei7_zandaka.uf_kotei7_name_ryakushiki is 'UF7名（略式）';
comment on column uf_kotei7_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei7_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei7_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei7_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei7_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei8_ichiran is 'ユニバーサルフィールド８一覧（固定値）';
comment on column uf_kotei8_ichiran.uf_kotei8_cd is 'UF8コード';
comment on column uf_kotei8_ichiran.uf_kotei8_name_ryakushiki is 'UF8名（略式）';
comment on column uf_kotei8_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei8_zandaka is 'ユニバーサルフィールド８残高（固定値）';
comment on column uf_kotei8_zandaka.uf_kotei8_cd is 'UF8コード';
comment on column uf_kotei8_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei8_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei8_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei8_zandaka.uf_kotei8_name_ryakushiki is 'UF8名（略式）';
comment on column uf_kotei8_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei8_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei8_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei8_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei8_zandaka.kishu_zandaka is '期首残高';

comment on table uf_kotei9_ichiran is 'ユニバーサルフィールド９一覧（固定値）';
comment on column uf_kotei9_ichiran.uf_kotei9_cd is 'UF9コード';
comment on column uf_kotei9_ichiran.uf_kotei9_name_ryakushiki is 'UF9名（略式）';
comment on column uf_kotei9_ichiran.kessanki_bangou is '決算期番号';

comment on table uf_kotei9_zandaka is 'ユニバーサルフィールド９残高（固定値）';
comment on column uf_kotei9_zandaka.uf_kotei9_cd is 'UF9コード';
comment on column uf_kotei9_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf_kotei9_zandaka.kessanki_bangou is '決算期番号';
comment on column uf_kotei9_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf_kotei9_zandaka.uf_kotei9_name_ryakushiki is 'UF9名（略式）';
comment on column uf_kotei9_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf_kotei9_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf_kotei9_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf_kotei9_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf_kotei9_zandaka.kishu_zandaka is '期首残高';

comment on table uf1_ichiran is 'ユニバーサルフィールド１一覧';
comment on column uf1_ichiran.uf1_cd is 'UF1コード';
comment on column uf1_ichiran.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column uf1_ichiran.kessanki_bangou is '決算期番号';

comment on table uf1_zandaka is 'ユニバーサルフィールド１残高';
comment on column uf1_zandaka.uf1_cd is 'UF1コード';
comment on column uf1_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf1_zandaka.kessanki_bangou is '決算期番号';
comment on column uf1_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf1_zandaka.uf1_name_ryakushiki is 'UF1名（略式）';
comment on column uf1_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf1_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf1_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf1_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf1_zandaka.kishu_zandaka is '期首残高';

comment on table uf10_ichiran is 'ユニバーサルフィールド１０一覧';
comment on column uf10_ichiran.uf10_cd is 'UF10コード';
comment on column uf10_ichiran.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column uf10_ichiran.kessanki_bangou is '決算期番号';

comment on table uf10_zandaka is 'ユニバーサルフィールド１０残高';
comment on column uf10_zandaka.uf10_cd is 'UF10コード';
comment on column uf10_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf10_zandaka.kessanki_bangou is '決算期番号';
comment on column uf10_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf10_zandaka.uf10_name_ryakushiki is 'UF10名（略式）';
comment on column uf10_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf10_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf10_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf10_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf10_zandaka.kishu_zandaka is '期首残高';

comment on table uf2_ichiran is 'ユニバーサルフィールド２一覧';
comment on column uf2_ichiran.uf2_cd is 'UF2コード';
comment on column uf2_ichiran.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column uf2_ichiran.kessanki_bangou is '決算期番号';

comment on table uf2_zandaka is 'ユニバーサルフィールド２残高';
comment on column uf2_zandaka.uf2_cd is 'UF2コード';
comment on column uf2_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf2_zandaka.kessanki_bangou is '決算期番号';
comment on column uf2_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf2_zandaka.uf2_name_ryakushiki is 'UF2名（略式）';
comment on column uf2_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf2_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf2_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf2_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf2_zandaka.kishu_zandaka is '期首残高';

comment on table uf3_ichiran is 'ユニバーサルフィールド３一覧';
comment on column uf3_ichiran.uf3_cd is 'UF3コード';
comment on column uf3_ichiran.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column uf3_ichiran.kessanki_bangou is '決算期番号';

comment on table uf3_zandaka is 'ユニバーサルフィールド３残高';
comment on column uf3_zandaka.uf3_cd is 'UF3コード';
comment on column uf3_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf3_zandaka.kessanki_bangou is '決算期番号';
comment on column uf3_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf3_zandaka.uf3_name_ryakushiki is 'UF3名（略式）';
comment on column uf3_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf3_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf3_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf3_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf3_zandaka.kishu_zandaka is '期首残高';

comment on table uf4_ichiran is 'ユニバーサルフィールド４一覧';
comment on column uf4_ichiran.uf4_cd is 'UF4コード';
comment on column uf4_ichiran.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column uf4_ichiran.kessanki_bangou is '決算期番号';

comment on table uf4_zandaka is 'ユニバーサルフィールド４残高';
comment on column uf4_zandaka.uf4_cd is 'UF4コード';
comment on column uf4_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf4_zandaka.kessanki_bangou is '決算期番号';
comment on column uf4_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf4_zandaka.uf4_name_ryakushiki is 'UF4名（略式）';
comment on column uf4_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf4_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf4_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf4_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf4_zandaka.kishu_zandaka is '期首残高';

comment on table uf5_ichiran is 'ユニバーサルフィールド５一覧';
comment on column uf5_ichiran.uf5_cd is 'UF5コード';
comment on column uf5_ichiran.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column uf5_ichiran.kessanki_bangou is '決算期番号';

comment on table uf5_zandaka is 'ユニバーサルフィールド５残高';
comment on column uf5_zandaka.uf5_cd is 'UF5コード';
comment on column uf5_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf5_zandaka.kessanki_bangou is '決算期番号';
comment on column uf5_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf5_zandaka.uf5_name_ryakushiki is 'UF5名（略式）';
comment on column uf5_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf5_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf5_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf5_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf5_zandaka.kishu_zandaka is '期首残高';

comment on table uf6_ichiran is 'ユニバーサルフィールド６一覧';
comment on column uf6_ichiran.uf6_cd is 'UF6コード';
comment on column uf6_ichiran.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column uf6_ichiran.kessanki_bangou is '決算期番号';

comment on table uf6_zandaka is 'ユニバーサルフィールド６残高';
comment on column uf6_zandaka.uf6_cd is 'UF6コード';
comment on column uf6_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf6_zandaka.kessanki_bangou is '決算期番号';
comment on column uf6_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf6_zandaka.uf6_name_ryakushiki is 'UF6名（略式）';
comment on column uf6_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf6_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf6_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf6_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf6_zandaka.kishu_zandaka is '期首残高';

comment on table uf7_ichiran is 'ユニバーサルフィールド７一覧';
comment on column uf7_ichiran.uf7_cd is 'UF7コード';
comment on column uf7_ichiran.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column uf7_ichiran.kessanki_bangou is '決算期番号';

comment on table uf7_zandaka is 'ユニバーサルフィールド７残高';
comment on column uf7_zandaka.uf7_cd is 'UF7コード';
comment on column uf7_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf7_zandaka.kessanki_bangou is '決算期番号';
comment on column uf7_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf7_zandaka.uf7_name_ryakushiki is 'UF7名（略式）';
comment on column uf7_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf7_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf7_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf7_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf7_zandaka.kishu_zandaka is '期首残高';

comment on table uf8_ichiran is 'ユニバーサルフィールド８一覧';
comment on column uf8_ichiran.uf8_cd is 'UF8コード';
comment on column uf8_ichiran.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column uf8_ichiran.kessanki_bangou is '決算期番号';

comment on table uf8_zandaka is 'ユニバーサルフィールド８残高';
comment on column uf8_zandaka.uf8_cd is 'UF8コード';
comment on column uf8_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf8_zandaka.kessanki_bangou is '決算期番号';
comment on column uf8_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf8_zandaka.uf8_name_ryakushiki is 'UF8名（略式）';
comment on column uf8_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf8_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf8_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf8_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf8_zandaka.kishu_zandaka is '期首残高';

comment on table uf9_ichiran is 'ユニバーサルフィールド９一覧';
comment on column uf9_ichiran.uf9_cd is 'UF9コード';
comment on column uf9_ichiran.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column uf9_ichiran.kessanki_bangou is '決算期番号';

comment on table uf9_zandaka is 'ユニバーサルフィールド９残高';
comment on column uf9_zandaka.uf9_cd is 'UF9コード';
comment on column uf9_zandaka.kamoku_gaibu_cd is '科目外部コード';
comment on column uf9_zandaka.kessanki_bangou is '決算期番号';
comment on column uf9_zandaka.kamoku_naibu_cd is '科目内部コード';
comment on column uf9_zandaka.uf9_name_ryakushiki is 'UF9名（略式）';
comment on column uf9_zandaka.chouhyou_shaturyoku_no is '帳票出力順番号';
comment on column uf9_zandaka.kamoku_name_ryakushiki is '科目名（略式）';
comment on column uf9_zandaka.kamoku_name_seishiki is '科目名（正式）';
comment on column uf9_zandaka.taishaku_zokusei is '貸借属性';
comment on column uf9_zandaka.kishu_zandaka is '期首残高';

comment on table user_default_value is 'ユーザー別デフォルト値';
comment on column user_default_value.kbn is '区分';
comment on column user_default_value.user_id is 'ユーザーID';
comment on column user_default_value.default_value is 'デフォルト値';

comment on table user_info is 'ユーザー情報';
comment on column user_info.user_id is 'ユーザーID';
comment on column user_info.shain_no is '社員番号';
comment on column user_info.user_sei is 'ユーザー姓';
comment on column user_info.user_mei is 'ユーザー名';
comment on column user_info.mail_address is 'メールアドレス';
comment on column user_info.yuukou_kigen_from is '有効期限開始日';
comment on column user_info.yuukou_kigen_to is '有効期限終了日';
comment on column user_info.touroku_user_id is '登録ユーザーID';
comment on column user_info.touroku_time is '登録日時';
comment on column user_info.koushin_user_id is '更新ユーザーID';
comment on column user_info.koushin_time is '更新日時';
comment on column user_info.pass_koushin_date is 'パスワード変更日';
comment on column user_info.pass_failure_count is 'パスワード誤り回数';
comment on column user_info.pass_failure_time is 'パスワード誤り時間';
comment on column user_info.tmp_lock_flg is 'アカウント一時ロックフラグ';
comment on column user_info.tmp_lock_time is 'アカウント一時ロック時間';
comment on column user_info.lock_flg is 'アカウントロックフラグ';
comment on column user_info.lock_time is 'アカウントロック時間';
comment on column user_info.dairikihyou_flg is '代理起票可能フラグ';
comment on column user_info.houjin_card_riyou_flag is '法人カード利用';
comment on column user_info.houjin_card_shikibetsuyou_num is '法人カード識別用番号';
comment on column user_info.security_pattern is 'セキュリティパターン';
comment on column user_info.security_wfonly_flg is 'セキュリティワークフロー限定フラグ';
comment on column user_info.shounin_route_henkou_level is '承認ルート変更権限レベル';
comment on column user_info.maruhi_kengen_flg is 'マル秘設定権限';
comment on column user_info.maruhi_kaijyo_flg is 'マル秘解除権限';
comment on column user_info.zaimu_kyoten_nyuryoku_only_flg is '拠点入力のみ使用フラグ';

comment on table version is 'ヴァージョン';
comment on column version.version is 'ヴァージョン';

comment on table yosan_kiankingaku_check is '予算・起案金額チェック';
comment on column yosan_kiankingaku_check.denpyou_id is '伝票ID';
comment on column yosan_kiankingaku_check.syuukei_bumon_cd is '集計部門コード';
comment on column yosan_kiankingaku_check.kamoku_gaibu_cd is '科目外部コード';
comment on column yosan_kiankingaku_check.kamoku_edaban_cd is '科目枝番コード';
comment on column yosan_kiankingaku_check.futan_bumon_cd is '負担部門コード';
comment on column yosan_kiankingaku_check.syuukei_bumon_name is '集計部門名';
comment on column yosan_kiankingaku_check.kamoku_name_ryakushiki is '科目名（略式）';
comment on column yosan_kiankingaku_check.edaban_name is '枝番名';
comment on column yosan_kiankingaku_check.futan_bumon_name is '負担部門名';
comment on column yosan_kiankingaku_check.kijun_kingaku is '基準金額';
comment on column yosan_kiankingaku_check.jissekigaku is '実績額';
comment on column yosan_kiankingaku_check.shinsei_kingaku is '申請金額';

comment on table yosan_kiankingaku_check_comment is '予算・起案金額チェックコメント';
comment on column yosan_kiankingaku_check_comment.denpyou_id is '伝票ID';
comment on column yosan_kiankingaku_check_comment.comment is 'コメント';

comment on table yosan_shikkou_shori_nengetsu is '予算執行処理年月';
comment on column yosan_shikkou_shori_nengetsu.from_nengetsu is '開始年月';
comment on column yosan_shikkou_shori_nengetsu.to_nengetsu is '終了年月';

comment on table invoice_start is 'インボイス開始';
comment on column invoice_start.invoice_flg is 'インボイス開始フラグ';
comment on column invoice_start.touroku_user_id is '登録ユーザーID';
comment on column invoice_start.touroku_time is '登録日時';

-- e文書検索の初期値挿入
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