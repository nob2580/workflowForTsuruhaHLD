/* http://keith-wood.name/calculator.html
   Japanese initialisation for the jQuery calculator extension
   Written by stabucky */
(function($) { // hide the namespace

$.calculator.regional['ja'] = {
	decimalChar: '.',
	buttonText: '...', buttonStatus: '電卓を開く',
	closeText: '閉じる', closeStatus: '電卓を閉じる',
	useText: '使用', useStatus: '現在の値を使用',
	eraseText: '削除', eraseStatus: 'フィールドの値を削除',
	backspaceText: 'BS', backspaceStatus: '最後の桁を削除',
	clearErrorText: 'CE', clearErrorStatus: '最後の数を削除',
	clearText: 'CA', clearStatus: '電卓をリセット',
	memClearText: 'MC', memClearStatus: 'メモリをクリア',
	memRecallText: 'MR', memRecallStatus: 'メモリを呼び出す',
	memStoreText: 'MS', memStoreStatus: 'メモリに保存',
	memAddText: 'M+', memAddStatus: 'メモリに加算',
	memSubtractText: 'M-', memSubtractStatus: 'メモリから除算',
	base2Text: 'Bin', base2Status: '2進数に切り替え',
	base8Text: 'Oct', base8Status: '8進数に切り替え',
	base10Text: 'Dec', base10Status: '10進数に切り替え',
	base16Text: 'Hex', base16Status: '16進数に切り替え',
	degreesText: 'Deg', degreesStatus: '度に切り替え',
	radiansText: 'Rad', radiansStatus: 'ラジアンに切り替え',
	isRTL: false
};
$.calculator.setDefaults($.calculator.regional['ja']);

})(jQuery);
