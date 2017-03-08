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
 * @fileOverview Defines the {@link CKFinder.lang} object for the Chinese-Simplified
 *		language.
 */

/**
 * Contains the dictionary of language entries.
 * @namespace
 */
CKFinder.lang['zh-cn'] =
{
	appTitle : 'CKFinder',

	// Common messages and labels.
	common :
	{
		// Put the voice-only part of the label in the span.
		unavailable		: '%1<span class="cke_accessibility">, \u4E0D\u53EF\u7528</span>',
		confirmCancel	: '\u90E8\u5206\u5185\u5BB9\u5C1A\u672A\u4FDD\u5B58\uFF0C\u786E\u5B9A\u5173\u95ED\u5BF9\u8BDD\u6846\u4E48?',
		ok				: '\u786E\u5B9A',
		cancel			: '\u53D6\u6D88',
		confirmationTitle	: '\u786E\u8BA4',
		messageTitle	: '\u63D0\u793A',
		inputTitle		: '\u8BE2\u95EE',
		undo			: '\u64A4\u9500',
		redo			: '\u91CD\u505A',
		skip			: '\u8DF3\u8FC7',
		skipAll			: '\u5168\u90E8\u8DF3\u8FC7',
		makeDecision	: '\u5E94\u91C7\u53D6\u4F55\u6837\u63AA\u65BD?',
		rememberDecision: '\u4E0B\u6B21\u4E0D\u518D\u8BE2\u95EE'
	},


	// Language direction, 'ltr' or 'rtl'.
	dir : 'ltr',
	HelpLang : 'en',
	LangCode : 'zh-cn',

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
	DateTime : 'yyyy\u5E74m\u6708d\u65E5 h:MM aa',
	DateAmPm : ['AM', 'PM'],

	// Folders
	FoldersTitle	: '\u6587\u4EF6\u5939',
	FolderLoading	: '\u6B63\u5728\u52A0\u8F7D\u6587\u4EF6\u5939...',
	FolderNew		: '\u8BF7\u8F93\u5165\u65B0\u6587\u4EF6\u5939\u540D\u79F0: ',
	FolderRename	: '\u8BF7\u8F93\u5165\u65B0\u6587\u4EF6\u5939\u540D\u79F0: ',
	FolderDelete	: '\u60A8\u786E\u5B9A\u8981\u5220\u9664\u6587\u4EF6\u5939 "%1" \u5417?',
	FolderRenaming	: ' (\u6B63\u5728\u91CD\u547D\u540D...)',
	FolderDeleting	: ' (\u6B63\u5728\u5220\u9664...)',
	DestinationFolder	: 'Destination Folder', // MISSING

	// Files
	FileRename		: '\u8BF7\u8F93\u5165\u65B0\u6587\u4EF6\u540D: ',
	FileRenameExt	: '\u5982\u679C\u6539\u53D8\u6587\u4EF6\u6269\u5C55\u540D\uFF0C\u53EF\u80FD\u4F1A\u5BFC\u81F4\u6587\u4EF6\u4E0D\u53EF\u7528\u3002\r\n\u786E\u5B9A\u8981\u66F4\u6539\u5417\uFF1F',
	FileRenaming	: '\u6B63\u5728\u91CD\u547D\u540D...',
	FileDelete		: '\u60A8\u786E\u5B9A\u8981\u5220\u9664\u6587\u4EF6 "%1" \u5417?',
	FilesDelete	: 'Are you sure you want to delete %1 files?', // MISSING
	FilesLoading	: '\u52A0\u8F7D\u4E2D...',
	FilesEmpty		: '\u7A7A\u6587\u4EF6\u5939',
	DestinationFile	: 'Destination File', // MISSING
	SkippedFiles	: 'List of skipped files:', // MISSING

	// Basket
	BasketFolder		: '\u4E34\u65F6\u6587\u4EF6\u5939',
	BasketClear			: '\u6E05\u7A7A\u4E34\u65F6\u6587\u4EF6\u5939',
	BasketRemove		: '\u4ECE\u4E34\u65F6\u6587\u4EF6\u5939\u79FB\u9664',
	BasketOpenFolder	: '\u6253\u5F00\u4E34\u65F6\u6587\u4EF6\u5939',
	BasketTruncateConfirm : '\u786E\u8BA4\u6E05\u7A7A\u4E34\u65F6\u6587\u4EF6\u5939?',
	BasketRemoveConfirm	: '\u786E\u8BA4\u4ECE\u4E34\u65F6\u6587\u4EF6\u5939\u4E2D\u79FB\u9664\u6587\u4EF6 "%1"\uFF1F',
	BasketRemoveConfirmMultiple	: 'Do you really want to remove %1 files from the basket?', // MISSING
	BasketEmpty			: '\u4E34\u65F6\u6587\u4EF6\u5939\u4E3A\u7A7A, \u53EF\u62D6\u653E\u6587\u4EF6\u81F3\u5176\u4E2D\u3002',
	BasketCopyFilesHere	: '\u4ECE\u4E34\u65F6\u6587\u4EF6\u5939\u590D\u5236\u81F3\u6B64',
	BasketMoveFilesHere	: '\u4ECE\u4E34\u65F6\u6587\u4EF6\u5939\u79FB\u52A8\u81F3\u6B64',

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
	Upload		: '\u4E0A\u4F20',
	UploadTip	: '\u4E0A\u4F20\u6587\u4EF6',
	Refresh		: '\u5237\u65B0',
	Settings	: '\u8BBE\u7F6E',
	Help		: '\u5E2E\u52A9',
	HelpTip		: '\u67E5\u770B\u5728\u7EBF\u5E2E\u52A9',

	// Context Menus
	Select			: '\u9009\u62E9',
	SelectThumbnail : '\u9009\u4E2D\u7F29\u7565\u56FE',
	View			: '\u67E5\u770B',
	Download		: '\u4E0B\u8F7D',

	NewSubFolder	: '\u521B\u5EFA\u5B50\u6587\u4EF6\u5939',
	Rename			: '\u91CD\u547D\u540D',
	Delete			: '\u5220\u9664',
	DeleteFiles		: 'Delete Files', // MISSING

	CopyDragDrop	: '\u5C06\u6587\u4EF6\u590D\u5236\u81F3\u6B64',
	MoveDragDrop	: '\u5C06\u6587\u4EF6\u79FB\u52A8\u81F3\u6B64',

	// Dialogs
	RenameDlgTitle		: '\u91CD\u547D\u540D',
	NewNameDlgTitle		: '\u6587\u4EF6\u540D',
	FileExistsDlgTitle	: '\u6587\u4EF6\u5DF2\u5B58\u5728',
	SysErrorDlgTitle : '\u7CFB\u7EDF\u9519\u8BEF',

	FileOverwrite	: '\u81EA\u52A8\u8986\u76D6\u91CD\u540D\u6587\u4EF6',
	FileAutorename	: '\u7ED9\u91CD\u540D\u6587\u4EF6\u81EA\u52A8\u547D\u540D',
	ManuallyRename	: 'Manually rename', // MISSING

	// Generic
	OkBtn		: '\u786E\u5B9A',
	CancelBtn	: '\u53D6\u6D88',
	CloseBtn	: '\u5173\u95ED',

	// Upload Panel
	UploadTitle			: '\u4E0A\u4F20\u6587\u4EF6',
	UploadSelectLbl		: '\u9009\u5B9A\u8981\u4E0A\u4F20\u7684\u6587\u4EF6',
	UploadProgressLbl	: '(\u6B63\u5728\u4E0A\u4F20\u6587\u4EF6\uFF0C\u8BF7\u7A0D\u5019...)',
	UploadBtn			: '\u4E0A\u4F20\u9009\u5B9A\u7684\u6587\u4EF6',
	UploadBtnCancel		: '\u53D6\u6D88',

	UploadNoFileMsg		: '\u8BF7\u9009\u62E9\u4E00\u4E2A\u8981\u4E0A\u4F20\u7684\u6587\u4EF6',
	UploadNoFolder		: '\u9700\u5148\u9009\u62E9\u4E00\u4E2A\u6587\u4EF6\u3002',
	UploadNoPerms		: '\u65E0\u6587\u4EF6\u4E0A\u4F20\u6743\u9650\u3002',
	UploadUnknError		: '\u4E0A\u4F20\u6587\u4EF6\u51FA\u9519\u3002',
	UploadExtIncorrect	: '\u6B64\u6587\u4EF6\u540E\u7F00\u5728\u5F53\u524D\u6587\u4EF6\u5939\u4E2D\u4E0D\u53EF\u7528\u3002',

	// Flash Uploads
	UploadLabel			: '\u4E0A\u4F20\u6587\u4EF6',
	UploadTotalFiles	: '\u4E0A\u4F20\u603B\u8BA1:',
	UploadTotalSize		: '\u4E0A\u4F20\u603B\u5927\u5C0F:',
	UploadSend			: '\u4E0A\u4F20',
	UploadAddFiles		: '\u6DFB\u52A0\u6587\u4EF6',
	UploadClearFiles	: '\u6E05\u7A7A\u6587\u4EF6',
	UploadCancel		: '\u53D6\u6D88\u4E0A\u4F20',
	UploadRemove		: '\u5220\u9664',
	UploadRemoveTip		: '\u5DF2\u5220\u9664!f',
	UploadUploaded		: '\u5DF2\u4E0A\u4F20!n%',
	UploadProcessing	: '\u4E0A\u4F20\u4E2D...',

	// Settings Panel
	SetTitle		: '\u8BBE\u7F6E',
	SetView			: '\u67E5\u770B:',
	SetViewThumb	: '\u7F29\u7565\u56FE',
	SetViewList		: '\u5217\u8868',
	SetDisplay		: '\u663E\u793A:',
	SetDisplayName	: '\u6587\u4EF6\u540D',
	SetDisplayDate	: '\u65E5\u671F',
	SetDisplaySize	: '\u5927\u5C0F',
	SetSort			: '\u6392\u5217\u987A\u5E8F:',
	SetSortName		: '\u6309\u6587\u4EF6\u540D',
	SetSortDate		: '\u6309\u65E5\u671F',
	SetSortSize		: '\u6309\u5927\u5C0F',
	SetSortExtension		: '\u6309\u6269\u5C55\u540D',

	// Status Bar
	FilesCountEmpty : '<\u7A7A\u6587\u4EF6\u5939>',
	FilesCountOne	: '1 \u4E2A\u6587\u4EF6',
	FilesCountMany	: '%1 \u4E2A\u6587\u4EF6',

	// Size and Speed
	Kb				: '%1 KB',
	Mb				: '%1 MB',
	Gb				: '%1 GB',
	SizePerSecond	: '%1/s',

	// Connector Error Messages.
	ErrorUnknown	: '\u8BF7\u6C42\u7684\u64CD\u4F5C\u672A\u80FD\u5B8C\u6210. (\u9519\u8BEF %1)',
	Errors :
	{
	 10 : '\u65E0\u6548\u7684\u6307\u4EE4\u3002',
	 11 : '\u6587\u4EF6\u7C7B\u578B\u4E0D\u5728\u8BB8\u53EF\u8303\u56F4\u4E4B\u5185\u3002',
	 12 : '\u6587\u4EF6\u7C7B\u578B\u65E0\u6548\u3002',
	102 : '\u65E0\u6548\u7684\u6587\u4EF6\u540D\u6216\u6587\u4EF6\u5939\u540D\u79F0\u3002',
	103 : '\u7531\u4E8E\u4F5C\u8005\u9650\u5236\uFF0C\u8BE5\u8BF7\u6C42\u4E0D\u80FD\u5B8C\u6210\u3002',
	104 : '\u7531\u4E8E\u6587\u4EF6\u7CFB\u7EDF\u7684\u9650\u5236\uFF0C\u8BE5\u8BF7\u6C42\u4E0D\u80FD\u5B8C\u6210\u3002',
	105 : '\u65E0\u6548\u7684\u6269\u5C55\u540D\u3002',
	109 : '\u65E0\u6548\u8BF7\u6C42\u3002',
	110 : '\u672A\u77E5\u9519\u8BEF\u3002',
	111 : 'It was not possible to complete the request due to resulting file size.', // MISSING
	115 : '\u5B58\u5728\u91CD\u540D\u7684\u6587\u4EF6\u6216\u6587\u4EF6\u5939\u3002',
	116 : '\u6587\u4EF6\u5939\u4E0D\u5B58\u5728. \u8BF7\u5237\u65B0\u540E\u518D\u8BD5\u3002',
	117 : '\u6587\u4EF6\u4E0D\u5B58\u5728. \u8BF7\u5237\u65B0\u5217\u8868\u540E\u518D\u8BD5\u3002',
	118 : '\u76EE\u6807\u4F4D\u7F6E\u4E0E\u5F53\u524D\u4F4D\u7F6E\u76F8\u540C\u3002',
	201 : '\u6587\u4EF6\u4E0E\u73B0\u6709\u7684\u91CD\u540D. \u65B0\u4E0A\u4F20\u7684\u6587\u4EF6\u6539\u540D\u4E3A "%1"\u3002',
	202 : '\u65E0\u6548\u7684\u6587\u4EF6\u3002',
	203 : '\u65E0\u6548\u7684\u6587\u4EF6. \u6587\u4EF6\u5C3A\u5BF8\u592A\u5927\u3002',
	204 : '\u4E0A\u4F20\u6587\u4EF6\u5DF2\u635F\u5931\u3002',
	205 : '\u670D\u52A1\u5668\u4E2D\u7684\u4E0A\u4F20\u4E34\u65F6\u6587\u4EF6\u5939\u65E0\u6548\u3002',
	206 : '\u56E0\u4E3A\u5B89\u5168\u539F\u56E0\uFF0C\u4E0A\u4F20\u4E2D\u65AD. \u4E0A\u4F20\u6587\u4EF6\u5305\u542B\u4E0D\u80FD HTML \u7C7B\u578B\u6570\u636E\u3002',
	207 : '\u65B0\u4E0A\u4F20\u7684\u6587\u4EF6\u6539\u540D\u4E3A "%1"\u3002',
	300 : '\u79FB\u52A8\u6587\u4EF6\u5931\u8D25\u3002',
	301 : '\u590D\u5236\u6587\u4EF6\u5931\u8D25\u3002',
	500 : '\u56E0\u4E3A\u5B89\u5168\u539F\u56E0\uFF0C\u6587\u4EF6\u4E0D\u53EF\u6D4F\u89C8. \u8BF7\u8054\u7CFB\u7CFB\u7EDF\u7BA1\u7406\u5458\u5E76\u68C0\u67E5CKFinder\u914D\u7F6E\u6587\u4EF6\u3002',
	501 : '\u4E0D\u652F\u6301\u7F29\u7565\u56FE\u65B9\u5F0F\u3002'
	},

	// Other Error Messages.
	ErrorMsg :
	{
		FileEmpty		: '\u6587\u4EF6\u540D\u4E0D\u80FD\u4E3A\u7A7A\u3002',
		FileExists		: '\u6587\u4EF6 %s \u5DF2\u5B58\u5728\u3002',
		FolderEmpty		: '\u6587\u4EF6\u5939\u540D\u79F0\u4E0D\u80FD\u4E3A\u7A7A\u3002',
		FolderExists	: 'Folder %s already exists.', // MISSING
		FolderNameExists	: 'Folder already exists.', // MISSING

		FileInvChar		: '\u6587\u4EF6\u540D\u4E0D\u80FD\u5305\u542B\u4EE5\u4E0B\u5B57\u7B26: \n\\ / : * ? " < > |',
		FolderInvChar	: '\u6587\u4EF6\u5939\u540D\u79F0\u4E0D\u80FD\u5305\u542B\u4EE5\u4E0B\u5B57\u7B26: \n\\ / : * ? " < > |',

		PopupBlockView	: '\u672A\u80FD\u5728\u65B0\u7A97\u53E3\u4E2D\u6253\u5F00\u6587\u4EF6. \u8BF7\u4FEE\u6539\u6D4F\u89C8\u5668\u914D\u7F6E\u89E3\u9664\u5BF9\u672C\u7AD9\u70B9\u7684\u9501\u5B9A\u3002',
		XmlError		: '\u4ECE\u670D\u52A1\u5668\u8BFB\u53D6XML\u6570\u636E\u51FA\u9519',
		XmlEmpty		: '\u65E0\u6CD5\u4ECE\u670D\u52A1\u5668\u8BFB\u53D6\u6570\u636E\uFF0C\u56E0XML\u54CD\u5E94\u8FD4\u56DE\u7ED3\u679C\u4E3A\u7A7A',
		XmlRawResponse	: '\u670D\u52A1\u5668\u8FD4\u56DE\u539F\u59CB\u7ED3\u679C: %s'
	},

	// Imageresize plugin
	Imageresize :
	{
		dialogTitle		: '\u6539\u53D8\u5C3A\u5BF8 %s',
		sizeTooBig		: '\u65E0\u6CD5\u5927\u4E8E\u539F\u56FE\u5C3A\u5BF8 (%size)\u3002',
		resizeSuccess	: '\u56FE\u50CF\u5C3A\u5BF8\u5DF2\u4FEE\u6539\u3002',
		thumbnailNew	: '\u521B\u5EFA\u7F29\u7565\u56FE',
		thumbnailSmall	: '\u5C0F (%s)',
		thumbnailMedium	: '\u4E2D (%s)',
		thumbnailLarge	: '\u5927 (%s)',
		newSize			: '\u8BBE\u7F6E\u65B0\u5C3A\u5BF8',
		width			: '\u5BBD\u5EA6',
		height			: '\u9AD8\u5EA6',
		invalidHeight	: '\u65E0\u6548\u9AD8\u5EA6\u3002',
		invalidWidth	: '\u65E0\u6548\u5BBD\u5EA6\u3002',
		invalidName		: '\u6587\u4EF6\u540D\u65E0\u6548\u3002',
		newImage		: '\u521B\u5EFA\u56FE\u50CF',
		noExtensionChange : '\u65E0\u6CD5\u6539\u53D8\u6587\u4EF6\u540E\u7F00\u3002',
		imageSmall		: '\u539F\u6587\u4EF6\u5C3A\u5BF8\u8FC7\u5C0F',
		contextMenuName	: '\u6539\u53D8\u5C3A\u5BF8',
		lockRatio		: '\u9501\u5B9A\u6BD4\u4F8B',
		resetSize		: '\u539F\u59CB\u5C3A\u5BF8'
	},

	// Fileeditor plugin
	Fileeditor :
	{
		save			: '\u4FDD\u5B58',
		fileOpenError	: '\u65E0\u6CD5\u6253\u5F00\u6587\u4EF6\u3002',
		fileSaveSuccess	: '\u6210\u529F\u4FDD\u5B58\u6587\u4EF6\u3002',
		contextMenuName	: '\u7F16\u8F91',
		loadingFile		: '\u52A0\u8F7D\u6587\u4EF6\u4E2D...'
	},

	Maximize :
	{
		maximize : '\u5168\u5C4F',
		minimize : '\u6700\u5C0F\u5316'
	},

	Gallery :
	{
		current : '\u7B2C {current} \u4E2A\u56FE\u50CF\uFF0C\u5171 {total} \u4E2A'
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
		searchPlaceholder : '\u641C\u7D22'
	}
};
