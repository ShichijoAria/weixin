(function () {
    var input = document.querySelector('input');

    input.onchange = function () {
        lrz(this.files[0], {width: 400}, function (results) {
            // 以下为演示用内容
            var tip = document.querySelector('#tip'),
                report = document.querySelector('#report'),
                footer = document.querySelector('footer');

            report.innerHTML = footer.innerHTML =  '';
            tip.innerHTML = '<p>正在生成和上传..</p> <small class="text-muted">演示使用了大量内存，可能会造成几秒内卡顿，不代表真实表现，请亲测。</small>';
            demo_report('原始图片', results.blob, results.origin.size);

            setTimeout(function () {
                demo_report('客户端预压的图片', results.base64, results.base64.length * 0.8);

                // 发送到后端
                var xhr = new XMLHttpRequest();
                var data = {
                    base64: results.base64,
                    size: results.base64.length // 校验用，防止未完整接收
                };



                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {

                        tip.innerHTML = '<p>卡号：'+xhr.responseText+'</p> <small class="text-muted">演示使用了大量内存，可能会造成几秒内卡顿，不代表真实表现，请亲测。</small>';
                    }
                };


                xhr.open('POST', 'IDCard');
                xhr.setRequestHeader('Content-Type', 'application/json; charset=utf-8');
                xhr.send(JSON.stringify(data)); // 发送base64
            }, 100);
        });
    };

    /**
     * 演示报告
     * @param title
     * @param src
     * @param size
     */
    function demo_report(title, src, size) {
        var img = new Image(),
            li = document.createElement('li'),
            size = (size / 1024).toFixed(2) + 'KB';

        img.onload = function () {
            var content = '<ul>' +
                '<li>' + title + '（' + img.width + ' X ' + img.height + '）<>' +
                '<li class="text-cyan">' + size + '<>' +
                '</ul>';

            li.className = 'item';
            li.innerHTML = content;
            li.appendChild(img);
            document.querySelector('#report').appendChild(li);
        };

        img.src = src;
    }

    // 演示用服务器太慢，做个延缓加载
    window.onload = function () {
        input.style.display = 'block';
    }
})();