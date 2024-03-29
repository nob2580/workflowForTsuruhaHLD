<%@page import="eteam.symbol.EteamSymbol"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="su" uri="http://java.sun.com/jsp/jstl/su"%>

					<!-- 入力フィールド -->
					<section class='print-unit'>
						<div class='form-horizontal'>
							<div class='control-group'>
								<input type = 'hidden' name='shiireZeiAnbun' value='${su:htmlEscape(shiireZeiAnbun)}'>
								<label class='control-label'>伝票種別</label>
								<div class='controls'>
									<input type='text' name='denpyouShubetsu' class='input-inline input-large' disabled value='${su:htmlEscape(denpyouShubetsu)}'>
								</div>
							</div>
<c:if test='${shiwakeEdanoEnabled}'>
							<div class='control-group'>
								<label class='control-label'><c:if test='${shiwakeEdanoEditable}'><span class='required'>*</span></c:if>取引コード</label>
								<div class='controls'>
									<input type='text' name='shiwakeEdano' class='input-inline input-small' <c:if test='${not shiwakeEdanoEditable}'>disabled</c:if> maxlength='8' value='${su:htmlEscape(shiwakeEdano)}'>
								</div>
							</div>
</c:if>
							<div class='control-group'>
								<label class='control-label'>有効期限</label>
								<div class='controls'>
									<b><span class='required'>* </span></b>
									<input type='text' name='yuukouKigenFrom' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenFrom)}'>
									<span> ～ </span>
									<input type='text' name='yuukouKigenTo' class='input-small datepicker' value='${su:htmlEscape(yuukouKigenTo)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>分類1</label>
								<div class='controls'>
									<input type='text' name='bunrui1' class='input-xlarge' maxlength="20" value='${su:htmlEscape(bunrui1)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>分類2</label>
								<div class='controls'>
									<input type='text' name='bunrui2' class='input-xlarge' maxlength="20" value='${su:htmlEscape(bunrui2)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'>分類3</label>
								<div class='controls'>
									<input type='text' name='bunrui3' class='input-xlarge' maxlength="20" value='${su:htmlEscape(bunrui3)}'>
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>取引名</label>
								<div class='controls'>
									<input type='text' name='torihikiNm' class='input-xlarge' maxlength="20" value='${su:htmlEscape(torihikiNm)}'>
									　仕訳データの「摘要」に使用されます
								</div>
							</div>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>摘要</label>
								<div class='controls'>
									<label class='radio inline'>
										<input type='radio' name='tekiyouNyuryokuOn' value='0' <c:if test='${"0" eq tekiyouNyuryokuOn}'>checked</c:if>>
										取引名と同じ
									</label>
									<label class='radio inline'>
										<input type='radio' name='tekiyouNyuryokuOn' value='1' <c:if test='${"1" eq tekiyouNyuryokuOn}'>checked</c:if>>
										取引名とは別
									</label>
									　<input type='text' name='tekiyou' class='input-xlarge' maxlength="20" value='${su:htmlEscape(tekiyou)}' <c:if test='${"0" eq tekiyouNyuryokuOn}'>disabled="disabled"</c:if>>
								</div>
							</div>
<c:if test='${inputSeigyo.defaultHyouji}'>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>デフォルト表示</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='defaultHyoujiOn' value='1' <c:if test='${"1" eq defaultHyoujiOn}'>checked</c:if>>
											表示する
										</label>
										<label class='radio inline'>
											<input type='radio' name='defaultHyoujiOn' value='0' <c:if test='${"0" eq defaultHyoujiOn}'>checked</c:if>>
											表示しない
										</label>
										（部門に関係なく選択できる仕訳パターンを登録する場合は、「表示する」を選択してください）
									</div>
								</div>
</c:if>
<c:if test='${inputSeigyo.kousaihiHyouji}'>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>交際費</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='kousaihiOn' value='1' <c:if test='${"1" eq kousaihiOn}'>checked</c:if>>
											表示する
										</label>
										<label class='radio inline'>
											<input type='radio' name='kousaihiOn' value='0' <c:if test='${"0" eq kousaihiOn}'>checked</c:if>>
											表示しない
										</label>
										（交際費を入力できる仕訳パターンを登録する場合は、「表示する」を選択してください）
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>人数項目</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='ninzuuOn' value='1' <c:if test='${"1" eq ninzuuOn}'>checked</c:if>>
											使用する
										</label>
										<label class='radio inline'>
											<input type='radio' name='ninzuuOn' value='0' <c:if test='${"0" eq ninzuuOn}'>checked</c:if>>
											使用しない
										</label>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>交際費基準額（税込）</label>
									<div class='controls'>
										<input type='text' name='kousaihiKijungaku' class='input-small autoNumeric' maxlength="7" value='${su:htmlEscape(kousaihiKijungaku)}'>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>チェック方法</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='kousaihiCheckHouhou' value='0' <c:if test='${"0" eq kousaihiCheckHouhou}'>checked</c:if>>
											チェックしない
										</label>
										<label class='radio inline'>
											<input type='radio' name='kousaihiCheckHouhou' value='1' <c:if test='${"1" eq kousaihiCheckHouhou}'>checked</c:if>>
											基準額超過時にチェックする
										</label>
										<label class='radio inline'>
											<input type='radio' name='kousaihiCheckHouhou' value='2' <c:if test='${"2" eq kousaihiCheckHouhou}'>checked</c:if>>
											基準額以下の時にチェックする
										</label>
									</div>
								</div>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>チェック時の処理</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='kousaihiCheckResult' value='1' <c:if test='${"1" eq kousaihiCheckResult}'>checked</c:if>>
											伝票登録を許可する
										</label>
										<label class='radio inline'>
											<input type='radio' name='kousaihiCheckResult' value='0' <c:if test='${"0" eq kousaihiCheckResult}'>checked</c:if>>
											伝票登録を許可しない
										</label>
									</div>
								</div>
</c:if>
<c:if test='${inputSeigyo.kakeHyouji}'>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>掛け</label>
									<div class='controls'>
										<label class='radio inline'>
											<input type='radio' name='kakeOn' value='1' <c:if test='${"1" eq kakeOn}'>checked</c:if>>
											あり
										</label>
										<label class='radio inline'>
											<input type='radio' name='kakeOn' value='0' <c:if test='${"0" eq kakeOn}'>checked</c:if>>
											なし
										</label>
										（「あり」の場合、明細単位に計上仕訳と支払仕訳を作成。「なし」の場合、明細単位に計上仕訳=支払仕訳を作成します。）
									</div>
								</div>
</c:if>
							<div class='control-group'>
								<label class='control-label'><span class='required'>*</span>表示順</label>
								<div class='controls'>
									<input type='text' name='hyoujijun' maxlength='4' class='input-mini' value='${su:htmlEscape(hyoujijun)}'>
								</div>
							</div>
<c:if test='${inputSeigyo.shainCdRenkeiHyouji}'>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>社員コード連携</label>
									<div class='controls'>
										<select name='shainCdRenkei' class='input-small' <c:if test='${not shainCdRenkeiUmu}'>disabled</c:if>>
											<option value='0' <c:if test='${shainCdRenkei eq "0"}'>selected</c:if>>なし</option>
											<option value='1' <c:if test='${shainCdRenkei eq "1"}'>selected</c:if>>あり</option>
										</select>
									</div>
								</div>
</c:if>
<c:if test='${inputSeigyo.zaimuEdabanRenkeiHyouji}'>
								<div class='control-group'>
									<label class='control-label'><span class='required'>*</span>財務枝番コード連携</label>
									<div class='controls'>
										<select name='zaimuEdabanRenkei' class='input-small'>
											<option value='0' <c:if test='${zaimuEdabanRenkei eq "0"}'>selected</c:if>>しない</option>
											<option value='1' <c:if test='${zaimuEdabanRenkei eq "1"}'>selected</c:if>>する</option>
										</select>
									</div>
								</div>
</c:if>
						</div>
					</section>

					<!-- 貸借 -->
					<section>
						<div class='no-more-tables'><table id='taishaku' class='table-bordered table-condensed'>
							<thead>
								<tr>
									<th style='width: 110px'>項目</th>
									<th style='width: 225px'>借方</th>
									<th style='width: 225px'>貸方1</th>
									<th style='width: 225px'>貸方2</th>
									<th style='width: 225px'>貸方3</th>
									<th style='width: 225px'>貸方4</th>
									<th style='width: 225px'>貸方5</th>
								</tr>
							</thead>
							<tbody>
<c:if test='${inputSeigyo.kariKamoku || inputSeigyo.kashi1Kamoku || inputSeigyo.kashi2Kamoku || inputSeigyo.kashi3Kamoku || inputSeigyo.kashi4Kamoku || inputSeigyo.kashi5Kamoku}'>
								<tr>
									<th class='row-title'><span class='required'>*</span><nobr>勘定科目</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariKamoku}'>
										<button type='button' id='kariKanjyouKamokuSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariKanjyouKamokuCd' class='input-small' autocomplete="on" list="dtKariKamoku" value='${su:htmlEscape(kariKanjyouKamokuCd)}'>
										<input type='text' name='kariKanjyouKamokuName' class='input-large' disabled value='${su:htmlEscape(kariKanjyouKamokuName)}'>
										<input type="hidden" name="kariShoriGroup">
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1Kamoku}'>
										<button type='button' id='kashiKanjyouKamokuSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKanjyouKamokuCd1' class='input-small' autocomplete="on" list="dtKashi1Kamoku" value='${su:htmlEscape(kashiKanjyouKamokuCd1)}'>
										<input type='text' name='kashiKanjyouKamokuName1' class='input-large' disabled value='${su:htmlEscape(kashiKanjyouKamokuName1)}'>
										<input type="hidden" name="kashiShoriGroup1">
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2Kamoku}'>
										<button type='button' id='kashiKanjyouKamokuSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKanjyouKamokuCd2' class='input-small' autocomplete="on" list="dtKashi2Kamoku" value='${su:htmlEscape(kashiKanjyouKamokuCd2)}'>
										<input type='text' name='kashiKanjyouKamokuName2' class='input-large' disabled value='${su:htmlEscape(kashiKanjyouKamokuName2)}'>
										<input type="hidden" name="kashiShoriGroup2">
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3Kamoku}'>
										<button type='button' id='kashiKanjyouKamokuSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKanjyouKamokuCd3' class='input-small' autocomplete="on" list="dtKashi3Kamoku" value='${su:htmlEscape(kashiKanjyouKamokuCd3)}'>
										<input type='text' name='kashiKanjyouKamokuName3' class='input-large' disabled value='${su:htmlEscape(kashiKanjyouKamokuName3)}'>
										<input type="hidden" name="kashiShoriGroup3">
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4Kamoku}'>
										<button type='button' id='kashiKanjyouKamokuSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKanjyouKamokuCd4' class='input-small' autocomplete="on" list="dtKashi4Kamoku" value='${su:htmlEscape(kashiKanjyouKamokuCd4)}'>
										<input type='text' name='kashiKanjyouKamokuName4' class='input-large' disabled value='${su:htmlEscape(kashiKanjyouKamokuName4)}'>
										<input type="hidden" name="kashiShoriGroup4">
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5Kamoku}'>
										<button type='button' id='kashiKanjyouKamokuSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKanjyouKamokuCd5' class='input-small' autocomplete="on" list="dtKashi5Kamoku" value='${su:htmlEscape(kashiKanjyouKamokuCd5)}'>
										<input type='text' name='kashiKanjyouKamokuName5' class='input-large' disabled value='${su:htmlEscape(kashiKanjyouKamokuName5)}'>
										<input type="hidden" name="kashiShoriGroup5">
	</c:if>
									</td>
								</tr>
