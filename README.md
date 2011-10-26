# �����N

* [Dispatch Github](https://github.com/n8han/Databinder-Dispatch)

* [Dispatch�`���[�g���A��](http://dispatch.databinder.net/Dispatch.html)

* [Dispatch API���t�@�����X](http://databinder.net/dispatch-doc/#package)

* [Twitter�T���v��](https://github.com/n8han/dispatch-twitter)

# ���̃T���v����Usage

    sbt console
    
    :load src/scala/Atnd.scala
    
    // @shibuyascala���o�^����Atnd�C�x���g��ID���擾����
    getEventsOwnedByUser("shibuyascala")
    
    // @cbirchall���Q������Atnd�C�x���g��ID���擾����
    getEventsAttendedByUser("cbirchall")
    
    // @shibuyascala���o�^����Atnd�C�x���g�̖��O���擾����i������������j
    getEventTitles(getEventsOwnedByUser("shibuyascala"))

# Dispatch�ɂ���

## ��{�I�Ȏg����

    val result = <executor>(<request> <response handler>)

## Executor

* HTTP�i����X���b�h�j

�ˑ����Fdispatch-http

������Apache HttpClient�g�p

    val h = new dispatch.Http
    val result: String = h(url("http://google.com/") as_str)

* HTTP�i�ʃX���b�h�j

�ˑ����Fdispatch-http

    val h = new dispatch.thread.Http
    val result: Future[String] = h(url("http://google.com/") as_str)

### Future

�Ԃ��Ă���`Future`��`apply()`����Ό��ʂ�������B

`Future`�̏�Ԃ��m�F����ɂ�`isSet`���Ăяo���B

* HTTP�inio�j

�ˑ����Fdispatch-nio

    val h = new dispatch.nio.Http
    val result: Future[String] = h(url("http://google.com/") as_str)

* Google App Engine��p

�ˑ����Fdispatch-gae

    val h = new dispatch.gae.Http
    val result: String = h(url("http://google.com/") as_str)

## Request

### URL

* `url()`���\�b�h���g��

    val url: Request = url("http://example.com/a/b/")

* �z�X�g�ƃp�X��g�ݍ��킹��

    val url: Request = :/("example.com") / "a" / "b"

### Request verbs

���N�G�X�g��ϊ����邽�߂̉��Z�q�B

* `<:<` HTTP�w�b�_���w��

* `<<` HTTP POST�̃{�f�B���w��

* `<<<` HTTP PUT�̃{�f�B���w��

* `<<?` URL�N�G���p�����[�^��t����

* `>\` �����R�[�h��ݒ�

* `as` HTTP BASIC/DIGEST�F�؂̃��[�U���ƃp�X���[�h���w��

* `gzip` Gzip���k�𗊂�

* `secure` HTTPS���g��

��j

    val req = url("http://example.com/a/b/") <<? Map("user" -> "chris")

## Response handlers

�Ԃ��Ă���HTTP���X�|���X����������n���h���B

* `>|` ���X�|���X�𖳎�����

* `as_str` �{�f�B��String�Ƃ��ĕԂ�

* `>-` �{�f�B��String�Ƃ��Ĉ����ď�������

* `as_source` �{�f�B��scala.io.Source�Ƃ��ĕԂ�

* `>~` �{�f�B��scala.io.Source�Ƃ��Ĉ����ď�������

* `>:>` �w�b�_��Map�Ƃ��Ĉ����ď�������

* `>>>` �{�f�B��OutputStream�ɏ�������

* `<>` �{�f�B��XML�Ƃ��Ĉ����ď�������

* `</>` �{�f�B��XHTML�Ƃ��Ĉ����ď�������

