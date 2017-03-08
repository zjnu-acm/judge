/*
 * CKFinder
 * ========
 * http://cksource.com/ckfinder
 * Copyright (C) 2007-2015, CKSource - Frederico Knabben. All rights reserved.
 *
 * The software, this file, and its contents are subject to the CKFinder
 * License. Please read the license.txt file before using, installing, copying,
 * modifying, or distributing this file or part of its contents. The contents of
 * this file is part of the Source Code of CKFinder.
 *
 */

/**
 * @fileOverview Defines the {@link CKFinder.lang} object, for the Turkish
 *		language.
 *
 *	Turkish translation by Abdullah M CEYLAN a.k.a. Kenan Balamir. Updated.
 * 	G\xFCnce BEKTA\u015E update tr.js file and translate help folder.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['tr'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility"> \xF6\u011Fesi, mevcut de\u011Fil</span>',
		confirmCancel	: 'Baz\u0131 se\xE7enekler de\u011Fi\u015Ftirildi. Pencereyi kapatmak istiyor musunuz?',
		ok				: 'Tamam',
		cancel			: 'Vazge\xE7',
		confirmationTitle	: 'Onay',
		messageTitle	: 'Bilgi',
		inputTitle		: 'Soru',
		undo			: 'Geri Al',
		redo			: 'Yinele',
		skip			: 'Atla',
		skipAll			: 'T\xFCm\xFCn\xFC Atla',
		makeDecision	: 'Hangi i\u015Flem yap\u0131ls\u0131n?',
		rememberDecision: 'Karar\u0131m\u0131 hat\u0131rla'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'tr',

	// Date Format
	//		d    : Day
	//		dd   : Day (padding zero)
	//		m    : Month
	//		mm   : Month (padding zero)
	//		yy   : Year (two digits)
	//		yyyy : Year (four digits)
	//		h    : Hour (12 hour clock)
	//		hh   : Hour (12 hour clock, padding zero)
	//		H    : Hour (24 hour clock)
	//		HH   : Hour (24 hour clock, padding zero)
	//		M    : Minute
	//		MM   : Minute (padding zero)
	//		a    : Firt char of AM/PM
	//		aa   : AM/PM
	DateTime : 'd/m/yyyy h:MM aa',
	DateAmPm : ['GN', 'GC'],

	// Folders
	FoldersTitle	: 'Klas\xF6rler',
	FolderLoading	: 'Y\xFCkleniyor...',
	FolderNew		: 'L\xFCtfen yeni klas\xF6r ad\u0131n\u0131 yaz\u0131n: ',
	FolderRename	: 'L\xFCtfen yeni klas\xF6r ad\u0131n\u0131 yaz\u0131n: ',
	FolderDelete	: '"%1" klas\xF6r\xFCn\xFC silmek istedi\u011Finizden emin misiniz?',
	FolderRenaming	: ' (Yeniden adland\u0131r\u0131l\u0131yor...)',
	FolderDeleting	: ' (Siliniyor...)',
	DestinationFolder	: 'Hedef Klas\xF6r',

	// Files
	FileRename		: 'L\xFCtfen yeni dosyan\u0131n ad\u0131n\u0131 yaz\u0131n: ',
	FileRenameExt	: 'Dosya uzant\u0131s\u0131n\u0131 de\u011Fi\u015Ftirmek istiyor musunuz? Bu, dosyay\u0131 kullan\u0131lamaz hale getirebilir.',
	FileRenaming	: 'Yeniden adland\u0131r\u0131l\u0131yor...',
	FileDelete		: '"%1" dosyas\u0131n\u0131 silmek istedi\u011Finizden emin misiniz?',
	FilesDelete	: '%1 adet dosyay\u0131 silmek istedi\u011Finize emin misiniz?',
	FilesLoading	: 'Y\xFCkleniyor...',
	FilesEmpty		: 'Klas\xF6r bo\u015F',
	DestinationFile	: 'Hedef Dosya',
	SkippedFiles	: 'Atlanan dosyalar\u0131n listesi:',

	// Basket
	BasketFolder		: 'Sepet',
	BasketClear			: 'Sepeti temizle',
	BasketRemove		: 'Sepetten sil',
	BasketOpenFolder	: '\xDCst klas\xF6r\xFC a\xE7',
	BasketTruncateConfirm : 'Sepetteki t\xFCm dosyalar\u0131 silmek istedi\u011Finizden emin misiniz?',
	BasketRemoveConfirm	: 'Sepetteki %1% dosyas\u0131n\u0131 silmek istedi\u011Finizden emin misiniz?',
	BasketRemoveConfirmMultiple	: '%1 adet dosyay\u0131 sepetinizden \xE7\u0131kartmak istedi\u011Finize emin misiniz?',
	BasketEmpty			: 'Sepette hi\xE7 dosya yok, birka\xE7 tane s\xFCr\xFCkleyip b\u0131rakabilirsiniz',
	BasketCopyFilesHere	: 'Sepetten Dosya Kopyala',
	BasketMoveFilesHere	: 'Sepetten Dosya Ta\u015F\u0131',

	// Global messages
	OperationCompletedSuccess	: '\u0130\u015Flem ba\u015Far\u0131yla tamamland\u0131.',
	OperationCompletedErrors		: '\u0130\u015Flem hatalar olmas\u0131na kar\u015F\u0131n tamamland\u0131.',
	FileError				: '%s: %e',

	// Move and Copy files
	MovedFilesNumber		: 'Ta\u015F\u0131nan dosya say\u0131s\u0131: %s.',
	CopiedFilesNumber	: 'Kopyalanan dosya say\u0131s\u0131: %s.',
	MoveFailedList		: 'Ta\u015F\u0131namayan dosyalar:<br />%s',
	CopyFailedList		: 'Koplanamayan dosyalar:<br />%s',

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'Y\xFCkle',
	UploadTip	: 'Yeni Dosya Y\xFCkle',
	Refresh		: 'Yenile',
	Settings	: 'Ayarlar',
	Help		: 'Yard\u0131m',
	HelpTip		: 'Yard\u0131m',

	// Context Menus
	Select			: 'Se\xE7',
	SelectThumbnail : '\xD6nizleme Olarak Se\xE7',
	View			: 'G\xF6r\xFCnt\xFCle',
	Download		: '\u0130ndir',

	NewSubFolder	: 'Yeni Altklas\xF6r',
	Rename			: 'Yeniden Adland\u0131r',
	Delete			: 'Sil',
	DeleteFiles		: 'Dosyalar\u0131 sil',

	CopyDragDrop	: 'Buraya kopyala',
	MoveDragDrop	: 'Buraya ta\u015F\u0131',

	// Dialogs
	RenameDlgTitle		: 'Yeniden Adland\u0131r',
	NewNameDlgTitle		: 'Yeni Ad\u0131',
	FileExistsDlgTitle	: 'Dosya zaten var',
	SysErrorDlgTitle : 'Sistem hatas\u0131',

	FileOverwrite	: '\xDCzerine yaz',
	FileAutorename	: 'Oto-Yeniden Adland\u0131r',
	ManuallyRename	: 'Elle isimlendir',

	// Generic
	OkBtn		: 'Tamam',
	CancelBtn	: 'Vazge\xE7',
	CloseBtn	: 'Kapat',

	// Upload Panel
	UploadTitle			: 'Yeni Dosya Y\xFCkle',
	UploadSelectLbl		: 'Y\xFCklenecek dosyay\u0131 se\xE7in',
	UploadProgressLbl	: '(Y\xFCkleniyor, l\xFCtfen bekleyin...)',
	UploadBtn			: 'Se\xE7ili Dosyay\u0131 Y\xFCkle',
	UploadBtnCancel		: 'Vazge\xE7',

	UploadNoFileMsg		: 'L\xFCtfen bilgisayar\u0131n\u0131zdan dosya se\xE7in',
	UploadNoFolder		: 'L\xFCtfen y\xFCklemeden \xF6nce klas\xF6r se\xE7in.',
	UploadNoPerms		: 'Dosya y\xFCklemeye izin verilmiyor.',
	UploadUnknError		: 'Dosya g\xF6nderme hatas\u0131.',
	UploadExtIncorrect	: 'Bu dosya uzant\u0131s\u0131na, bu klas\xF6rde izin verilmiyor.',

	// Flash Uploads
	UploadLabel			: 'G\xF6nderilecek Dosyalar',
	UploadTotalFiles	: 'Toplam Dosyalar:',
	UploadTotalSize		: 'Toplam B\xFCy\xFCkl\xFCk:',
	UploadSend			: 'Y\xFCkle',
	UploadAddFiles		: 'Dosyalar\u0131 Ekle',
	UploadClearFiles	: 'Dosyalar\u0131 Temizle',
	UploadCancel		: 'G\xF6ndermeyi \u0130ptal Et',
	UploadRemove		: 'Sil',
	UploadRemoveTip		: '!f sil',
	UploadUploaded		: '!n% g\xF6nderildi',
	UploadProcessing	: 'G\xF6nderiliyor...',

	// Settings Panel
	SetTitle		: 'Ayarlar',
	SetView			: 'G\xF6r\xFCn\xFCm:',
	SetViewThumb	: '\xD6nizlemeler',
	SetViewList		: 'Liste',
	SetDisplay		: 'G\xF6sterim:',
	SetDisplayName	: 'Dosya ad\u0131',
	SetDisplayDate	: 'Tarih',
	SetDisplaySize	: 'Dosya boyutu',
	SetSort			: 'S\u0131ralama:',
	SetSortName		: 'Dosya ad\u0131na g\xF6re',
	SetSortDate		: 'Tarihe g\xF6re',
	SetSortSize		: 'Boyuta g\xF6re',
	SetSortExtension		: 'Uzant\u0131s\u0131na g\xF6re',

	// Status Bar
	FilesCountEmpty : '<Klas\xF6rde Dosya Yok>',
	FilesCountOne	: '1 dosya',
	FilesCountMany	: '%1 dosya',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/sn',

	// Connector Error Messages.
	ErrorUnknown	: '\u0130ste\u011Finizi yerine getirmek m\xFCmk\xFCn de\u011Fil. (Hata %1)',
	Errors :
	{
	 10 : 'Ge\xE7ersiz komut.',
	 11 : '\u0130stekte kaynak t\xFCr\xFC belirtilmemi\u015F.',
	 12 : 'Talep edilen kaynak t\xFCr\xFC ge\xE7ersiz.',
	102 : 'Ge\xE7ersiz dosya ya da klas\xF6r ad\u0131.',
	103 : 'Kimlik do\u011Frulama k\u0131s\u0131tlamalar\u0131 nedeni ile talebinizi yerine getiremiyoruz.',
	104 : 'Dosya sistemi k\u0131s\u0131tlamalar\u0131 nedeni ile talebinizi yerine getiremiyoruz.',
	105 : 'Ge\xE7ersiz dosya uzant\u0131s\u0131.',
	109 : 'Ge\xE7ersiz istek.',
	110 : 'Bilinmeyen hata.',
	111 : 'Dosya boyutundan dolay\u0131 bu i\u015Flemin yap\u0131lmas\u0131 m\xFCmk\xFCn de\u011Fil.',
	115 : 'Ayn\u0131 isimde bir dosya ya da klas\xF6r zaten var.',
	116 : 'Klas\xF6r bulunamad\u0131. L\xFCtfen yenileyin ve tekrar deneyin.',
	117 : 'Dosya bulunamad\u0131. L\xFCtfen dosya listesini yenileyin ve tekrar deneyin.',
	118 : 'Kaynak ve hedef yol ayn\u0131!',
	201 : 'Ayn\u0131 ada sahip bir dosya zaten var. Y\xFCklenen dosyan\u0131n ad\u0131 "%1" olarak de\u011Fi\u015Ftirildi.',
	202 : 'Ge\xE7ersiz dosya',
	203 : 'Ge\xE7ersiz dosya. Dosya boyutu \xE7ok b\xFCy\xFCk.',
	204 : 'Y\xFCklenen dosya bozuk.',
	205 : 'Dosyalar\u0131 y\xFCklemek i\xE7in gerekli ge\xE7ici klas\xF6r sunucuda bulunamad\u0131.',
	206 : 'G\xFCvenlik nedeni ile y\xFCkleme iptal edildi. Dosya HTML benzeri veri i\xE7eriyor.',
	207 : 'Y\xFCklenen dosyan\u0131n ad\u0131 "%1" olarak de\u011Fi\u015Ftirildi.',
	300 : 'Dosya ta\u015F\u0131ma i\u015Flemi ba\u015Far\u0131s\u0131z.',
	301 : 'Dosya kopyalama i\u015Flemi ba\u015Far\u0131s\u0131z.',
	500 : 'G\xFCvenlik nedeni ile dosya gezgini devred\u0131\u015F\u0131 b\u0131rak\u0131ld\u0131. L\xFCtfen sistem y\xF6neticiniz ile irtibata ge\xE7in ve CKFinder yap\u0131land\u0131rma dosyas\u0131n\u0131 kontrol edin.',
	501 : '\xD6nizleme deste\u011Fi devred\u0131\u015F\u0131.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Dosya ad\u0131 bo\u015F olamaz',
		FileExists		: '%s dosyas\u0131 zaten var',
		FolderEmpty		: 'Klas\xF6r ad\u0131 bo\u015F olamaz',
		FolderExists	: '%s klas\xF6r\xFC zaten mevcut.',
		FolderNameExists	: 'Klas\xF6r zaten mevcut.',

		FileInvChar		: 'Dosya ad\u0131n\u0131n i\xE7ermesi m\xFCmk\xFCn olmayan karakterler: \n\\ / : * ? " < > |',
		FolderInvChar	: 'Klas\xF6r ad\u0131n\u0131n i\xE7ermesi m\xFCmk\xFCn olmayan karakterler: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Dosyay\u0131 yeni pencerede a\xE7mak i\xE7in, taray\u0131c\u0131 ayarlar\u0131ndan bu sitenin a\xE7\u0131l\u0131r pencerelerine izin vermeniz gerekiyor.',
		XmlError		: 'Web sunucusundan XML yan\u0131t\u0131 d\xFCzg\xFCn bir \u015Fekilde y\xFCklenemedi.',
		XmlEmpty		: 'Web sunucusundan XML yan\u0131t\u0131 d\xFCzg\xFCn bir \u015Fekilde y\xFCklenemedi. Sunucudan bo\u015F cevap d\xF6nd\xFC.',
		XmlRawResponse	: 'Sunucudan gelen ham mesaj: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: 'Boyutland\u0131r: %s',
		sizeTooBig		: 'Y\xFCkseklik ve geni\u015Flik de\u011Feri orijinal boyuttan b\xFCy\xFCk oldu\u011Fundan, i\u015Flem ger\xE7ekle\u015Ftirilemedi (%size).',
		resizeSuccess	: 'Resim ba\u015Far\u0131yla yeniden boyutland\u0131r\u0131ld\u0131.',
		thumbnailNew	: 'Yeni \xF6nizleme olu\u015Ftur',
		thumbnailSmall	: 'K\xFC\xE7\xFCk (%s)',
		thumbnailMedium	: 'Orta (%s)',
		thumbnailLarge	: 'B\xFCy\xFCk (%s)',
		newSize			: 'Yeni boyutu ayarla',
		width			: 'Geni\u015Flik',
		height			: 'Y\xFCkseklik',
		invalidHeight	: 'Ge\xE7ersiz y\xFCkseklik.',
		invalidWidth	: 'Ge\xE7ersiz geni\u015Flik.',
		invalidName		: 'Ge\xE7ersiz dosya ad\u0131.',
		newImage		: 'Yeni resim olu\u015Ftur',
		noExtensionChange : 'Dosya uzant\u0131s\u0131 de\u011Fi\u015Ftirilemedi.',
		imageSmall		: 'Kaynak resim \xE7ok k\xFC\xE7\xFCk',
		contextMenuName	: 'Boyutland\u0131r',
		lockRatio		: 'Oran\u0131 kilitle',
		resetSize		: 'B\xFCy\xFCkl\xFC\u011F\xFC s\u0131f\u0131rla'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'Kaydet',
		fileOpenError	: 'Dosya a\xE7\u0131lamad\u0131.',
		fileSaveSuccess	: 'Dosya ba\u015Far\u0131yla kaydedildi.',
		contextMenuName	: 'D\xFCzenle',
		loadingFile		: 'Dosya y\xFCkleniyor, l\xFCtfen bekleyin...'
	},

	Maximize :
	{
		maximize : 'B\xFCy\xFClt',
		minimize : 'K\xFC\xE7\xFClt'
	},

	Gallery :
	{
		current : '{current} / {total} resim'
	},

	Zip :
	{
		extractHereLabel	: 'Buraya a\xE7',
		extractToLabel		: 'Hedefe a\xE7...',
		downloadZipLabel	: 'Zip olarak indir',
		compressZipLabel	: 'Zip dosyas\u0131 olarak s\u0131k\u0131\u015Ft\u0131r',
		removeAndExtract	: 'Varolan\u0131 kald\u0131r ve a\xE7',
		extractAndOverwrite	: 'Mevcut dosyalar\u0131n \xFCzerine yazarak a\xE7',
		extractSuccess		: 'Ba\u015Far\u0131yla a\xE7\u0131ld\u0131.'
	},

	Search :
	{
		searchPlaceholder : 'Ara'
	}
};