</c:if>

<c:if test='${inputSeigyo.kariKazeiKbn || inputSeigyo.kashi1KazeiKbn || inputSeigyo.kashi2KazeiKbn || inputSeigyo.kashi3KazeiKbn || inputSeigyo.kashi4KazeiKbn || inputSeigyo.kashi5KazeiKbn}'>
								<tr>
									<th class='row-title'><span class='required'>*</span><nobr>課税区分</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariKazeiKbn}'>
										<select name='kariKazeiKbn' class='input-small'>
											<c:forEach var='kariKazeiKbnList' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kariKazeiKbnList.naibuCd)}' data-kazeiKbnGroup='${kariKazeiKbnList.option1}' <c:if test='${kariKazeiKbnList.naibuCd eq kariKazeiKbn}'>selected</c:if>>${su:htmlEscape(kariKazeiKbnList.name)}</option>
											</c:forEach>
										</select>
										<span id="kariDefaultKazeiKbn"></span>
	</c:if>
									</td>
									<td>
								<c:if test='${inputSeigyo.kashi1KazeiKbn}'>
										<select name='kashiKazeiKbn1' class='input-small' disabled>
		<c:forEach var='kashiKazeiKbn1List' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kashiKazeiKbn1List.naibuCd)}' <c:if test='${kashiKazeiKbn1List.naibuCd eq kashiKazeiKbn1}'>selected</c:if>>${su:htmlEscape(kashiKazeiKbn1List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2KazeiKbn}'>
										<select name='kashiKazeiKbn2' class='input-small' disabled>
		<c:forEach var='kashiKazeiKbn2List' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kashiKazeiKbn2List.naibuCd)}' <c:if test='${kashiKazeiKbn2List.naibuCd eq kashiKazeiKbn2}'>selected</c:if>>${su:htmlEscape(kashiKazeiKbn2List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3KazeiKbn}'>
										<select name='kashiKazeiKbn3' class='input-small' disabled>
		<c:forEach var='kashiKazeiKbn3List' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kashiKazeiKbn3List.naibuCd)}' <c:if test='${kashiKazeiKbn3List.naibuCd eq kashiKazeiKbn3}'>selected</c:if>>${su:htmlEscape(kashiKazeiKbn3List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4KazeiKbn}'>
										<select name='kashiKazeiKbn4' class='input-small' disabled>
		<c:forEach var='kashiKazeiKbn4List' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kashiKazeiKbn4List.naibuCd)}' <c:if test='${kashiKazeiKbn4List.naibuCd eq kashiKazeiKbn4}'>selected</c:if>>${su:htmlEscape(kashiKazeiKbn4List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5KazeiKbn}'>
										<select name='kashiKazeiKbn5' class='input-small' disabled>
		<c:forEach var='kashiKazeiKbn5List' items='${kazeiKbnList}'>
												<option value='${su:htmlEscape(kashiKazeiKbn5List.naibuCd)}' <c:if test='${kashiKazeiKbn5List.naibuCd eq kashiKazeiKbn5}'>selected</c:if>>${su:htmlEscape(kashiKazeiKbn5List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
								</tr>
</c:if>

<c:if test='${inputSeigyo.kariZeiritsu}'>
								<tr>
									<th class='row-title'><span class='required'>*</span><nobr>消費税率</nobr></th>
									<td>
										<select name='kariZeiritsu' class='input-medium'>
											<option value='<ZEIRITSU>' data-keigenZeiritsuKbn='' <c:if test='${"<ZEIRITSU>" eq kariZeiritsu}'>selected</c:if>>任意消費税率</option>
											<c:forEach var="kariZeiritsuList" items="${zeiritsuList}">
												<option value='${kariZeiritsuList.zeiritsu}' data-keigenZeiritsuKbn='${kariZeiritsuList.keigen_zeiritsu_kbn}' <c:if test='${kariZeiritsuList.zeiritsu eq kariZeiritsu && kariZeiritsuList.keigen_zeiritsu_kbn eq kariKeigenZeiritsuKbn}'>selected</c:if>><c:if test='${kariZeiritsuList.keigen_zeiritsu_kbn eq 1}'>*</c:if>${kariZeiritsuList.zeiritsu}%</option>
											</c:forEach>
										</select>
										<input type="hidden" name='kariKeigenZeiritsuKbn' value='${su:htmlEscape(kariKeigenZeiritsuKbn)}'>
										<span id="kariDefaultZeiritsu"></span>
									</td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
</c:if>

<c:if test='${inputSeigyo.kariBunriKbn || inputSeigyo.kashi1BunriKbn || inputSeigyo.kashi2BunriKbn || inputSeigyo.kashi3BunriKbn || inputSeigyo.kashi4BunriKbn || inputSeigyo.kashi5BunriKbn}'>
								<tr>
									<th class='row-title'><nobr>分離区分</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariBunriKbn}'>
										<select name='kariBunriKbn' class='input-small'>
											<c:forEach var='kariBunriKbnList' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kariBunriKbnList.naibuCd)}' data-bunriKbnGroup='${kariBunriKbnList.option1}' <c:if test='${kariBunriKbnList.naibuCd eq kariBunriKbn}'>selected</c:if>>${su:htmlEscape(kariBunriKbnList.name)}</option>
											</c:forEach>
										</select>
										<span id="kariDefaultBunriKbn"></span>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1BunriKbn}'>
										<select name='kashiBunriKbn1' class='input-small' disabled>
		<c:forEach var='kashiBunriKbn1List' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kashiBunriKbn1List.naibuCd)}' <c:if test='${kashiBunriKbn1List.naibuCd eq kashiBunriKbn1}'>selected</c:if>>${su:htmlEscape(kashiBunriKbn1List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2BunriKbn}'>
										<select name='kashiBunriKbn2' class='input-small' disabled>
		<c:forEach var='kashiBunriKbn2List' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kashiBunriKbn2List.naibuCd)}' <c:if test='${kashiBunriKbn2List.naibuCd eq kashiBunriKbn2}'>selected</c:if>>${su:htmlEscape(kashiBunriKbn2List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3BunriKbn}'>
										<select name='kashiBunriKbn3' class='input-small' disabled>
		<c:forEach var='kashiBunriKbn3List' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kashiBunriKbn3List.naibuCd)}' <c:if test='${kashiBunriKbn3List.naibuCd eq kashiBunriKbn3}'>selected</c:if>>${su:htmlEscape(kashiBunriKbn3List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4BunriKbn}'>
										<select name='kashiBunriKbn4' class='input-small' disabled>
		<c:forEach var='kashiBunriKbn4List' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kashiBunriKbn4List.naibuCd)}' <c:if test='${kashiBunriKbn4List.naibuCd eq kashiBunriKbn4}'>selected</c:if>>${su:htmlEscape(kashiBunriKbn4List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5BunriKbn}'>
										<select name='kashiBunriKbn5' class='input-small' disabled>
		<c:forEach var='kashiBunriKbn5List' items='${bunriKbnList}'>
												<option value='${su:htmlEscape(kashiBunriKbn5List.naibuCd)}' <c:if test='${kashiBunriKbn5List.naibuCd eq kashiBunriKbn5}'>selected</c:if>>${su:htmlEscape(kashiBunriKbn5List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
								</tr>
</c:if>

<c:if test='${inputSeigyo.kariShiireKbn || inputSeigyo.kashi1ShiireKbn || inputSeigyo.kashi2ShiireKbn || inputSeigyo.kashi3ShiireKbn || inputSeigyo.kashi4ShiireKbn || inputSeigyo.kashi5ShiireKbn}'>
								<tr <c:if test='${shiireZeiAnbun eq "0"}'>style='display:none'</c:if>>
									<th class='row-title'><nobr>仕入区分</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariShiireKbn}'>
										<select name='kariShiireKbn' class='input-small'>
											<c:forEach var='kariShiireKbnList' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kariShiireKbnList.naibuCd)}' data-shiireKbnGroup='${kariShiireKbnList.option1}' <c:if test='${kariShiireKbnList.naibuCd eq kariShiireKbn}'>selected</c:if>>${su:htmlEscape(kariShiireKbnList.name)}</option>
											</c:forEach>
										</select>
										<span id="kariDefaultShiireKbn"></span>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1ShiireKbn}'>
										<select name='kashiShiireKbn1' class='input-small' disabled>
		<c:forEach var='kashiShiireKbn1List' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kashiShiireKbn1List.naibuCd)}' <c:if test='${kashiShiireKbn1List.naibuCd eq kashiShiireKbn1}'>selected</c:if>>${su:htmlEscape(kashiShiireKbn1List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2ShiireKbn}'>
										<select name='kashiShiireKbn2' class='input-small' disabled>
		<c:forEach var='kashiShiireKbn2List' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kashiShiireKbn2List.naibuCd)}' <c:if test='${kashiShiireKbn2List.naibuCd eq kashiShiireKbn2}'>selected</c:if>>${su:htmlEscape(kashiShiireKbn2List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3ShiireKbn}'>
										<select name='kashiShiireKbn3' class='input-small' disabled>
		<c:forEach var='kashiShiireKbn3List' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kashiShiireKbn3List.naibuCd)}' <c:if test='${kashiShiireKbn3List.naibuCd eq kashiShiireKbn3}'>selected</c:if>>${su:htmlEscape(kashiShiireKbn3List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4ShiireKbn}'>
										<select name='kashiShiireKbn4' class='input-small' disabled>
		<c:forEach var='kashiShiireKbn4List' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kashiShiireKbn4List.naibuCd)}' <c:if test='${kashiShiireKbn4List.naibuCd eq kashiShiireKbn4}'>selected</c:if>>${su:htmlEscape(kashiShiireKbn4List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5ShiireKbn}'>
										<select name='kashiShiireKbn5' class='input-small' disabled>
		<c:forEach var='kashiShiireKbn5List' items='${shiireKbnList}'>
												<option value='${su:htmlEscape(kashiShiireKbn5List.naibuCd)}' <c:if test='${kashiShiireKbn5List.naibuCd eq kashiShiireKbn5}'>selected</c:if>>${su:htmlEscape(kashiShiireKbn5List.name)}</option>
		</c:forEach>
										</select>
	</c:if>
									</td>
								</tr>
</c:if>

<c:if test='${inputSeigyo.kariKamokuEda || inputSeigyo.kashi1KamokuEda || inputSeigyo.kashi2KamokuEda || inputSeigyo.kashi3KamokuEda || inputSeigyo.kashi4KamokuEda || inputSeigyo.kashi5KamokuEda}'>
								<tr>
									<th class='row-title'><nobr>勘定科目枝番</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariKamokuEda}'>
										<button type='button' id='kariKamokuEdabanSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariKamokuEdabanCd' class='input-medium' autocomplete="on" list="dtKariEdano" value='${su:htmlEscape(kariKamokuEdabanCd)}'>
										<input type='text' name='kariKamokuEdabanName' class='input-large' disabled value='${su:htmlEscape(kariKamokuEdabanName)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1KamokuEda}'>
										<button type='button' id='kashiKamokuEdabanSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKamokuEdabanCd1' class='input-medium' autocomplete="on" list="dtKashi1Edano" value='${su:htmlEscape(kashiKamokuEdabanCd1)}'>
										<input type='text' name='kashiKamokuEdabanName1' class='input-large' disabled value='${su:htmlEscape(kashiKamokuEdabanName1)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2KamokuEda}'>
										<button type='button' id='kashiKamokuEdabanSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKamokuEdabanCd2' class='input-medium' autocomplete="on" list="dtKashi2Edano" value='${su:htmlEscape(kashiKamokuEdabanCd2)}'>
										<input type='text' name='kashiKamokuEdabanName2' class='input-large' disabled value='${su:htmlEscape(kashiKamokuEdabanName2)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3KamokuEda}'>
										<button type='button' id='kashiKamokuEdabanSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKamokuEdabanCd3' class='input-medium' autocomplete="on" list="dtKashi3Edano" value='${su:htmlEscape(kashiKamokuEdabanCd3)}'>
										<input type='text' name='kashiKamokuEdabanName3' class='input-large' disabled value='${su:htmlEscape(kashiKamokuEdabanName3)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4KamokuEda}'>
										<button type='button' id='kashiKamokuEdabanSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKamokuEdabanCd4' class='input-medium' autocomplete="on" list="dtKashi4Edano" value='${su:htmlEscape(kashiKamokuEdabanCd4)}'>
										<input type='text' name='kashiKamokuEdabanName4' class='input-large' disabled value='${su:htmlEscape(kashiKamokuEdabanName4)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5KamokuEda}'>
										<button type='button' id='kashiKamokuEdabanSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiKamokuEdabanCd5' class='input-medium' autocomplete="on" list="dtKashi5Edano" value='${su:htmlEscape(kashiKamokuEdabanCd5)}'>
										<input type='text' name='kashiKamokuEdabanName5' class='input-large' disabled value='${su:htmlEscape(kashiKamokuEdabanName5)}'>
	</c:if>
									</td>
								</tr>
