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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Vietnamese
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['vi'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, kh\xF4ng kh\u1EA3 d\u1EE5ng</span>',
		confirmCancel	: 'V\xE0i t\xF9y ch\u1ECDn \u0111\xE3 thay \u0111\u1ED5i. B\u1EA1n c\xF3 mu\u1ED1n \u0111\xF3ng h\u1ED9p tho\u1EA1i?',
		ok				: 'OK',
		cancel			: 'H\u1EE7y',
		confirmationTitle	: 'X\xE1c nh\u1EADn',
		messageTitle	: 'Th\xF4ng tin',
		inputTitle		: 'C\xE2u h\u1ECFi',
		undo			: 'Ho\xE0n t\xE1c',
		redo			: 'L\xE0m l\u1EA1i',
		skip			: 'B\u1ECF qua',
		skipAll			: 'B\u1ECF qua t\u1EA5t c\u1EA3',
		makeDecision	: 'Ch\u1ECDn h\xE0nh \u0111\u1ED9ng n\xE0o?',
		rememberDecision: 'Ghi nh\u1EDB quy\u1EBFt \u0111\u1ECBnh n\xE0y'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'vi',

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
	DateAmPm : ['SA', 'CH'],

	// Folders
	FoldersTitle	: 'Th\u01B0 m\u1EE5c',
	FolderLoading	: '\u0110ang t\u1EA3i...',
	FolderNew		: 'Xin ch\u1ECDn t\xEAn cho th\u01B0 m\u1EE5c m\u1EDBi: ',
	FolderRename	: 'Xin ch\u1ECDn t\xEAn m\u1EDBi cho th\u01B0 m\u1EE5c: ',
	FolderDelete	: 'B\u1EA1n c\xF3 ch\u1EAFc mu\u1ED1n x\xF3a th\u01B0 m\u1EE5c "%1"?',
	FolderRenaming	: ' (\u0110ang \u0111\u1ED5i t\xEAn...)',
	FolderDeleting	: ' (\u0110ang x\xF3a...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: 'Xin nh\u1EADp t\xEAn t\u1EADp tin m\u1EDBi: ',
	FileRenameExt	: 'B\u1EA1n c\xF3 ch\u1EAFc mu\u1ED1n \u0111\u1ED5i ph\u1EA7n m\u1EDF r\u1ED9ng? T\u1EADp tin c\xF3 th\u1EC3 s\u1EBD kh\xF4ng d\xF9ng \u0111\u01B0\u1EE3c.',
	FileRenaming	: '\u0110ang \u0111\u1ED5i t\xEAn...',
	FileDelete		: 'B\u1EA1n c\xF3 ch\u1EAFc mu\u1ED1n x\xF3a t\u1EADp tin "%1"?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: '\u0110ang t\u1EA3i...',
	FilesEmpty		: 'Th\u01B0 m\u1EE5c tr\u1ED1ng.',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: 'R\u1ED5',
	BasketClear			: 'D\u1ECDn r\u1ED5',
	BasketRemove		: 'X\xF3a kh\u1ECFi r\u1ED5',
	BasketOpenFolder	: 'M\u1EDF th\u01B0 m\u1EE5c cha',
	BasketTruncateConfirm : 'B\u1EA1n c\xF3 ch\u1EAFc mu\u1ED1n b\u1ECF t\u1EA5t c\u1EA3 t\u1EADp tin trong r\u1ED5?',
	BasketRemoveConfirm	: 'B\u1EA1n c\xF3 ch\u1EAFc mu\u1ED1n b\u1ECF t\u1EADp tin "%1" kh\u1ECFi r\u1ED5?',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: 'Kh\xF4ng c\xF3 t\u1EADp tin trong r\u1ED5, h\xE3y k\xE9o v\xE0 th\u1EA3 t\u1EADp tin v\xE0o r\u1ED5.',
	BasketCopyFilesHere	: 'Ch\xE9p t\u1EADp tin t\u1EEB r\u1ED5',
	BasketMoveFilesHere	: 'Chuy\u1EC3n t\u1EADp tin t\u1EEB r\u1ED5',

	// Global messages
	OperationCompletedSuccess	: 'Operation completed successfully.', // MISSING
	OperationCompletedErrors		: 'Operation completed with errors.', // MISSING
	FileError				: '%s: %e', // MISSING

	// Move and Copy files
	MovedFilesNumber		: 'Number of files moved: %s.', // MISSING
	CopiedFilesNumber	: 'Number of files copied: %s.', // MISSING
	MoveFailedList		: 'The following files could not be moved:<br />%s', // MISSING
	CopyFailedList		: 'The following files could not be copied:<br />%s', // MISSING

	// Toolbar Buttons (some used elsewhere)
	Upload		: 'T\u1EA3i l\xEAn',
	UploadTip	: 'T\u1EA3i t\u1EADp tin m\u1EDBi',
	Refresh		: 'L\xE0m t\u01B0\u01A1i',
	Settings	: 'Thi\u1EBFt l\u1EADp',
	Help		: 'H\u01B0\u1EDBng d\u1EABn',
	HelpTip		: 'H\u01B0\u1EDBng d\u1EABn',

	// Context Menus
	Select			: 'Ch\u1ECDn',
	SelectThumbnail : 'Ch\u1ECDn \u1EA3nh m\u1EABu',
	View			: 'Xem',
	Download		: 'T\u1EA3i v\u1EC1',

	NewSubFolder	: 'T\u1EA1o th\u01B0 m\u1EE5c con',
	Rename			: '\u0110\u1ED5i t\xEAn',
	Delete			: 'X\xF3a',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: 'Sao ch\xE9p \u1EDF \u0111\xE2y',
	MoveDragDrop	: 'Di chuy\u1EC3n \u1EDF \u0111\xE2y',

	// Dialogs
	RenameDlgTitle		: '\u0110\u1ED5i t\xEAn',
	NewNameDlgTitle		: 'T\xEAn m\u1EDBi',
	FileExistsDlgTitle	: 'T\u1EADp tin \u0111\xE3 t\u1ED3n t\u1EA1i',
	SysErrorDlgTitle : 'L\u1ED7i h\u1EC7 th\u1ED1ng',

	FileOverwrite	: 'Ghi \u0111\xE8',
	FileAutorename	: 'T\u1EF1 \u0111\u1ED5i t\xEAn',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: 'OK',
	CancelBtn	: 'H\u1EE7y b\u1ECF',
	CloseBtn	: '\u0110\xF3ng',

	// Upload Panel
	UploadTitle			: 'T\u1EA3i t\u1EADp tin m\u1EDBi',
	UploadSelectLbl		: 'Ch\u1ECDn t\u1EADp tin t\u1EA3i l\xEAn',
	UploadProgressLbl	: '(\u0110ang t\u1EA3i l\xEAn, vui l\xF2ng ch\u1EDD...)',
	UploadBtn			: 'T\u1EA3i t\u1EADp tin \u0111\xE3 ch\u1ECDn',
	UploadBtnCancel		: 'H\u1EE7y b\u1ECF',

	UploadNoFileMsg		: 'Xin ch\u1ECDn m\u1ED9t t\u1EADp tin trong m\xE1y t\xEDnh.',
	UploadNoFolder		: 'Xin ch\u1ECDn th\u01B0 m\u1EE5c tr\u01B0\u1EDBc khi t\u1EA3i l\xEAn.',
	UploadNoPerms		: 'Kh\xF4ng \u0111\u01B0\u1EE3c ph\xE9p t\u1EA3i l\xEAn.',
	UploadUnknError		: 'L\u1ED7i khi t\u1EA3i t\u1EADp tin.',
	UploadExtIncorrect	: 'Ki\u1EC3u t\u1EADp tin kh\xF4ng \u0111\u01B0\u1EE3c ch\u1EA5p nh\u1EADn trong th\u01B0 m\u1EE5c n\xE0y.',

	// Flash Uploads
	UploadLabel			: 'T\u1EADp tin s\u1EBD t\u1EA3i:',
	UploadTotalFiles	: 'T\u1ED5ng s\u1ED1 t\u1EADp tin:',
	UploadTotalSize		: 'Dung l\u01B0\u1EE3ng t\u1ED5ng c\u1ED9ng:',
	UploadSend			: 'T\u1EA3i l\xEAn',
	UploadAddFiles		: 'Th\xEAm t\u1EADp tin',
	UploadClearFiles	: 'X\xF3a t\u1EADp tin',
	UploadCancel		: 'H\u1EE7y t\u1EA3i',
	UploadRemove		: 'X\xF3a',
	UploadRemoveTip		: 'X\xF3a !f',
	UploadUploaded		: '\u0110\xE3 t\u1EA3i !n%',
	UploadProcessing	: '\u0110ang x\u1EED l\xED...',

	// Settings Panel
	SetTitle		: 'Thi\u1EBFt l\u1EADp',
	SetView			: 'Xem:',
	SetViewThumb	: '\u1EA2nh m\u1EABu',
	SetViewList		: 'Danh s\xE1ch',
	SetDisplay		: 'Hi\u1EC3n th\u1ECB:',
	SetDisplayName	: 'T\xEAn t\u1EADp tin',
	SetDisplayDate	: 'Ng\xE0y',
	SetDisplaySize	: 'Dung l\u01B0\u1EE3ng',
	SetSort			: 'S\u1EAFp x\u1EBFp:',
	SetSortName		: 'theo t\xEAn',
	SetSortDate		: 'theo ng\xE0y',
	SetSortSize		: 'theo dung l\u01B0\u1EE3ng',
	SetSortExtension		: 'theo ph\u1EA7n m\u1EDF r\u1ED9ng',

	// Status Bar
	FilesCountEmpty : '<Th\u01B0 m\u1EE5c r\u1ED7ng>',
	FilesCountOne	: '1 t\u1EADp tin',
	FilesCountMany	: '%1 t\u1EADp tin',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: 'Kh\xF4ng th\u1EC3 ho\xE0n t\u1EA5t y\xEAu c\u1EA7u. (L\u1ED7i %1)',
	Errors :
	{
	 10 : 'L\u1EC7nh kh\xF4ng h\u1EE3p l\u1EC7.',
	 11 : 'Ki\u1EC3u t\xE0i nguy\xEAn kh\xF4ng \u0111\u01B0\u1EE3c ch\u1EC9 \u0111\u1ECBnh trong y\xEAu c\u1EA7u.',
	 12 : 'Ki\u1EC3u t\xE0i nguy\xEAn y\xEAu c\u1EA7u kh\xF4ng h\u1EE3p l\u1EC7.',
	102 : 'T\xEAn t\u1EADp tin hay th\u01B0 m\u1EE5c kh\xF4ng h\u1EE3p l\u1EC7.',
	103 : 'Kh\xF4ng th\u1EC3 ho\xE0n t\u1EA5t y\xEAu c\u1EA7u v\xEC gi\u1EDBi h\u1EA1n quy\u1EC1n.',
	104 : 'Kh\xF4ng th\u1EC3 ho\xE0n t\u1EA5t y\xEAu c\u1EA7u v\xEC gi\u1EDBi h\u1EA1n quy\u1EC1n c\u1EE7a h\u1EC7 th\u1ED1ng t\u1EADp tin.',
	105 : 'Ph\u1EA7n m\u1EDF r\u1ED9ng t\u1EADp tin kh\xF4ng h\u1EE3p l\u1EC7.',
	109 : 'Y\xEAu c\u1EA7u kh\xF4ng h\u1EE3p l\u1EC7.',
	110 : 'L\u1ED7i kh\xF4ng x\xE1c \u0111\u1ECBnh.',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : 'T\u1EADp tin ho\u1EB7c th\u01B0 m\u1EE5c c\xF9ng t\xEAn \u0111\xE3 t\u1ED3n t\u1EA1i.',
	116 : 'Kh\xF4ng th\u1EA5y th\u01B0 m\u1EE5c. H\xE3y l\xE0m t\u01B0\u01A1i v\xE0 th\u1EED l\u1EA1i.',
	117 : 'Kh\xF4ng th\u1EA5y t\u1EADp tin. H\xE3y l\xE0m t\u01B0\u01A1i v\xE0 th\u1EED l\u1EA1i.',
	118 : '\u0110\u01B0\u1EDDng d\u1EABn ngu\u1ED3n v\xE0 \u0111\xEDch gi\u1ED1ng nhau.',
	201 : 'T\u1EADp tin c\xF9ng t\xEAn \u0111\xE3 t\u1ED3n t\u1EA1i. T\u1EADp tin v\u1EEBa t\u1EA3i l\xEAn \u0111\u01B0\u1EE3c \u0111\u1ED5i t\xEAn th\xE0nh "%1".',
	202 : 'T\u1EADp tin kh\xF4ng h\u1EE3p l\u1EC7.',
	203 : 'T\u1EADp tin kh\xF4ng h\u1EE3p l\u1EC7. Dung l\u01B0\u1EE3ng qu\xE1 l\u1EDBn.',
	204 : 'T\u1EADp tin t\u1EA3i l\xEAn b\u1ECB h\u1ECFng.',
	205 : 'Kh\xF4ng c\xF3 th\u01B0 m\u1EE5c t\u1EA1m \u0111\u1EC3 t\u1EA3i t\u1EADp tin.',
	206 : 'Hu\u1EF7 t\u1EA3i l\xEAn v\xEC l\xED do b\u1EA3o m\u1EADt. T\u1EADp tin ch\u1EE9a d\u1EEF li\u1EC7u gi\u1ED1ng HTML.',
	207 : 'T\u1EADp tin \u0111\u01B0\u1EE3c \u0111\u1ED5i t\xEAn th\xE0nh "%1".',
	300 : 'Di chuy\u1EC3n t\u1EADp tin th\u1EA5t b\u1EA1i.',
	301 : 'Ch\xE9p t\u1EADp tin th\u1EA5t b\u1EA1i.',
	500 : 'Tr\xECnh duy\u1EC7t t\u1EADp tin b\u1ECB v\xF4 hi\u1EC7u v\xEC l\xED do b\u1EA3o m\u1EADt. Xin li\xEAn h\u1EC7 qu\u1EA3n tr\u1ECB h\u1EC7 th\u1ED1ng v\xE0 ki\u1EC3m tra t\u1EADp tin c\u1EA5u h\xECnh CKFinder.',
	501 : 'Ch\u1EE9c n\u0103ng h\u1ED7 tr\u1EE3 \u1EA3nh m\u1EABu b\u1ECB v\xF4 hi\u1EC7u.'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: 'Kh\xF4ng th\u1EC3 \u0111\u1EC3 tr\u1ED1ng t\xEAn t\u1EADp tin.',
		FileExists		: 'T\u1EADp tin %s \u0111\xE3 t\u1ED3n t\u1EA1i.',
		FolderEmpty		: 'Kh\xF4ng th\u1EC3 \u0111\u1EC3 tr\u1ED1ng t\xEAn th\u01B0 m\u1EE5c.',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: 'T\xEAn t\u1EADp tin kh\xF4ng th\u1EC3 ch\u01B0a c\xE1c k\xED t\u1EF1: \n\\ / : * ? " < > |',
		FolderInvChar	: 'T\xEAn th\u01B0 m\u1EE5c kh\xF4ng th\u1EC3 ch\u1EE9a c\xE1c k\xED t\u1EF1: \n\\ / : * ? " < > |',

		PopupBlockView	: 'Kh\xF4ng th\u1EC3 m\u1EDF t\u1EADp tin trong c\u1EEDa s\u1ED5 m\u1EDBi. H\xE3y ki\u1EC3m tra tr\xECnh duy\u1EC7t v\xE0 t\u1EAFt ch\u1EE9c n\u0103ng ch\u1EB7n popup tr\xEAn trang web n\xE0y.',
		XmlError		: 'Kh\xF4ng th\u1EC3 n\u1EA1p h\u1ED3i \u0111\xE1p XML t\u1EEB m\xE1y ch\u1EE7 web.',
		XmlEmpty		: 'Kh\xF4ng th\u1EC3 n\u1EA1p h\u1ED3i \u0111\xE1p XML t\u1EEB m\xE1y ch\u1EE7 web. D\u1EEF li\u1EC7u r\u1ED7ng.',
		XmlRawResponse	: 'H\u1ED3i \u0111\xE1p th\xF4 t\u1EEB m\xE1y ch\u1EE7: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: '\u0110\u1ED5i k\xEDch th\u01B0\u1EDBc %s',
		sizeTooBig		: 'Kh\xF4ng th\u1EC3 \u0111\u1EB7t chi\u1EC1u cao ho\u1EB7c r\u1ED9ng to h\u01A1n k\xEDch th\u01B0\u1EDBc g\u1ED1c (%size).',
		resizeSuccess	: '\u0110\u1ED5i k\xEDch th\u01B0\u1EDBc \u1EA3nh th\xE0nh c\xF4ng.',
		thumbnailNew	: 'T\u1EA1o \u1EA3nh m\u1EABu m\u1EDBi',
		thumbnailSmall	: 'Nh\u1ECF (%s)',
		thumbnailMedium	: 'V\u1EEBa (%s)',
		thumbnailLarge	: 'L\u1EDBn (%s)',
		newSize			: 'Ch\u1ECDn k\xEDch th\u01B0\u1EDBc m\u1EDBi',
		width			: 'R\u1ED9ng',
		height			: 'Cao',
		invalidHeight	: 'Chi\u1EC1u cao kh\xF4ng h\u1EE3p l\u1EC7.',
		invalidWidth	: 'Chi\u1EC1u r\u1ED9ng kh\xF4ng h\u1EE3p l\u1EC7.',
		invalidName		: 'T\xEAn t\u1EADp tin kh\xF4ng h\u1EE3p l\u1EC7.',
		newImage		: 'T\u1EA1o \u1EA3nh m\u1EDBi',
		noExtensionChange : 'Kh\xF4ng th\u1EC3 thay \u0111\u1ED5i ph\u1EA7n m\u1EDF r\u1ED9ng.',
		imageSmall		: '\u1EA2nh ngu\u1ED3n qu\xE1 nh\u1ECF.',
		contextMenuName	: '\u0110\u1ED5i k\xEDch th\u01B0\u1EDBc',
		lockRatio		: 'Kho\xE1 t\u1EC9 l\u1EC7',
		resetSize		: '\u0110\u1EB7t l\u1EA1i k\xEDch th\u01B0\u1EDBc'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: 'L\u01B0u',
		fileOpenError	: 'Kh\xF4ng th\u1EC3 m\u1EDF t\u1EADp tin.',
		fileSaveSuccess	: 'L\u01B0u t\u1EADp tin th\xE0nh c\xF4ng.',
		contextMenuName	: 'S\u1EEDa',
		loadingFile		: '\u0110ang t\u1EA3i t\u1EADp tin, xin ch\u1EDD...'
	},

	Maximize :
	{
		maximize : 'C\u1EF1c \u0111\u1EA1i h\xF3a',
		minimize : 'C\u1EF1c ti\u1EC3u h\xF3a'
	},

	Gallery :
	{
		current : 'H\xECnh th\u1EE9 {current} tr\xEAn {total}'
	},

	Zip :
	{
		extractHereLabel	: 'Extract here', // MISSING
		extractToLabel		: 'Extract to...', // MISSING
		downloadZipLabel	: 'Download as zip', // MISSING
		compressZipLabel	: 'Compress to zip', // MISSING
		removeAndExtract	: 'Remove existing and extract', // MISSING
		extractAndOverwrite	: 'Extract overwriting existing files', // MISSING
		extractSuccess		: 'File extracted successfully.' // MISSING
	},

	Search :
	{
		searchPlaceholder : 'T\xECm ki\u1EBFm'
	}
};
