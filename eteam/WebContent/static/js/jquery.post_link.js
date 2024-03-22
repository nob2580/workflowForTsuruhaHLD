/**
 *
 * リンククリック時に POST でリクエストするようにフックする.
 *
 * [使い方]
 *
 * HTML で以下のようにリンクを設定しているとする.
 *
 *     <a href="..." class="post">...</a>
 *
 * Javascript で次のように記述することで, 本プラグインを有効化することができる.
 *
 *     $("a.post").toPostLink();
 *
 * [詳細]
 *
 * GET でリクエストする場合に, URL が長すぎるとサーバ側でエラーとなる.
 * クエリ文字列が長くなりすぎる場合に, この問題が顕在化することが多い.
 * 対応としてはリクエストを POST で送信することで問題を回避することができる.
 *
 * Ruby on Rails では以下のように link_to メソッドのオプションで POST を指定できるが,
 * クエリ文字列は URL に含まれたままリクエストが送信されるため, この問題の解決にはならない.
 *
 *     link_to('...', '...', method: :post)
 *
 * 本プラグインはリンククリック時に次の処理を行うことで問題を回避する.
 *
 *     1. 内部的に form 要素を作成する.
 *     2. クエリ文字列は <input type="hidden"> として追加する.
 *     3. 生成した form を submit する.
 *
 * MIT License
 *
 */
(function($) {

    $.fn.toPostLink = function() {
        var escapeHTML = function(html) {
            return $('<div>').text(html).html();
        };

        $(this).click(function() {
            var href = $(this).attr("href").split("#")[0].split("?", 2);
            var path = href[0];
            var query = href[1] || "";

            var form = $("<form method='post' action='" + escapeHTML(path) + "'>").appendTo($("body"));

            // query parameters.
            $.each(query.split("&"), function() {
                var pair = this.split("=");
                var name = decodeURIComponent(pair[0]);
                var value = decodeURIComponent(pair[1]);
                $("<input type='hidden' name='" + escapeHTML(name) + "' value='" + escapeHTML(value) + "' />").appendTo(form);
            });

            // csrf token.
            var csrf_token = $('meta[name=csrf-token]').attr('content');
            var csrf_param = $('meta[name=csrf-param]').attr('content');
            if (csrf_param !== undefined && csrf_token !== undefined) {
                $("<input type='hidden' name='" + escapeHTML(csrf_param) + "' value='" + escapeHTML(csrf_token) + "' />").appendTo(form);
            }

            form.submit();

            return false;
        });
    };

})(jQuery);