</c:if>
<c:if test='${inputSeigyo.kashi1Bumon || inputSeigyo.kashi1Bumon || inputSeigyo.kashi2Bumon || inputSeigyo.kashi3Bumon || inputSeigyo.kashi4Bumon || inputSeigyo.kashi5Bumon}'>
								<tr>
									<th class='row-title'><nobr>負担部門</nobr></th>
									<td>
									<c:if test='${inputSeigyo.kariBumon}'>
										<button type='button' id='kariFutanBumonSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariFutanBumonCd' class='input-small' autocomplete="on" list="dtKariBumon" value='${su:htmlEscape(kariFutanBumonCd)}'>
										<input type='text' name='kariFutanBumonName' class='input-large' disabled value='${su:htmlEscape(kariFutanBumonName)}'>
									</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Bumon}'>
										<button type='button' id='kashiFutanBumonSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiFutanBumonCd1' class='input-small' autocomplete="on" list="dtKashi1Bumon" value='${su:htmlEscape(kashiFutanBumonCd1)}'>
										<input type='text' name='kashiFutanBumonName1' class='input-large' disabled value='${su:htmlEscape(kashiFutanBumonName1)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2Bumon}'>
										<button type='button' id='kashiFutanBumonSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiFutanBumonCd2' class='input-small' autocomplete="on" list="dtKashi2Bumon" value='${su:htmlEscape(kashiFutanBumonCd2)}'>
										<input type='text' name='kashiFutanBumonName2' class='input-large' disabled value='${su:htmlEscape(kashiFutanBumonName2)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3Bumon}'>
										<button type='button' id='kashiFutanBumonSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiFutanBumonCd3' class='input-small' autocomplete="on" list="dtKashi3Bumon" value='${su:htmlEscape(kashiFutanBumonCd3)}'>
										<input type='text' name='kashiFutanBumonName3' class='input-large' disabled value='${su:htmlEscape(kashiFutanBumonName3)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4Bumon}'>
										<button type='button' id='kashiFutanBumonSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiFutanBumonCd4' class='input-small' autocomplete="on" list="dtKashi4Bumon" value='${su:htmlEscape(kashiFutanBumonCd4)}'>
										<input type='text' name='kashiFutanBumonName4' class='input-large' disabled value='${su:htmlEscape(kashiFutanBumonName4)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5Bumon}'>
										<button type='button' id='kashiFutanBumonSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiFutanBumonCd5' class='input-small' autocomplete="on" list="dtKashi5Bumon" value='${su:htmlEscape(kashiFutanBumonCd5)}'>
										<input type='text' name='kashiFutanBumonName5' class='input-large' disabled value='${su:htmlEscape(kashiFutanBumonName5)}'>
	</c:if>
									</td>
								</tr>
</c:if>
<c:if test='${inputSeigyo.kariTorihiki || inputSeigyo.kashi1Torihiki || inputSeigyo.kashi2Torihiki || inputSeigyo.kashi3Torihiki || inputSeigyo.kashi4Torihiki || inputSeigyo.kashi5Torihiki}'>
								<tr>
									<th class='row-title'><nobr>取引先</nobr></th>
									<td>
	<c:if test='${inputSeigyo.kariTorihiki}'>
										<button type='button' id='kariTorihikisakiSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariTorihikisakiCd' class='input-medium' autocomplete="on" list="dtKariTorihiki" value='${su:htmlEscape(kariTorihikisakiCd)}'>
										<input type='text' name='kariTorihikisakiName' class='input-large' disabled value='${su:htmlEscape(kariTorihikisakiName)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1Torihiki}'>
										<button type='button' id='kashiTorihikisakiSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiTorihikisakiCd1' class='input-medium' autocomplete="on" list="dtKashi1Torihiki" value='${su:htmlEscape(kashiTorihikisakiCd1)}'>
										<input type='text' name='kashiTorihikisakiName1' class='input-large' disabled value='${su:htmlEscape(kashiTorihikisakiName1)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2Torihiki}'>
										<button type='button' id='kashiTorihikisakiSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiTorihikisakiCd2' class='input-medium' autocomplete="on" list="dtKashi2Torihiki" value='${su:htmlEscape(kashiTorihikisakiCd2)}'>
										<input type='text' name='kashiTorihikisakiName2' class='input-large' disabled value='${su:htmlEscape(kashiTorihikisakiName2)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3Torihiki}'>
										<button type='button' id='kashiTorihikisakiSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiTorihikisakiCd3' class='input-medium' autocomplete="on" list="dtKashi3Torihiki" value='${su:htmlEscape(kashiTorihikisakiCd3)}'>
										<input type='text' name='kashiTorihikisakiName3' class='input-large' disabled value='${su:htmlEscape(kashiTorihikisakiName3)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4Torihiki}'>
										<button type='button' id='kashiTorihikisakiSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiTorihikisakiCd4' class='input-medium' autocomplete="on" list="dtKashi4Torihiki" value='${su:htmlEscape(kashiTorihikisakiCd4)}'>
										<input type='text' name='kashiTorihikisakiName4' class='input-large' disabled value='${su:htmlEscape(kashiTorihikisakiName4)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5Torihiki}'>
										<button type='button' id='kashiTorihikisakiSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiTorihikisakiCd5' class='input-medium' autocomplete="on" list="dtKashi5Torihiki" value='${su:htmlEscape(kashiTorihikisakiCd5)}'>
										<input type='text' name='kashiTorihikisakiName5' class='input-large' disabled value='${su:htmlEscape(kashiTorihikisakiName5)}'>
	</c:if>
									</td>
								</tr>
</c:if>
<c:if test='${inputSeigyo.kariProject}'>
								<tr>
									<th class='row-title'><nobr>プロジェクト</nobr></th>
									<td>
										<button type='button' id='kariProjectSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariProjectCd' class='input-medium' autocomplete="on" list="dtKariProject" value='${su:htmlEscape(kariProjectCd)}'>
										<input type='text' name='kariProjectName' class='input-large' disabled value='${su:htmlEscape(kariProjectName)}'>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1Project}'>
										<button type='button' id='kashiProjectSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiProjectCd1' class='input-medium' autocomplete="on" list="dtKashi1Project" value='${su:htmlEscape(kashiProjectCd1)}'>
										<input type='text' name='kashiProjectName1' class='input-large' disabled value='${su:htmlEscape(kashiProjectName1)}'>
	</c:if>	
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2Project}'>
										<button type='button' id='kashiProjectSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiProjectCd2' class='input-medium' autocomplete="on" list="dtKashi2Project" value='${su:htmlEscape(kashiProjectCd2)}'>
										<input type='text' name='kashiProjectName2' class='input-large' disabled value='${su:htmlEscape(kashiProjectName2)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3Project}'>
										<button type='button' id='kashiProjectSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiProjectCd3' class='input-medium' autocomplete="on" list="dtKashi3Project" value='${su:htmlEscape(kashiProjectCd3)}'>
										<input type='text' name='kashiProjectName3' class='input-large' disabled value='${su:htmlEscape(kashiProjectName3)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4Project}'>
										<button type='button' id='kashiProjectSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiProjectCd4' class='input-medium' autocomplete="on" list="dtKashi4Project" value='${su:htmlEscape(kashiProjectCd4)}'>
										<input type='text' name='kashiProjectName4' class='input-large' disabled value='${su:htmlEscape(kashiProjectName4)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5Project}'>
										<button type='button' id='kashiProjectSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiProjectCd5' class='input-medium' autocomplete="on" list="dtKashi5Project" value='${su:htmlEscape(kashiProjectCd5)}'>
										<input type='text' name='kashiProjectName5' class='input-large' disabled value='${su:htmlEscape(kashiProjectName5)}'>
	</c:if>
									</td>
								</tr>
</c:if>
<c:if test='${inputSeigyo.kariSegment}'>
								<tr>
									<th class='row-title'><nobr>セグメント</nobr></th>
									<td>
										<button type='button' id='kariSegmentSentaku' class='btn btn-small'>選択</button>
										<input type='text' name='kariSegmentCd' class='input-medium' autocomplete="on" list="dtKariSegment" value='${su:htmlEscape(kariSegmentCd)}'>
										<input type='text' name='kariSegmentName' class='input-large' disabled value='${su:htmlEscape(kariSegmentName)}'>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi1Segment}'>
										<button type='button' id='kashiSegmentSentaku1' class='btn btn-small'>選択</button>
										<input type='text' name='kashiSegmentCd1' class='input-medium' autocomplete="on" list="dtKashi1Segment" value='${su:htmlEscape(kashiSegmentCd1)}'>
										<input type='text' name='kashiSegmentName1' class='input-large' disabled value='${su:htmlEscape(kashiSegmentName1)}'>
	</c:if>	
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi2Segment}'>
										<button type='button' id='kashiSegmentSentaku2' class='btn btn-small'>選択</button>
										<input type='text' name='kashiSegmentCd2' class='input-medium' autocomplete="on" list="dtKashi2Segment" value='${su:htmlEscape(kashiSegmentCd2)}'>
										<input type='text' name='kashiSegmentName2' class='input-large' disabled value='${su:htmlEscape(kashiSegmentName2)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi3Segment}'>
										<button type='button' id='kashiSegmentSentaku3' class='btn btn-small'>選択</button>
										<input type='text' name='kashiSegmentCd3' class='input-medium' autocomplete="on" list="dtKashi3Segment" value='${su:htmlEscape(kashiSegmentCd3)}'>
										<input type='text' name='kashiSegmentName3' class='input-large' disabled value='${su:htmlEscape(kashiSegmentName3)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi4Segment}'>
										<button type='button' id='kashiSegmentSentaku4' class='btn btn-small'>選択</button>
										<input type='text' name='kashiSegmentCd4' class='input-medium' autocomplete="on" list="dtKashi4Segment" value='${su:htmlEscape(kashiSegmentCd4)}'>
										<input type='text' name='kashiSegmentName4' class='input-large' disabled value='${su:htmlEscape(kashiSegmentName4)}'>
	</c:if>
									</td>
									<td>
	<c:if test='${inputSeigyo.kashi5Segment}'>
										<button type='button' id='kashiSegmentSentaku5' class='btn btn-small'>選択</button>
										<input type='text' name='kashiSegmentCd5' class='input-medium' autocomplete="on" list="dtKashi5Segment" value='${su:htmlEscape(kashiSegmentCd5)}'>
										<input type='text' name='kashiSegmentName5' class='input-large' disabled value='${su:htmlEscape(kashiSegmentName5)}'>
	</c:if>
									</td>
								</tr>
</c:if>
								<tr id="uf1" <c:if test='${not ("0" ne hfUfSeigyo.uf1ShiyouFlg and ks.uf1.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf1Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kariUf1Cd" maxlength='20' value='${su:htmlEscape(kariUf1Cd)}' autocomplete="on" list="dtKariUf1">
				<input type='hidden' name="kariUf1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kariUf1SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf1Cd" class='input-small' value='${su:htmlEscape(kariUf1Cd)}' autocomplete="on" list="dtKariUf1">
				<input type='text' name="kariUf1Name" class='input-large' disabled value='${su:htmlEscape(kariUf1Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kashiUf1Cd1" maxlength='20' value='${su:htmlEscape(kashiUf1Cd1)}' autocomplete="on" list="dtKashi1Uf1">
				<input type='hidden' name="kashiUf1Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kashiUf1SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf1Cd1" class='input-small' value='${su:htmlEscape(kashiUf1Cd1)}' autocomplete="on" list="dtKashi1Uf1">
				<input type='text' name="kashiUf1Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf1Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kashiUf1Cd2" maxlength='20' value='${su:htmlEscape(kashiUf1Cd2)}' autocomplete="on" list="dtKashi2Uf1">
				<input type='hidden' name="kashiUf1Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kashiUf1SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf1Cd2" class='input-small' value='${su:htmlEscape(kashiUf1Cd2)}' autocomplete="on" list="dtKashi2Uf1">
				<input type='text' name="kashiUf1Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf1Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kashiUf1Cd3" maxlength='20' value='${su:htmlEscape(kashiUf1Cd3)}' autocomplete="on" list="dtKashi3Uf1">
				<input type='hidden' name="kashiUf1Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kashiUf1SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf1Cd3" class='input-small' value='${su:htmlEscape(kashiUf1Cd3)}' autocomplete="on" list="dtKashi3Uf1">
				<input type='text' name="kashiUf1Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf1Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kashiUf1Cd4" maxlength='20' value='${su:htmlEscape(kashiUf1Cd4)}' autocomplete="on" list="dtKashi4Uf1">
				<input type='hidden' name="kashiUf1Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kashiUf1SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf1Cd4" class='input-small' value='${su:htmlEscape(kashiUf1Cd4)}' autocomplete="on" list="dtKashi4Uf1">
				<input type='text' name="kashiUf1Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf1Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf1}'>
<c:if test='${"0" eq hfUfSeigyo.uf1ShiyouFlg or "1" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<input type='text' name="kashiUf1Cd5" maxlength='20' value='${su:htmlEscape(kashiUf1Cd5)}' autocomplete="on" list="dtKashi5Uf1">
				<input type='hidden' name="kashiUf1Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf1ShiyouFlg or "3" eq hfUfSeigyo.uf1ShiyouFlg}'>
				<button type='button' id='kashiUf1SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf1Cd5" class='input-small' value='${su:htmlEscape(kashiUf1Cd5)}' autocomplete="on" list="dtKashi5Uf1">
				<input type='text' name="kashiUf1Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf1Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf2" <c:if test='${not ("0" ne hfUfSeigyo.uf2ShiyouFlg and ks.uf2.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf2Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kariUf2Cd" maxlength='20' value='${su:htmlEscape(kariUf2Cd)}' autocomplete="on" list="dtKariUf2">
				<input type='hidden' name="kariUf2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kariUf2SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf2Cd" class='input-small' value='${su:htmlEscape(kariUf2Cd)}' autocomplete="on" list="dtKariUf2">
				<input type='text' name="kariUf2Name" class='input-large' disabled value='${su:htmlEscape(kariUf2Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kashiUf2Cd1" maxlength='20' value='${su:htmlEscape(kashiUf2Cd1)}' autocomplete="on" list="dtKashi1Uf2">
				<input type='hidden' name="kashiUf2Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kashiUf2SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf2Cd1" class='input-small' value='${su:htmlEscape(kashiUf2Cd1)}' autocomplete="on" list="dtKashi1Uf2">
				<input type='text' name="kashiUf2Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf2Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kashiUf2Cd2" maxlength='20' value='${su:htmlEscape(kashiUf2Cd2)}' autocomplete="on" list="dtKashi2Uf2">
				<input type='hidden' name="kashiUf2Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kashiUf2SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf2Cd2" class='input-small' value='${su:htmlEscape(kashiUf2Cd2)}' autocomplete="on" list="dtKashi2Uf2">
				<input type='text' name="kashiUf2Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf2Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kashiUf2Cd3" maxlength='20' value='${su:htmlEscape(kashiUf2Cd3)}' autocomplete="on" list="dtKashi3Uf2">
				<input type='hidden' name="kashiUf2Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kashiUf2SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf2Cd3" class='input-small' value='${su:htmlEscape(kashiUf2Cd3)}' autocomplete="on" list="dtKashi3Uf2">
				<input type='text' name="kashiUf2Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf2Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kashiUf2Cd4" maxlength='20' value='${su:htmlEscape(kashiUf2Cd4)}' autocomplete="on" list="dtKashi4Uf2">
				<input type='hidden' name="kashiUf2Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kashiUf2SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf2Cd4" class='input-small' value='${su:htmlEscape(kashiUf2Cd4)}' autocomplete="on" list="dtKashi4Uf2">
				<input type='text' name="kashiUf2Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf2Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf2}'>
<c:if test='${"0" eq hfUfSeigyo.uf2ShiyouFlg or "1" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<input type='text' name="kashiUf2Cd5" maxlength='20' value='${su:htmlEscape(kashiUf2Cd5)}' autocomplete="on" list="dtKashi5Uf2">
				<input type='hidden' name="kashiUf2Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf2ShiyouFlg or "3" eq hfUfSeigyo.uf2ShiyouFlg}'>
				<button type='button' id='kashiUf2SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf2Cd5" class='input-small' value='${su:htmlEscape(kashiUf2Cd5)}' autocomplete="on" list="dtKashi5Uf1">
				<input type='text' name="kashiUf2Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf2Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf3" <c:if test='${not ("0" ne hfUfSeigyo.uf3ShiyouFlg and ks.uf3.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf3Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kariUf3Cd" maxlength='20' value='${su:htmlEscape(kariUf3Cd)}' autocomplete="on" list="dtKariUf3">
				<input type='hidden' name="kariUf3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kariUf3SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf3Cd" class='input-small' value='${su:htmlEscape(kariUf3Cd)}' autocomplete="on" list="dtKariUf3">
				<input type='text' name="kariUf3Name" class='input-large' disabled value='${su:htmlEscape(kariUf3Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kashiUf3Cd1" maxlength='20' value='${su:htmlEscape(kashiUf3Cd1)}' autocomplete="on" list="dtKashi1Uf3">
				<input type='hidden' name="kashiUf3Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kashiUf3SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf3Cd1" class='input-small' value='${su:htmlEscape(kashiUf3Cd1)}' autocomplete="on" list="dtKashi1Uf3">
				<input type='text' name="kashiUf3Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf3Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kashiUf3Cd2" maxlength='20' value='${su:htmlEscape(kashiUf3Cd2)}' autocomplete="on" list="dtKashi2Uf3">
				<input type='hidden' name="kashiUf3Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kashiUf3SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf3Cd2" class='input-small' value='${su:htmlEscape(kashiUf3Cd2)}' autocomplete="on" list="dtKashi2Uf3">
				<input type='text' name="kashiUf3Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf3Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kashiUf3Cd3" maxlength='20' value='${su:htmlEscape(kashiUf3Cd3)}' autocomplete="on" list="dtKashi3Uf3">
				<input type='hidden' name="kashiUf3Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kashiUf3SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf3Cd3" class='input-small' value='${su:htmlEscape(kashiUf3Cd3)}' autocomplete="on" list="dtKashi3Uf3">
				<input type='text' name="kashiUf3Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf3Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kashiUf3Cd4" maxlength='20' value='${su:htmlEscape(kashiUf3Cd4)}' autocomplete="on" list="dtKashi4Uf3">
				<input type='hidden' name="kashiUf3Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kashiUf3SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf3Cd4" class='input-small' value='${su:htmlEscape(kashiUf3Cd4)}' autocomplete="on" list="dtKashi4Uf3">
				<input type='text' name="kashiUf3Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf3Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf3}'>
<c:if test='${"0" eq hfUfSeigyo.uf3ShiyouFlg or "1" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<input type='text' name="kashiUf3Cd5" maxlength='20' value='${su:htmlEscape(kashiUf3Cd5)}' autocomplete="on" list="dtKashi5Uf3">
				<input type='hidden' name="kashiUf3Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf3ShiyouFlg or "3" eq hfUfSeigyo.uf3ShiyouFlg}'>
				<button type='button' id='kashiUf3SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf3Cd5" class='input-small' value='${su:htmlEscape(kashiUf3Cd5)}' autocomplete="on" list="dtKashi5Uf3">
				<input type='text' name="kashiUf3Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf3Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf4" <c:if test='${not ("0" ne hfUfSeigyo.uf4ShiyouFlg and ks.uf4.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf4Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kariUf4Cd" maxlength='20' value='${su:htmlEscape(kariUf4Cd)}' autocomplete="on" list="dtKariUf4">
				<input type='hidden' name="kariUf4Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kariUf4SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf4Cd" class='input-small' value='${su:htmlEscape(kariUf4Cd)}' autocomplete="on" list="dtKariUf4">
				<input type='text' name="kariUf4Name" class='input-large' disabled value='${su:htmlEscape(kariUf4Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kashiUf4Cd1" maxlength='20' value='${su:htmlEscape(kashiUf4Cd1)}' autocomplete="on" list="dtKashi1Uf4">
				<input type='hidden' name="kashiUf4Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kashiUf4SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf4Cd1" class='input-small' value='${su:htmlEscape(kashiUf4Cd1)}' autocomplete="on" list="dtKashi1Uf4">
				<input type='text' name="kashiUf4Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf4Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kashiUf4Cd2" maxlength='20' value='${su:htmlEscape(kashiUf4Cd2)}' autocomplete="on" list="dtKashi2Uf4">
				<input type='hidden' name="kashiUf4Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kashiUf4SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf4Cd2" class='input-small' value='${su:htmlEscape(kashiUf4Cd2)}' autocomplete="on" list="dtKashi2Uf4">
				<input type='text' name="kashiUf4Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf4Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kashiUf4Cd3" maxlength='20' value='${su:htmlEscape(kashiUf4Cd3)}' autocomplete="on" list="dtKashi3Uf4">
				<input type='hidden' name="kashiUf4Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kashiUf4SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf4Cd3" class='input-small' value='${su:htmlEscape(kashiUf4Cd3)}' autocomplete="on" list="dtKashi3Uf4">
				<input type='text' name="kashiUf4Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf4Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kashiUf4Cd4" maxlength='20' value='${su:htmlEscape(kashiUf4Cd4)}' autocomplete="on" list="dtKashi4Uf4">
				<input type='hidden' name="kashiUf4Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kashiUf4SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf4Cd4" class='input-small' value='${su:htmlEscape(kashiUf4Cd4)}' autocomplete="on" list="dtKashi4Uf4">
				<input type='text' name="kashiUf4Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf4Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf4}'>
<c:if test='${"0" eq hfUfSeigyo.uf4ShiyouFlg or "1" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<input type='text' name="kashiUf4Cd5" maxlength='20' value='${su:htmlEscape(kashiUf4Cd5)}' autocomplete="on" list="dtKashi5Uf4">
				<input type='hidden' name="kashiUf4Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf4ShiyouFlg or "3" eq hfUfSeigyo.uf4ShiyouFlg}'>
				<button type='button' id='kashiUf4SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf4Cd5" class='input-small' value='${su:htmlEscape(kashiUf4Cd5)}' autocomplete="on" list="dtKashi5Uf4">
				<input type='text' name="kashiUf4Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf4Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf5" <c:if test='${not ("0" ne hfUfSeigyo.uf5ShiyouFlg and ks.uf5.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf5Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kariUf5Cd" maxlength='20' value='${su:htmlEscape(kariUf5Cd)}' autocomplete="on" list="dtKariUf5">
				<input type='hidden' name="kariUf5Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kariUf5SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf5Cd" class='input-small' value='${su:htmlEscape(kariUf5Cd)}' autocomplete="on" list="dtKariUf5">
				<input type='text' name="kariUf5Name" class='input-large' disabled value='${su:htmlEscape(kariUf5Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kashiUf5Cd1" maxlength='20' value='${su:htmlEscape(kashiUf5Cd1)}' autocomplete="on" list="dtKashi1Uf5">
				<input type='hidden' name="kashiUf5Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kashiUf5SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf5Cd1" class='input-small' value='${su:htmlEscape(kashiUf5Cd1)}' autocomplete="on" list="dtKashi1Uf5">
				<input type='text' name="kashiUf5Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf5Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kashiUf5Cd2" maxlength='20' value='${su:htmlEscape(kashiUf5Cd2)}' autocomplete="on" list="dtKashi2Uf5">
				<input type='hidden' name="kashiUf5Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kashiUf5SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf5Cd2" class='input-small' value='${su:htmlEscape(kashiUf5Cd2)}' autocomplete="on" list="dtKashi2Uf5">
				<input type='text' name="kashiUf5Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf5Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kashiUf5Cd3" maxlength='20' value='${su:htmlEscape(kashiUf5Cd3)}' autocomplete="on" list="dtKashi3Uf5">
				<input type='hidden' name="kashiUf5Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kashiUf5SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf5Cd3" class='input-small' value='${su:htmlEscape(kashiUf5Cd3)}' autocomplete="on" list="dtKashi3Uf5">
				<input type='text' name="kashiUf5Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf5Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kashiUf5Cd4" maxlength='20' value='${su:htmlEscape(kashiUf5Cd4)}' autocomplete="on" list="dtKashi4Uf5">
				<input type='hidden' name="kashiUf5Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kashiUf5SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf5Cd4" class='input-small' value='${su:htmlEscape(kashiUf5Cd4)}' autocomplete="on" list="dtKashi4Uf5">
				<input type='text' name="kashiUf5Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf5Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf5}'>
<c:if test='${"0" eq hfUfSeigyo.uf5ShiyouFlg or "1" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<input type='text' name="kashiUf5Cd5" maxlength='20' value='${su:htmlEscape(kashiUf5Cd5)}' autocomplete="on" list="dtKashi5Uf5">
				<input type='hidden' name="kashiUf5Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf5ShiyouFlg or "3" eq hfUfSeigyo.uf5ShiyouFlg}'>
				<button type='button' id='kashiUf5SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf5Cd5" class='input-small' value='${su:htmlEscape(kashiUf5Cd5)}' autocomplete="on" list="dtKashi5Uf5">
				<input type='text' name="kashiUf5Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf5Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf6" <c:if test='${not ("0" ne hfUfSeigyo.uf6ShiyouFlg and ks.uf6.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf6Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kariUf6Cd" maxlength='20' value='${su:htmlEscape(kariUf6Cd)}' autocomplete="on" list="dtKariUf6">
				<input type='hidden' name="kariUf6Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kariUf6SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf6Cd" class='input-small' value='${su:htmlEscape(kariUf6Cd)}' autocomplete="on" list="dtKariUf6">
				<input type='text' name="kariUf6Name" class='input-large' disabled value='${su:htmlEscape(kariUf6Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kashiUf6Cd1" maxlength='20' value='${su:htmlEscape(kashiUf6Cd1)}' autocomplete="on" list="dtKashi1Uf6">
				<input type='hidden' name="kashiUf6Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kashiUf6SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf6Cd1" class='input-small' value='${su:htmlEscape(kashiUf6Cd1)}' autocomplete="on" list="dtKashi1Uf6">
				<input type='text' name="kashiUf6Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf6Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kashiUf6Cd2" maxlength='20' value='${su:htmlEscape(kashiUf6Cd2)}' autocomplete="on" list="dtKashi2Uf6">
				<input type='hidden' name="kashiUf6Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kashiUf6SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf6Cd2" class='input-small' value='${su:htmlEscape(kashiUf6Cd2)}' autocomplete="on" list="dtKashi2Uf6">
				<input type='text' name="kashiUf6Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf6Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kashiUf6Cd3" maxlength='20' value='${su:htmlEscape(kashiUf6Cd3)}' autocomplete="on" list="dtKashi3Uf6">
				<input type='hidden' name="kashiUf6Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kashiUf6SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf6Cd3" class='input-small' value='${su:htmlEscape(kashiUf6Cd3)}' autocomplete="on" list="dtKashi3Uf6">
				<input type='text' name="kashiUf6Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf6Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kashiUf6Cd4" maxlength='20' value='${su:htmlEscape(kashiUf6Cd4)}' autocomplete="on" list="dtKashi4Uf6">
				<input type='hidden' name="kashiUf6Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kashiUf6SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf6Cd4" class='input-small' value='${su:htmlEscape(kashiUf6Cd4)}' autocomplete="on" list="dtKashi4Uf6">
				<input type='text' name="kashiUf6Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf6Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf6}'>
<c:if test='${"0" eq hfUfSeigyo.uf6ShiyouFlg or "1" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<input type='text' name="kashiUf6Cd5" maxlength='20' value='${su:htmlEscape(kashiUf6Cd5)}' autocomplete="on" list="dtKashi5Uf6">
				<input type='hidden' name="kashiUf6Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf6ShiyouFlg or "3" eq hfUfSeigyo.uf6ShiyouFlg}'>
				<button type='button' id='kashiUf6SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf6Cd5" class='input-small' value='${su:htmlEscape(kashiUf6Cd5)}' autocomplete="on" list="dtKashi5Uf6">
				<input type='text' name="kashiUf6Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf6Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf7" <c:if test='${not ("0" ne hfUfSeigyo.uf7ShiyouFlg and ks.uf7.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf7Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kariUf7Cd" maxlength='20' value='${su:htmlEscape(kariUf7Cd)}' autocomplete="on" list="dtKariUf7">
				<input type='hidden' name="kariUf7Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kariUf7SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf7Cd" class='input-small' value='${su:htmlEscape(kariUf7Cd)}' autocomplete="on" list="dtKariUf7">
				<input type='text' name="kariUf7Name" class='input-large' disabled value='${su:htmlEscape(kariUf7Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kashiUf7Cd1" maxlength='20' value='${su:htmlEscape(kashiUf7Cd1)}' autocomplete="on" list="dtKashi1Uf7">
				<input type='hidden' name="kashiUf7Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kashiUf7SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf7Cd1" class='input-small' value='${su:htmlEscape(kashiUf7Cd1)}' autocomplete="on" list="dtKashi1Uf7">
				<input type='text' name="kashiUf7Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf7Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kashiUf7Cd2" maxlength='20' value='${su:htmlEscape(kashiUf7Cd2)}' autocomplete="on" list="dtKashi2Uf7">
				<input type='hidden' name="kashiUf7Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kashiUf7SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf7Cd2" class='input-small' value='${su:htmlEscape(kashiUf7Cd2)}' autocomplete="on" list="dtKashi2Uf7">
				<input type='text' name="kashiUf7Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf7Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kashiUf7Cd3" maxlength='20' value='${su:htmlEscape(kashiUf7Cd3)}' autocomplete="on" list="dtKashi3Uf7">
				<input type='hidden' name="kashiUf7Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kashiUf7SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf7Cd3" class='input-small' value='${su:htmlEscape(kashiUf7Cd3)}' autocomplete="on" list="dtKashi3Uf7">
				<input type='text' name="kashiUf7Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf7Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kashiUf7Cd4" maxlength='20' value='${su:htmlEscape(kashiUf7Cd4)}' autocomplete="on" list="dtKashi4Uf7">
				<input type='hidden' name="kashiUf7Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kashiUf7SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf7Cd4" class='input-small' value='${su:htmlEscape(kashiUf7Cd4)}' autocomplete="on" list="dtKashi4Uf7">
				<input type='text' name="kashiUf7Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf7Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf7}'>
<c:if test='${"0" eq hfUfSeigyo.uf7ShiyouFlg or "1" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<input type='text' name="kashiUf7Cd5" maxlength='20' value='${su:htmlEscape(kashiUf7Cd5)}' autocomplete="on" list="dtKashi5Uf7">
				<input type='hidden' name="kashiUf7Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf7ShiyouFlg or "3" eq hfUfSeigyo.uf7ShiyouFlg}'>
				<button type='button' id='kashiUf7SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf7Cd5" class='input-small' value='${su:htmlEscape(kashiUf7Cd5)}' autocomplete="on" list="dtKashi5Uf7">
				<input type='text' name="kashiUf7Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf7Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf8" <c:if test='${not ("0" ne hfUfSeigyo.uf8ShiyouFlg and ks.uf8.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf8Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kariUf8Cd" maxlength='20' value='${su:htmlEscape(kariUf8Cd)}' autocomplete="on" list="dtKariUf8">
				<input type='hidden' name="kariUf8Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kariUf8SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf8Cd" class='input-small' value='${su:htmlEscape(kariUf8Cd)}' autocomplete="on" list="dtKariUf8">
				<input type='text' name="kariUf8Name" class='input-large' disabled value='${su:htmlEscape(kariUf8Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kashiUf8Cd1" maxlength='20' value='${su:htmlEscape(kashiUf8Cd1)}' autocomplete="on" list="dtKashi1Uf8">
				<input type='hidden' name="kashiUf8Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kashiUf8SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf8Cd1" class='input-small' value='${su:htmlEscape(kashiUf8Cd1)}' autocomplete="on" list="dtKashi1Uf8">
				<input type='text' name="kashiUf8Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf8Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kashiUf8Cd2" maxlength='20' value='${su:htmlEscape(kashiUf8Cd2)}' autocomplete="on" list="dtKashi2Uf8">
				<input type='hidden' name="kashiUf8Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kashiUf8SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf8Cd2" class='input-small' value='${su:htmlEscape(kashiUf8Cd2)}' autocomplete="on" list="dtKashi2Uf8">
				<input type='text' name="kashiUf8Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf8Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kashiUf8Cd3" maxlength='20' value='${su:htmlEscape(kashiUf8Cd3)}' autocomplete="on" list="dtKashi3Uf8">
				<input type='hidden' name="kashiUf8Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kashiUf8SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf8Cd3" class='input-small' value='${su:htmlEscape(kashiUf8Cd3)}' autocomplete="on" list="dtKashi3Uf8">
				<input type='text' name="kashiUf8Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf8Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kashiUf8Cd4" maxlength='20' value='${su:htmlEscape(kashiUf8Cd4)}' autocomplete="on" list="dtKashi4Uf8">
				<input type='hidden' name="kashiUf8Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kashiUf8SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf8Cd4" class='input-small' value='${su:htmlEscape(kashiUf8Cd4)}' autocomplete="on" list="dtKashi4Uf8">
				<input type='text' name="kashiUf8Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf8Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf8}'>
<c:if test='${"0" eq hfUfSeigyo.uf8ShiyouFlg or "1" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<input type='text' name="kashiUf8Cd5" maxlength='20' value='${su:htmlEscape(kashiUf8Cd5)}' autocomplete="on" list="dtKashi5Uf8">
				<input type='hidden' name="kashiUf8Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf8ShiyouFlg or "3" eq hfUfSeigyo.uf8ShiyouFlg}'>
				<button type='button' id='kashiUf8SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf8Cd5" class='input-small' value='${su:htmlEscape(kashiUf8Cd5)}' autocomplete="on" list="dtKashi5Uf8">
				<input type='text' name="kashiUf8Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf8Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf9" <c:if test='${not ("0" ne hfUfSeigyo.uf9ShiyouFlg and ks.uf9.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf9Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kariUf9Cd" maxlength='20' value='${su:htmlEscape(kariUf9Cd)}' autocomplete="on" list="dtKariUf9">
				<input type='hidden' name="kariUf9Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kariUf9SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf9Cd" class='input-small' value='${su:htmlEscape(kariUf9Cd)}' autocomplete="on" list="dtKariUf9">
				<input type='text' name="kariUf9Name" class='input-large' disabled value='${su:htmlEscape(kariUf9Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kashiUf9Cd1" maxlength='20' value='${su:htmlEscape(kashiUf9Cd1)}' autocomplete="on" list="dtKashi1Uf9">
				<input type='hidden' name="kashiUf9Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kashiUf9SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf9Cd1" class='input-small' value='${su:htmlEscape(kashiUf9Cd1)}' autocomplete="on" list="dtKashi1Uf9">
				<input type='text' name="kashiUf9Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf9Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kashiUf9Cd2" maxlength='20' value='${su:htmlEscape(kashiUf9Cd2)}' autocomplete="on" list="dtKashi2Uf9">
				<input type='hidden' name="kashiUf9Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kashiUf9SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf9Cd2" class='input-small' value='${su:htmlEscape(kashiUf9Cd2)}' autocomplete="on" list="dtKashi2Uf9">
				<input type='text' name="kashiUf9Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf9Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kashiUf9Cd3" maxlength='20' value='${su:htmlEscape(kashiUf9Cd3)}' autocomplete="on" list="dtKashi3Uf9">
				<input type='hidden' name="kashiUf9Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kashiUf9SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf9Cd3" class='input-small' value='${su:htmlEscape(kashiUf9Cd3)}' autocomplete="on" list="dtKashi3Uf9">
				<input type='text' name="kashiUf9Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf9Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kashiUf9Cd4" maxlength='20' value='${su:htmlEscape(kashiUf9Cd4)}' autocomplete="on" list="dtKashi4Uf9">
				<input type='hidden' name="kashiUf9Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kashiUf9SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf9Cd4" class='input-small' value='${su:htmlEscape(kashiUf9Cd4)}' autocomplete="on" list="dtKashi4Uf9">
				<input type='text' name="kashiUf9Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf9Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf9}'>
<c:if test='${"0" eq hfUfSeigyo.uf9ShiyouFlg or "1" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<input type='text' name="kashiUf9Cd5" maxlength='20' value='${su:htmlEscape(kashiUf9Cd5)}' autocomplete="on" list="dtKashi5Uf9">
				<input type='hidden' name="kashiUf9Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf9ShiyouFlg or "3" eq hfUfSeigyo.uf9ShiyouFlg}'>
				<button type='button' id='kashiUf9SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf9Cd5" class='input-small' value='${su:htmlEscape(kashiUf9Cd5)}' autocomplete="on" list="dtKashi5Uf9">
				<input type='text' name="kashiUf9Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf9Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="uf10" <c:if test='${not ("0" ne hfUfSeigyo.uf10ShiyouFlg and ks.uf10.hyoujiFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.uf10Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kariUf10Cd" maxlength='20' value='${su:htmlEscape(kariUf10Cd)}' autocomplete="on" list="dtKariUf10">
				<input type='hidden' name="kariUf10Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kariUf10SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUf10Cd" class='input-small' value='${su:htmlEscape(kariUf10Cd)}' autocomplete="on" list="dtKariUf10">
				<input type='text' name="kariUf10Name" class='input-large' disabled value='${su:htmlEscape(kariUf10Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1Uf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kashiUf10Cd1" maxlength='20' value='${su:htmlEscape(kashiUf10Cd1)}' autocomplete="on" list="dtKashi1Uf10">
				<input type='hidden' name="kashiUf10Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kashiUf10SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf10Cd1" class='input-small' value='${su:htmlEscape(kashiUf10Cd1)}' autocomplete="on" list="dtKashi1Uf10">
				<input type='text' name="kashiUf10Name1" class='input-large' disabled value='${su:htmlEscape(kashiUf10Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2Uf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kashiUf10Cd2" maxlength='20' value='${su:htmlEscape(kashiUf10Cd2)}' autocomplete="on" list="dtKashi2Uf10">
				<input type='hidden' name="kashiUf10Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kashiUf10SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf10Cd2" class='input-small' value='${su:htmlEscape(kashiUf10Cd2)}' autocomplete="on" list="dtKashi2Uf10">
				<input type='text' name="kashiUf10Name2" class='input-large' disabled value='${su:htmlEscape(kashiUf10Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3Uf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kashiUf10Cd3" maxlength='20' value='${su:htmlEscape(kashiUf10Cd3)}' autocomplete="on" list="dtKashi3Uf10">
				<input type='hidden' name="kashiUf10Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kashiUf10SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf10Cd3" class='input-small' value='${su:htmlEscape(kashiUf10Cd3)}' autocomplete="on" list="dtKashi3Uf10">
				<input type='text' name="kashiUf10Name3" class='input-large' disabled value='${su:htmlEscape(kashiUf10Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4Uf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kashiUf10Cd4" maxlength='20' value='${su:htmlEscape(kashiUf10Cd4)}' autocomplete="on" list="dtKashi4Uf10">
				<input type='hidden' name="kashiUf10Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kashiUf10SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf10Cd4" class='input-small' value='${su:htmlEscape(kashiUf10Cd4)}' autocomplete="on" list="dtKashi4Uf10">
				<input type='text' name="kashiUf10Name4" class='input-large' disabled value='${su:htmlEscape(kashiUf10Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5Uf10}'>
<c:if test='${"0" eq hfUfSeigyo.uf10ShiyouFlg or "1" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<input type='text' name="kashiUf10Cd5" maxlength='20' value='${su:htmlEscape(kashiUf10Cd5)}' autocomplete="on" list="dtKashi5Uf10">
				<input type='hidden' name="kashiUf10Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.uf10ShiyouFlg or "3" eq hfUfSeigyo.uf10ShiyouFlg}'>
				<button type='button' id='kashiUf10SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUf10Cd5" class='input-small' value='${su:htmlEscape(kashiUf10Cd5)}' autocomplete="on" list="dtKashi5Uf10">
				<input type='text' name="kashiUf10Name5" class='input-large' disabled value='${su:htmlEscape(kashiUf10Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei1" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei1ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei1Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kariUfKotei1Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei1Cd)}' autocomplete="on" list="dtKariUfKotei1">
				<input type='hidden' name="kariUfKotei1Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kariUfKotei1SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei1Cd" class='input-small' value='${su:htmlEscape(kariUfKotei1Cd)}' autocomplete="on" list="dtKariUfKotei1">
				<input type='text' name="kariUfKotei1Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei1Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kashiUfKotei1Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei1Cd1)}' autocomplete="on" list="dtKashi1UfKotei1">
				<input type='hidden' name="kashiUfKotei1Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kashiUfKotei1SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei1Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei1Cd1)}' autocomplete="on" list="dtKashi1UfKotei1">
				<input type='text' name="kashiUfKotei1Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei1Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kashiUfKotei1Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei1Cd2)}' autocomplete="on" list="dtKashi2UfKotei1">
				<input type='hidden' name="kashiUfKotei1Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kashiUfKotei1SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei1Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei1Cd2)}' autocomplete="on" list="dtKashi2UfKotei1">
				<input type='text' name="kashiUfKotei1Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei1Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kashiUfKotei1Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei1Cd3)}' autocomplete="on" list="dtKashi3UfKotei1">
				<input type='hidden' name="kashiUfKotei1Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kashiUfKotei1SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei1Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei1Cd3)}' autocomplete="on" list="dtKashi3UfKotei1">
				<input type='text' name="kashiUfKotei1Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei1Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kashiUfKotei1Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei1Cd4)}' autocomplete="on" list="dtKashi4UfKotei1">
				<input type='hidden' name="kashiUfKotei1Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kashiUfKotei1SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei1Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei1Cd4)}' autocomplete="on" list="dtKashi4UfKotei1">
				<input type='text' name="kashiUfKotei1Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei1Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei1}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei1ShiyouFlg or "1" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<input type='text' name="kashiUfKotei1Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei1Cd5)}' autocomplete="on" list="dtKashi5UfKotei1">
				<input type='hidden' name="kashiUfKotei1Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei1ShiyouFlg or "3" eq hfUfSeigyo.ufKotei1ShiyouFlg}'>
				<button type='button' id='kashiUfKotei1SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei1Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei1Cd5)}' autocomplete="on" list="dtKashi5UfKotei1">
				<input type='text' name="kashiUfKotei1Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei1Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei2" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei2ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei2Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kariUfKotei2Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei2Cd)}' autocomplete="on" list="dtKariUfKotei2">
				<input type='hidden' name="kariUfKotei2Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kariUfKotei2SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei2Cd" class='input-small' value='${su:htmlEscape(kariUfKotei2Cd)}' autocomplete="on" list="dtKariUfKotei2">
				<input type='text' name="kariUfKotei2Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei2Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kashiUfKotei2Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei2Cd1)}' autocomplete="on" list="dtKashi1UfKotei2">
				<input type='hidden' name="kashiUfKotei2Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kashiUfKotei2SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei2Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei2Cd1)}' autocomplete="on" list="dtKashi1UfKotei2">
				<input type='text' name="kashiUfKotei2Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei2Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kashiUfKotei2Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei2Cd2)}' autocomplete="on" list="dtKashi2UfKotei2">
				<input type='hidden' name="kashiUfKotei2Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kashiUfKotei2SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei2Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei2Cd2)}' autocomplete="on" list="dtKashi2UfKotei2">
				<input type='text' name="kashiUfKotei2Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei2Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kashiUfKotei2Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei2Cd3)}' autocomplete="on" list="dtKashi3UfKotei2">
				<input type='hidden' name="kashiUfKotei2Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kashiUfKotei2SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei2Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei2Cd3)}' autocomplete="on" list="dtKashi3UfKotei2">
				<input type='text' name="kashiUfKotei2Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei2Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kashiUfKotei2Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei2Cd4)}' autocomplete="on" list="dtKashi4UfKotei2">
				<input type='hidden' name="kashiUfKotei2Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kashiUfKotei2SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei2Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei2Cd4)}' autocomplete="on" list="dtKashi4UfKotei2">
				<input type='text' name="kashiUfKotei2Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei2Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei2}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei2ShiyouFlg or "1" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<input type='text' name="kashiUfKotei2Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei2Cd5)}' autocomplete="on" list="dtKashi5UfKotei2">
				<input type='hidden' name="kashiUfKotei2Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei2ShiyouFlg or "3" eq hfUfSeigyo.ufKotei2ShiyouFlg}'>
				<button type='button' id='kashiUfKotei2SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei2Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei2Cd5)}' autocomplete="on" list="dtKashi5UfKotei1">
				<input type='text' name="kashiUfKotei2Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei2Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei3" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei3ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei3Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kariUfKotei3Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei3Cd)}' autocomplete="on" list="dtKariUfKotei3">
				<input type='hidden' name="kariUfKotei3Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kariUfKotei3SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei3Cd" class='input-small' value='${su:htmlEscape(kariUfKotei3Cd)}' autocomplete="on" list="dtKariUfKotei3">
				<input type='text' name="kariUfKotei3Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei3Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kashiUfKotei3Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei3Cd1)}' autocomplete="on" list="dtKashi1UfKotei3">
				<input type='hidden' name="kashiUfKotei3Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kashiUfKotei3SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei3Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei3Cd1)}' autocomplete="on" list="dtKashi1UfKotei3">
				<input type='text' name="kashiUfKotei3Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei3Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kashiUfKotei3Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei3Cd2)}' autocomplete="on" list="dtKashi2UfKotei3">
				<input type='hidden' name="kashiUfKotei3Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kashiUfKotei3SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei3Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei3Cd2)}' autocomplete="on" list="dtKashi2UfKotei3">
				<input type='text' name="kashiUfKotei3Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei3Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kashiUfKotei3Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei3Cd3)}' autocomplete="on" list="dtKashi3UfKotei3">
				<input type='hidden' name="kashiUfKotei3Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kashiUfKotei3SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei3Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei3Cd3)}' autocomplete="on" list="dtKashi3UfKotei3">
				<input type='text' name="kashiUfKotei3Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei3Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kashiUfKotei3Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei3Cd4)}' autocomplete="on" list="dtKashi4UfKotei3">
				<input type='hidden' name="kashiUfKotei3Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kashiUfKotei3SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei3Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei3Cd4)}' autocomplete="on" list="dtKashi4UfKotei3">
				<input type='text' name="kashiUfKotei3Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei3Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei3}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei3ShiyouFlg or "1" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<input type='text' name="kashiUfKotei3Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei3Cd5)}' autocomplete="on" list="dtKashi5UfKotei3">
				<input type='hidden' name="kashiUfKotei3Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei3ShiyouFlg or "3" eq hfUfSeigyo.ufKotei3ShiyouFlg}'>
				<button type='button' id='kashiUfKotei3SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei3Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei3Cd5)}' autocomplete="on" list="dtKashi5UfKotei3">
				<input type='text' name="kashiUfKotei3Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei3Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei4" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei4ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei4Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kariUfKotei4Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei4Cd)}' autocomplete="on" list="dtKariUfKotei4">
				<input type='hidden' name="kariUfKotei4Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kariUfKotei4SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei4Cd" class='input-small' value='${su:htmlEscape(kariUfKotei4Cd)}' autocomplete="on" list="dtKariUfKotei4">
				<input type='text' name="kariUfKotei4Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei4Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kashiUfKotei4Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei4Cd1)}' autocomplete="on" list="dtKashi1UfKotei4">
				<input type='hidden' name="kashiUfKotei4Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kashiUfKotei4SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei4Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei4Cd1)}' autocomplete="on" list="dtKashi1UfKotei4">
				<input type='text' name="kashiUfKotei4Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei4Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kashiUfKotei4Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei4Cd2)}' autocomplete="on" list="dtKashi2UfKotei4">
				<input type='hidden' name="kashiUfKotei4Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kashiUfKotei4SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei4Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei4Cd2)}' autocomplete="on" list="dtKashi2UfKotei4">
				<input type='text' name="kashiUfKotei4Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei4Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kashiUfKotei4Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei4Cd3)}' autocomplete="on" list="dtKashi3UfKotei4">
				<input type='hidden' name="kashiUfKotei4Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kashiUfKotei4SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei4Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei4Cd3)}' autocomplete="on" list="dtKashi3UfKotei4">
				<input type='text' name="kashiUfKotei4Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei4Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kashiUfKotei4Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei4Cd4)}' autocomplete="on" list="dtKashi4UfKotei4">
				<input type='hidden' name="kashiUfKotei4Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kashiUfKotei4SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei4Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei4Cd4)}' autocomplete="on" list="dtKashi4UfKotei4">
				<input type='text' name="kashiUfKotei4Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei4Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei4}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei4ShiyouFlg or "1" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<input type='text' name="kashiUfKotei4Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei4Cd5)}' autocomplete="on" list="dtKashi5UfKotei4">
				<input type='hidden' name="kashiUfKotei4Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei4ShiyouFlg or "3" eq hfUfSeigyo.ufKotei4ShiyouFlg}'>
				<button type='button' id='kashiUfKotei4SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei4Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei4Cd5)}' autocomplete="on" list="dtKashi5UfKotei4">
				<input type='text' name="kashiUfKotei4Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei4Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei5" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei5ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei5Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kariUfKotei5Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei5Cd)}' autocomplete="on" list="dtKariUfKotei5">
				<input type='hidden' name="kariUfKotei5Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kariUfKotei5SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei5Cd" class='input-small' value='${su:htmlEscape(kariUfKotei5Cd)}' autocomplete="on" list="dtKariUfKotei5">
				<input type='text' name="kariUfKotei5Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei5Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kashiUfKotei5Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei5Cd1)}' autocomplete="on" list="dtKashi1UfKotei5">
				<input type='hidden' name="kashiUfKotei5Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kashiUfKotei5SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei5Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei5Cd1)}' autocomplete="on" list="dtKashi1UfKotei5">
				<input type='text' name="kashiUfKotei5Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei5Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kashiUfKotei5Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei5Cd2)}' autocomplete="on" list="dtKashi2UfKotei5">
				<input type='hidden' name="kashiUfKotei5Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kashiUfKotei5SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei5Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei5Cd2)}' autocomplete="on" list="dtKashi2UfKotei5">
				<input type='text' name="kashiUfKotei5Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei5Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kashiUfKotei5Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei5Cd3)}' autocomplete="on" list="dtKashi3UfKotei5">
				<input type='hidden' name="kashiUfKotei5Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kashiUfKotei5SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei5Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei5Cd3)}' autocomplete="on" list="dtKashi3UfKotei5">
				<input type='text' name="kashiUfKotei5Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei5Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kashiUfKotei5Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei5Cd4)}' autocomplete="on" list="dtKashi4UfKotei5">
				<input type='hidden' name="kashiUfKotei5Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kashiUfKotei5SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei5Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei5Cd4)}' autocomplete="on" list="dtKashi4UfKotei5">
				<input type='text' name="kashiUfKotei5Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei5Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei5}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei5ShiyouFlg or "1" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<input type='text' name="kashiUfKotei5Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei5Cd5)}' autocomplete="on" list="dtKashi5UfKotei5">
				<input type='hidden' name="kashiUfKotei5Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei5ShiyouFlg or "3" eq hfUfSeigyo.ufKotei5ShiyouFlg}'>
				<button type='button' id='kashiUfKotei5SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei5Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei5Cd5)}' autocomplete="on" list="dtKashi5UfKotei5">
				<input type='text' name="kashiUfKotei5Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei5Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei6" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei6ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei6Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kariUfKotei6Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei6Cd)}' autocomplete="on" list="dtKariUfKotei6">
				<input type='hidden' name="kariUfKotei6Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kariUfKotei6SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei6Cd" class='input-small' value='${su:htmlEscape(kariUfKotei6Cd)}' autocomplete="on" list="dtKariUfKotei6">
				<input type='text' name="kariUfKotei6Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei6Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kashiUfKotei6Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei6Cd1)}' autocomplete="on" list="dtKashi1UfKotei6">
				<input type='hidden' name="kashiUfKotei6Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kashiUfKotei6SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei6Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei6Cd1)}' autocomplete="on" list="dtKashi1UfKotei6">
				<input type='text' name="kashiUfKotei6Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei6Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kashiUfKotei6Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei6Cd2)}' autocomplete="on" list="dtKashi2UfKotei6">
				<input type='hidden' name="kashiUfKotei6Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kashiUfKotei6SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei6Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei6Cd2)}' autocomplete="on" list="dtKashi2UfKotei6">
				<input type='text' name="kashiUfKotei6Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei6Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kashiUfKotei6Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei6Cd3)}' autocomplete="on" list="dtKashi3UfKotei6">
				<input type='hidden' name="kashiUfKotei6Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kashiUfKotei6SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei6Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei6Cd3)}' autocomplete="on" list="dtKashi3UfKotei6">
				<input type='text' name="kashiUfKotei6Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei6Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kashiUfKotei6Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei6Cd4)}' autocomplete="on" list="dtKashi4UfKotei6">
				<input type='hidden' name="kashiUfKotei6Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kashiUfKotei6SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei6Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei6Cd4)}' autocomplete="on" list="dtKashi4UfKotei6">
				<input type='text' name="kashiUfKotei6Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei6Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei6}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei6ShiyouFlg or "1" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<input type='text' name="kashiUfKotei6Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei6Cd5)}' autocomplete="on" list="dtKashi5UfKotei6">
				<input type='hidden' name="kashiUfKotei6Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei6ShiyouFlg or "3" eq hfUfSeigyo.ufKotei6ShiyouFlg}'>
				<button type='button' id='kashiUfKotei6SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei6Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei6Cd5)}' autocomplete="on" list="dtKashi5UfKotei6">
				<input type='text' name="kashiUfKotei6Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei6Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei7" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei7ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei7Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kariUfKotei7Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei7Cd)}' autocomplete="on" list="dtKariUfKotei7">
				<input type='hidden' name="kariUfKotei7Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kariUfKotei7SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei7Cd" class='input-small' value='${su:htmlEscape(kariUfKotei7Cd)}' autocomplete="on" list="dtKariUfKotei7">
				<input type='text' name="kariUfKotei7Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei7Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kashiUfKotei7Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei7Cd1)}' autocomplete="on" list="dtKashi1UfKotei7">
				<input type='hidden' name="kashiUfKotei7Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kashiUfKotei7SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei7Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei7Cd1)}' autocomplete="on" list="dtKashi1UfKotei7">
				<input type='text' name="kashiUfKotei7Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei7Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kashiUfKotei7Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei7Cd2)}' autocomplete="on" list="dtKashi2UfKotei7">
				<input type='hidden' name="kashiUfKotei7Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kashiUfKotei7SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei7Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei7Cd2)}' autocomplete="on" list="dtKashi2UfKotei7">
				<input type='text' name="kashiUfKotei7Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei7Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kashiUfKotei7Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei7Cd3)}' autocomplete="on" list="dtKashi3UfKotei7">
				<input type='hidden' name="kashiUfKotei7Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kashiUfKotei7SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei7Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei7Cd3)}' autocomplete="on" list="dtKashi3UfKotei7">
				<input type='text' name="kashiUfKotei7Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei7Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kashiUfKotei7Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei7Cd4)}' autocomplete="on" list="dtKashi4UfKotei7">
				<input type='hidden' name="kashiUfKotei7Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kashiUfKotei7SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei7Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei7Cd4)}' autocomplete="on" list="dtKashi4UfKotei7">
				<input type='text' name="kashiUfKotei7Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei7Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei7}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei7ShiyouFlg or "1" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<input type='text' name="kashiUfKotei7Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei7Cd5)}' autocomplete="on" list="dtKashi5UfKotei7">
				<input type='hidden' name="kashiUfKotei7Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei7ShiyouFlg or "3" eq hfUfSeigyo.ufKotei7ShiyouFlg}'>
				<button type='button' id='kashiUfKotei7SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei7Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei7Cd5)}' autocomplete="on" list="dtKashi5UfKotei7">
				<input type='text' name="kashiUfKotei7Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei7Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei8" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei8ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei8Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kariUfKotei8Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei8Cd)}' autocomplete="on" list="dtKariUfKotei8">
				<input type='hidden' name="kariUfKotei8Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kariUfKotei8SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei8Cd" class='input-small' value='${su:htmlEscape(kariUfKotei8Cd)}' autocomplete="on" list="dtKariUfKotei8">
				<input type='text' name="kariUfKotei8Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei8Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kashiUfKotei8Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei8Cd1)}' autocomplete="on" list="dtKashi1UfKotei8">
				<input type='hidden' name="kashiUfKotei8Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kashiUfKotei8SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei8Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei8Cd1)}' autocomplete="on" list="dtKashi1UfKotei8">
				<input type='text' name="kashiUfKotei8Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei8Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kashiUfKotei8Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei8Cd2)}' autocomplete="on" list="dtKashi2UfKotei8">
				<input type='hidden' name="kashiUfKotei8Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kashiUfKotei8SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei8Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei8Cd2)}' autocomplete="on" list="dtKashi2UfKotei8">
				<input type='text' name="kashiUfKotei8Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei8Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kashiUfKotei8Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei8Cd3)}' autocomplete="on" list="dtKashi3UfKotei8">
				<input type='hidden' name="kashiUfKotei8Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kashiUfKotei8SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei8Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei8Cd3)}' autocomplete="on" list="dtKashi3UfKotei8">
				<input type='text' name="kashiUfKotei8Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei8Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kashiUfKotei8Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei8Cd4)}' autocomplete="on" list="dtKashi4UfKotei8">
				<input type='hidden' name="kashiUfKotei8Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kashiUfKotei8SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei8Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei8Cd4)}' autocomplete="on" list="dtKashi4UfKotei8">
				<input type='text' name="kashiUfKotei8Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei8Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei8}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei8ShiyouFlg or "1" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<input type='text' name="kashiUfKotei8Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei8Cd5)}' autocomplete="on" list="dtKashi5UfKotei8">
				<input type='hidden' name="kashiUfKotei8Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei8ShiyouFlg or "3" eq hfUfSeigyo.ufKotei8ShiyouFlg}'>
				<button type='button' id='kashiUfKotei8SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei8Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei8Cd5)}' autocomplete="on" list="dtKashi5UfKotei8">
				<input type='text' name="kashiUfKotei8Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei8Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei9" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei9ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei9Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kariUfKotei9Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei9Cd)}' autocomplete="on" list="dtKariUfKotei9">
				<input type='hidden' name="kariUfKotei9Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kariUfKotei9SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei9Cd" class='input-small' value='${su:htmlEscape(kariUfKotei9Cd)}' autocomplete="on" list="dtKariUfKotei9">
				<input type='text' name="kariUfKotei9Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei9Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kashiUfKotei9Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei9Cd1)}' autocomplete="on" list="dtKashi1UfKotei9">
				<input type='hidden' name="kashiUfKotei9Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kashiUfKotei9SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei9Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei9Cd1)}' autocomplete="on" list="dtKashi1UfKotei9">
				<input type='text' name="kashiUfKotei9Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei9Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kashiUfKotei9Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei9Cd2)}' autocomplete="on" list="dtKashi2UfKotei9">
				<input type='hidden' name="kashiUfKotei9Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kashiUfKotei9SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei9Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei9Cd2)}' autocomplete="on" list="dtKashi2UfKotei9">
				<input type='text' name="kashiUfKotei9Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei9Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kashiUfKotei9Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei9Cd3)}' autocomplete="on" list="dtKashi3UfKotei9">
				<input type='hidden' name="kashiUfKotei9Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kashiUfKotei9SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei9Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei9Cd3)}' autocomplete="on" list="dtKashi3UfKotei9">
				<input type='text' name="kashiUfKotei9Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei9Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kashiUfKotei9Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei9Cd4)}' autocomplete="on" list="dtKashi4UfKotei9">
				<input type='hidden' name="kashiUfKotei9Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kashiUfKotei9SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei9Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei9Cd4)}' autocomplete="on" list="dtKashi4UfKotei9">
				<input type='text' name="kashiUfKotei9Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei9Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei9}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei9ShiyouFlg or "1" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<input type='text' name="kashiUfKotei9Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei9Cd5)}' autocomplete="on" list="dtKashi5UfKotei9">
				<input type='hidden' name="kashiUfKotei9Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei9ShiyouFlg or "3" eq hfUfSeigyo.ufKotei9ShiyouFlg}'>
				<button type='button' id='kashiUfKotei9SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei9Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei9Cd5)}' autocomplete="on" list="dtKashi5UfKotei9">
				<input type='text' name="kashiUfKotei9Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei9Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>
								<tr id="ufKotei10" <c:if test='${not ("0" ne hfUfSeigyo.ufKotei10ShiyouFlg)}'>class='never_show' style='display:none;'</c:if>>
									<th class='row-title'><nobr>${HfUfSeigyo.ufKotei10Name}</nobr></th>
									<td>
<c:if test='${inputSeigyo.kariUfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kariUfKotei10Cd" maxlength='20' value='${su:htmlEscape(kariUfKotei10Cd)}' autocomplete="on" list="dtKariUfKotei10">
				<input type='hidden' name="kariUfKotei10Name" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kariUfKotei10SentakuButton' class='btn btn-small'>選択</button>
				<input type='text' name="kariUfKotei10Cd" class='input-small' value='${su:htmlEscape(kariUfKotei10Cd)}' autocomplete="on" list="dtKariUfKotei10">
				<input type='text' name="kariUfKotei10Name" class='input-large' disabled value='${su:htmlEscape(kariUfKotei10Name)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi1UfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kashiUfKotei10Cd1" maxlength='20' value='${su:htmlEscape(kashiUfKotei10Cd1)}' autocomplete="on" list="dtKashi1UfKotei10">
				<input type='hidden' name="kashiUfKotei10Name1" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kashiUfKotei10SentakuButton1' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei10Cd1" class='input-small' value='${su:htmlEscape(kashiUfKotei10Cd1)}' autocomplete="on" list="dtKashi1UfKotei10">
				<input type='text' name="kashiUfKotei10Name1" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei10Name1)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi2UfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kashiUfKotei10Cd2" maxlength='20' value='${su:htmlEscape(kashiUfKotei10Cd2)}' autocomplete="on" list="dtKashi2UfKotei10">
				<input type='hidden' name="kashiUfKotei10Name2" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kashiUfKotei10SentakuButton2' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei10Cd2" class='input-small' value='${su:htmlEscape(kashiUfKotei10Cd2)}' autocomplete="on" list="dtKashi2UfKotei10">
				<input type='text' name="kashiUfKotei10Name2" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei10Name2)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi3UfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kashiUfKotei10Cd3" maxlength='20' value='${su:htmlEscape(kashiUfKotei10Cd3)}' autocomplete="on" list="dtKashi3UfKotei10">
				<input type='hidden' name="kashiUfKotei10Name3" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kashiUfKotei10SentakuButton3' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei10Cd3" class='input-small' value='${su:htmlEscape(kashiUfKotei10Cd3)}' autocomplete="on" list="dtKashi3UfKotei10">
				<input type='text' name="kashiUfKotei10Name3" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei10Name3)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi4UfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kashiUfKotei10Cd4" maxlength='20' value='${su:htmlEscape(kashiUfKotei10Cd4)}' autocomplete="on" list="dtKashi4UfKotei10">
				<input type='hidden' name="kashiUfKotei10Name4" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kashiUfKotei10SentakuButton4' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei10Cd4" class='input-small' value='${su:htmlEscape(kashiUfKotei10Cd4)}' autocomplete="on" list="dtKashi4UfKotei10">
				<input type='text' name="kashiUfKotei10Name4" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei10Name4)}'>
</c:if>
</c:if>
									</td>
									<td>
<c:if test='${inputSeigyo.kashi5UfKotei10}'>
<c:if test='${"0" eq hfUfSeigyo.ufKotei10ShiyouFlg or "1" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<input type='text' name="kashiUfKotei10Cd5" maxlength='20' value='${su:htmlEscape(kashiUfKotei10Cd5)}' autocomplete="on" list="dtKashi5UfKotei10">
				<input type='hidden' name="kashiUfKotei10Name5" value=''>
</c:if>
<c:if test='${"2" eq hfUfSeigyo.ufKotei10ShiyouFlg or "3" eq hfUfSeigyo.ufKotei10ShiyouFlg}'>
				<button type='button' id='kashiUfKotei10SentakuButton5' class='btn btn-small'>選択</button>
				<input type='text' name="kashiUfKotei10Cd5" class='input-small' value='${su:htmlEscape(kashiUfKotei10Cd5)}' autocomplete="on" list="dtKashi5UfKotei10">
				<input type='text' name="kashiUfKotei10Name5" class='input-large' disabled value='${su:htmlEscape(kashiUfKotei10Name5)}'>
</c:if>
</c:if>
									</td>
								</tr>

								<tr>
									<th><nobr>説明</nobr></th>
									<td>
										<p class='output'>${su:htmlEscape(inputSeigyo.kariSetsumeiStr)}</p>
									</td>
									<td>
										<p class='output'>${su:htmlEscape(inputSeigyo.kashiSetsumei1Str)}</p>
									</td>
									<td>
										<c:if test='${inputSeigyo.kashi2Setsumei}'><p class='output'>${su:htmlEscape(inputSeigyo.kashiSetsumei2Str)}</p></c:if>
									</td>
									<td>
										<c:if test='${inputSeigyo.kashi3Setsumei}'><p class='output'>${su:htmlEscape(inputSeigyo.kashiSetsumei3Str)}</p></c:if>
									</td>
									<td>
										<c:if test='${inputSeigyo.kashi4Setsumei}'><p class='output'>${su:htmlEscape(inputSeigyo.kashiSetsumei4Str)}</p></c:if>
									</td>
									<td>
										<c:if test='${inputSeigyo.kashi5Setsumei}'><p class='output'>${su:htmlEscape(inputSeigyo.kashiSetsumei5Str)}</p></c:if>
									</td>
								</tr>
							</tbody>
						</table></div>
					</section>

					<!-- 変数説明 -->
					<section>
						<p>「部門」「勘定科目枝番」「取引先」「プロジェクト」には、コードを直接入力または、検索機能からコード検索するか、下記の一覧から仕訳パターン変数をコピー＆ペーストで入力して下さい。 </p>
						<div class='no-more-tables'>
							<table class='table-bordered table-condensed'>
								<thead>
									<tr>
										<th>仕訳パターン変数</th>
										<th>変数説明</th>
									</tr>
								</thead>
								<tbody>
<c:forEach var='record' items='${inputSeigyo.shiwakeVarList}' varStatus="st">
									<tr>
										<td><p class='output'>${su:htmlEscape(record.shiwake_pattern_var_name)}</p></td>
										<td><p class='output'>${su:htmlEscape(record.var_setsumei)}</p></td>
									</tr>
</c:forEach>
								</tbody>
							</table>
							<datalist id="dtKariBumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariBumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariKamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariKamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariEdano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariEdano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariTorihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariTorihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariProject"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariProject}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariSegment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariSegment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKariUf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKariUf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Bumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Bumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Kamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Kamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Edano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Edano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Torihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Torihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Project"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Project}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Segment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Segment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi1Uf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi1Uf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Bumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Bumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Kamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Kamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Edano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Edano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Torihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Torihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Project"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Project}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Segment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Segment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi2Uf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi2Uf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Bumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Bumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Kamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Kamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Edano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Edano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Torihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Torihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Project"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Project}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Segment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Segment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi3Uf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi3Uf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Bumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Bumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Kamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Kamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Edano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Edano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Torihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Torihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Project"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Project}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Segment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Segment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi4Uf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi4Uf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Bumon"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Bumon}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Kamoku"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Kamoku}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Edano"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Edano}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Torihiki"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Torihiki}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Project"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Project}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Segment"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Segment}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf1"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf1}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf2"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf2}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf3"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf3}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf4"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf4}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf5"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf5}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf6"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf6}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf7"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf7}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf8"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf8}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf9"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf9}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
							<datalist id="dtKashi5Uf10"><c:forEach var='record' items='${inputSeigyo.shiwakeVarListKashi5Uf10}'><option value='${su:htmlEscape(record.shiwake_pattern_var_name)}'></c:forEach></datalist>
						</div>
					</section>
