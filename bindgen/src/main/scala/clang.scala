package scala.scalanative.native


@link("clang")
@extern
object clang {

  type CXIndex = Ptr[_]
  type CXTranslationUnit = Ptr[_]

  @struct
  class CXCursor(
    val kind: CXCursorKind.enum,
		val xdata: CInt,
    val data1: Ptr[_],
    val data2: Ptr[_],
    val data3: Ptr[_]
  )

  @struct
  class CXUnsavedFile(
    val filename: CString,
    val contents: CString,
    val length: ULong
  )


  def clang_createIndex(excludeDeclarationsFromPCH: CInt, displayDiagnostics: CInt): CXIndex = extern

  def	clang_parseTranslationUnit(cIdx: CXIndex,
                                 source_filename: CString,
                                 clang_command_line_args: Ptr[CString],
                                 num_clang_command_line_args: CInt,
                                 unsaved_files: Ptr[CXUnsavedFile],
                                 num_unsaved_files: UInt,
                                 options: UInt) = extern

  def clang_getTranslationUnitCursor(translation_unit: CXTranslationUnit): CXCursor = extern


  object CXTranslationUnit_Flags {
    type enum = UInt
   
     /**
    	* \brief Used to indicate that no special translation-unit options are
    	* needed.
    	*/
     final val CXTranslationUnit_None : CXTranslationUnit_Flags.enum = 0x0.toUInt
    
     /**
    	* \brief Used to indicate that the parser should construct a "detailed"
    	* preprocessing record, including all macro definitions and instantiations.
    	*
    	* Constructing a detailed preprocessing record requires more memory
    	* and time to parse, since the information contained in the record
    	* is usually not retained. However, it can be useful for
    	* applications that require more detailed information about the
    	* behavior of the preprocessor.
    	*/
    final val CXTranslationUnit_DetailedPreprocessingRecord : CXTranslationUnit_Flags.enum = 0x01.toUInt
    
     /**
    	* \brief Used to indicate that the translation unit is incomplete.
    	*
    	* When a translation unit is considered "incomplete", semantic
    	* analysis that is typically performed at the end of the
    	* translation unit will be suppressed. For example, this suppresses
    	* the completion of tentative declarations in C and of
    	* instantiation of implicitly-instantiation function templates in
    	* C++. This option is typically used when parsing a header with the
    	* intent of producing a precompiled header.
    	*/
    final val CXTranslationUnit_Incomplete : CXTranslationUnit_Flags.enum = 0x02.toUInt
     
     /**
    	* \brief Used to indicate that the translation unit should be built with an 
    	* implicit precompiled header for the preamble.
    	*
    	* An implicit precompiled header is used as an optimization when a
    	* particular translation unit is likely to be reparsed many times
    	* when the sources aren't changing that often. In this case, an
    	* implicit precompiled header will be built containing all of the
    	* initial includes at the top of the main file (what we refer to as
    	* the "preamble" of the file). In subsequent parses, if the
    	* preamble or the files in it have not changed, \c
    	* clang_reparseTranslationUnit() will re-use the implicit
    	* precompiled header to improve parsing performance.
    	*/
    final val CXTranslationUnit_PrecompiledPreamble : CXTranslationUnit_Flags.enum = 0x04.toUInt
     
     /**
    	* \brief Used to indicate that the translation unit should cache some
    	* code-completion results with each reparse of the source file.
    	*
    	* Caching of code-completion results is a performance optimization that
    	* introduces some overhead to reparsing but improves the performance of
    	* code-completion operations.
    	*/
    final val CXTranslationUnit_CacheCompletionResults : CXTranslationUnit_Flags.enum = 0x08.toUInt
    
     /**
    	* \brief Used to indicate that the translation unit will be serialized with
    	* \c clang_saveTranslationUnit.
    	*
    	* This option is typically used when parsing a header with the intent of
    	* producing a precompiled header.
    	*/
    final val CXTranslationUnit_ForSerialization : CXTranslationUnit_Flags.enum = 0x10.toUInt
    
     /**
    	* \brief DEPRECATED: Enabled chained precompiled preambles in C++.
    	*
    	* Note: this is a *temporary* option that is available only while
    	* we are testing C++ precompiled preamble support. It is deprecated.
    	*/
    final val CXTranslationUnit_CXXChainedPCH : CXTranslationUnit_Flags.enum = 0x20.toUInt
    
     /**
    	* \brief Used to indicate that function/method bodies should be skipped while
    	* parsing.
    	*
    	* This option can be used to search for declarations/definitions while
    	* ignoring the usages.
    	*/
    final val CXTranslationUnit_SkipFunctionBodies : CXTranslationUnit_Flags.enum = 0x40.toUInt
    
     /**
    	* \brief Used to indicate that brief documentation comments should be
    	* included into the set of code completions returned from this translation
    	* unit.
    	*/
    final val CXTranslationUnit_IncludeBriefCommentsInCodeCompletion : CXTranslationUnit_Flags.enum = 0x80.toUInt
    
     /**
    	* \brief Used to indicate that the precompiled preamble should be created on
    	* the first parse. Otherwise it will be created on the first reparse. This
    	* trades runtime on the first parse (serializing the preamble takes time) for
    	* reduced runtime on the second parse (can now reuse the preamble).
    	*/
    final val CXTranslationUnit_CreatePreambleOnFirstParse : CXTranslationUnit_Flags.enum = 0x100.toUInt
    
     /**
    	* \brief Do not stop processing when fatal errors are encountered.
    	*
    	* When fatal errors are encountered while parsing a translation unit,
    	* semantic analysis is typically stopped early when compiling code. A common
    	* source for fatal errors are unresolvable include files. For the
    	* purposes of an IDE, this is undesirable behavior and as much information
    	* as possible should be reported. Use this flag to enable this behavior.
    	*/
    final val CXTranslationUnit_KeepGoing : CXTranslationUnit_Flags.enum = 0x200.toUInt
  }


  object CXCursorKind {
    type enum = Int
  
    /**
   	* \brief A declaration whose specific kind is not exposed via this
   	* interface.
   	*
   	* Unexposed declarations have the same operations as any other kind
   	* of declaration; one can extract their location information,
   	* spelling, find their definitions, etc. However, the specific kind
   	* of the declaration is not reported.
   	*/
    final val CXCursor_UnexposedDecl									: CXCursorKind.enum = 1
    /** \brief A C or C++ struct. */
    final val CXCursor_StructDecl										: CXCursorKind.enum = 2
    /** \brief A C or C++ union. */
    final val CXCursor_UnionDecl											: CXCursorKind.enum = 3
    /** \brief A C++ class. */
    final val CXCursor_ClassDecl											: CXCursorKind.enum = 4
    /** \brief An enumeration. */
    final val CXCursor_EnumDecl											: CXCursorKind.enum = 5
    /**
   	* \brief A field (in C) or non-static data member (in C++) in a
   	* struct, union, or C++ class.
   	*/
    final val CXCursor_FieldDecl											: CXCursorKind.enum = 6
    /** \brief An enumerator constant. */
    final val CXCursor_EnumConstantDecl							: CXCursorKind.enum = 7
    /** \brief A function. */
    final val CXCursor_FunctionDecl									: CXCursorKind.enum = 8
    /** \brief A variable. */
    final val CXCursor_VarDecl												: CXCursorKind.enum = 9
    /** \brief A function or method parameter. */
    final val CXCursor_ParmDecl											: CXCursorKind.enum = 10
    /** \brief An Objective-C \@interface. */
    final val CXCursor_ObjCInterfaceDecl							: CXCursorKind.enum = 11
    /** \brief An Objective-C \@interface for a category. */
    final val CXCursor_ObjCCategoryDecl							: CXCursorKind.enum = 12
    /** \brief An Objective-C \@protocol declaration. */
    final val CXCursor_ObjCProtocolDecl							: CXCursorKind.enum = 13
    /** \brief An Objective-C \@property declaration. */
    final val CXCursor_ObjCPropertyDecl							: CXCursorKind.enum = 14
    /** \brief An Objective-C instance variable. */
    final val CXCursor_ObjCIvarDecl									: CXCursorKind.enum = 15
    /** \brief An Objective-C instance method. */
    final val CXCursor_ObjCInstanceMethodDecl				: CXCursorKind.enum = 16
    /** \brief An Objective-C class method. */
    final val CXCursor_ObjCClassMethodDecl						: CXCursorKind.enum = 17
    /** \brief An Objective-C \@implementation. */
    final val CXCursor_ObjCImplementationDecl				: CXCursorKind.enum = 18
    /** \brief An Objective-C \@implementation for a category. */
    final val CXCursor_ObjCCategoryImplDecl					: CXCursorKind.enum = 19
    /** \brief A typedef. */
    final val CXCursor_TypedefDecl										: CXCursorKind.enum = 20
    /** \brief A C++ class method. */
    final val CXCursor_CXXMethod											: CXCursorKind.enum = 21
    /** \brief A C++ namespace. */
    final val CXCursor_Namespace											: CXCursorKind.enum = 22
    /** \brief A linkage specification, e.g. 'extern "C"'. */
    final val CXCursor_LinkageSpec										: CXCursorKind.enum = 23
    /** \brief A C++ constructor. */
    final val CXCursor_Constructor										: CXCursorKind.enum = 24
    /** \brief A C++ destructor. */
    final val CXCursor_Destructor										: CXCursorKind.enum = 25
    /** \brief A C++ conversion function. */
    final val CXCursor_ConversionFunction						: CXCursorKind.enum = 26
    /** \brief A C++ template type parameter. */
    final val CXCursor_TemplateTypeParameter					: CXCursorKind.enum = 27
    /** \brief A C++ non-type template parameter. */
    final val CXCursor_NonTypeTemplateParameter			: CXCursorKind.enum = 28
    /** \brief A C++ template template parameter. */
    final val CXCursor_TemplateTemplateParameter			: CXCursorKind.enum = 29
    /** \brief A C++ function template. */
    final val CXCursor_FunctionTemplate							: CXCursorKind.enum = 30
    /** \brief A C++ class template. */
    final val CXCursor_ClassTemplate									: CXCursorKind.enum = 31
    /** \brief A C++ class template partial specialization. */
    final val CXCursor_ClassTemplatePartialSpecialization : CXCursorKind.enum = 32
    /** \brief A C++ namespace alias declaration. */
    final val CXCursor_NamespaceAlias								: CXCursorKind.enum = 33
    /** \brief A C++ using directive. */
    final val CXCursor_UsingDirective								: CXCursorKind.enum = 34
    /** \brief A C++ using declaration. */
    final val CXCursor_UsingDeclaration							: CXCursorKind.enum = 35
    /** \brief A C++ alias declaration */
    final val CXCursor_TypeAliasDecl									: CXCursorKind.enum = 36
    /** \brief An Objective-C \@synthesize definition. */
    final val CXCursor_ObjCSynthesizeDecl						: CXCursorKind.enum = 37
    /** \brief An Objective-C \@dynamic definition. */
    final val CXCursor_ObjCDynamicDecl								: CXCursorKind.enum = 38
    /** \brief An access specifier. */
    final val CXCursor_CXXAccessSpecifier						: CXCursorKind.enum = 39
  
    final val CXCursor_FirstDecl											: CXCursorKind.enum = CXCursor_UnexposedDecl
    final val CXCursor_LastDecl											: CXCursorKind.enum = CXCursor_CXXAccessSpecifier
  
    /* References */
    final val CXCursor_FirstRef											: CXCursorKind.enum = 40 /* Decl references */
    final val CXCursor_ObjCSuperClassRef							: CXCursorKind.enum = 40
    final val CXCursor_ObjCProtocolRef								: CXCursorKind.enum = 41
    final val CXCursor_ObjCClassRef									: CXCursorKind.enum = 42
    /**
   	* \brief A reference to a type declaration.
   	*
   	* A type reference occurs anywhere where a type is named but not
   	* declared. For example, given:
   	*
   	* \code
   	* typedef unsigned size_type;
   	* size_type size;
   	* \endcode
   	*
   	* The typedef is a declaration of size_type (CXCursor_TypedefDecl),
   	* while the type of the variable "size" is referenced. The cursor
   	* referenced by the type of size is the typedef for size_type.
   	*/
    final val CXCursor_TypeRef												: CXCursorKind.enum = 43
    final val CXCursor_CXXBaseSpecifier							: CXCursorKind.enum = 44
    /** 
   	* \brief A reference to a class template, function template, template
   	* template parameter, or class template partial specialization.
   	*/
    final val CXCursor_TemplateRef										: CXCursorKind.enum = 45
    /**
   	* \brief A reference to a namespace or namespace alias.
   	*/
    final val CXCursor_NamespaceRef									: CXCursorKind.enum = 46
    /**
   	* \brief A reference to a member of a struct, union, or class that occurs in 
   	* some non-expression context, e.g., a designated initializer.
   	*/
    final val CXCursor_MemberRef											: CXCursorKind.enum = 47
    /**
   	* \brief A reference to a labeled statement.
   	*
   	* This cursor kind is used to describe the jump to "start_over" in the 
   	* goto statement in the following example:
   	*
   	* \code
   	*		start_over:
   	*			++counter;
   	*
   	*			goto start_over;
   	* \endcode
   	*
   	* A label reference cursor refers to a label statement.
   	*/
    final val CXCursor_LabelRef											: CXCursorKind.enum = 48
    
    /**
   	* \brief A reference to a set of overloaded functions or function templates
   	* that has not yet been resolved to a specific function or function template.
   	*
   	* An overloaded declaration reference cursor occurs in C++ templates where
   	* a dependent name refers to a function. For example:
   	*
   	* \code
   	* template<typename T> void swap(T&, T&);
   	*
   	* struct X { ... };
   	* void swap(X&, X&);
   	*
   	* template<typename T>
   	* void reverse(T* first, T* last) {
   	*		while (first < last - 1) {
   	*			swap(*first, *--last);
   	*			++first;
   	*		}
   	* }
   	*
   	* struct Y { };
   	* void swap(Y&, Y&);
   	* \endcode
   	*
   	* Here, the identifier "swap" is associated with an overloaded declaration
   	* reference. In the template definition, "swap" refers to either of the two
   	* "swap" functions declared above, so both results will be available. At
   	* instantiation time, "swap" may also refer to other functions found via
   	* argument-dependent lookup (e.g., the "swap" function at the end of the
   	* example).
   	*
   	* The functions \c clang_getNumOverloadedDecls() and 
   	* \c clang_getOverloadedDecl() can be used to retrieve the definitions
   	* referenced by this cursor.
   	*/
    final val CXCursor_OverloadedDeclRef							: CXCursorKind.enum = 49
    
    /**
   	* \brief A reference to a variable that occurs in some non-expression 
   	* context, e.g., a C++ lambda capture list.
   	*/
    final val CXCursor_VariableRef										: CXCursorKind.enum = 50
    
    final val CXCursor_LastRef												: CXCursorKind.enum = CXCursor_VariableRef
  
    /* Error conditions */
    final val CXCursor_FirstInvalid									: CXCursorKind.enum = 70
    final val CXCursor_InvalidFile										: CXCursorKind.enum = 70
    final val CXCursor_NoDeclFound										: CXCursorKind.enum = 71
    final val CXCursor_NotImplemented								: CXCursorKind.enum = 72
    final val CXCursor_InvalidCode										: CXCursorKind.enum = 73
    final val CXCursor_LastInvalid										: CXCursorKind.enum = CXCursor_InvalidCode
  
    /* Expressions */
    final val CXCursor_FirstExpr											: CXCursorKind.enum = 100
  
    /**
   	* \brief An expression whose specific kind is not exposed via this
   	* interface.
   	*
   	* Unexposed expressions have the same operations as any other kind
   	* of expression; one can extract their location information,
   	* spelling, children, etc. However, the specific kind of the
   	* expression is not reported.
   	*/
    final val CXCursor_UnexposedExpr									: CXCursorKind.enum = 100
  
    /**
   	* \brief An expression that refers to some value declaration, such
   	* as a function, variable, or enumerator.
   	*/
    final val CXCursor_DeclRefExpr										: CXCursorKind.enum = 101
  
    /**
   	* \brief An expression that refers to a member of a struct, union,
   	* class, Objective-C class, etc.
   	*/
    final val CXCursor_MemberRefExpr									: CXCursorKind.enum = 102
  
    /** \brief An expression that calls a function. */
    final val CXCursor_CallExpr											: CXCursorKind.enum = 103
  
    /** \brief An expression that sends a message to an Objective-C
   	object or class. */
    final val CXCursor_ObjCMessageExpr								: CXCursorKind.enum = 104
  
    /** \brief An expression that represents a block literal. */
    final val CXCursor_BlockExpr											: CXCursorKind.enum = 105
  
    /** \brief An integer literal.
   	*/
    final val CXCursor_IntegerLiteral								: CXCursorKind.enum = 106
  
    /** \brief A floating point number literal.
   	*/
    final val CXCursor_FloatingLiteral								: CXCursorKind.enum = 107
  
    /** \brief An imaginary number literal.
   	*/
    final val CXCursor_ImaginaryLiteral							: CXCursorKind.enum = 108
  
    /** \brief A string literal.
   	*/
    final val CXCursor_StringLiteral									: CXCursorKind.enum = 109
  
    /** \brief A character literal.
   	*/
    final val CXCursor_CharacterLiteral							: CXCursorKind.enum = 110
  
    /** \brief A parenthesized expression, e.g. "(1)".
   	*
   	* This AST node is only formed if full location information is requested.
   	*/
    final val CXCursor_ParenExpr											: CXCursorKind.enum = 111
  
    /** \brief This represents the unary-expression's (except sizeof and
   	* alignof).
   	*/
    final val CXCursor_UnaryOperator									: CXCursorKind.enum = 112
  
    /** \brief [C99 6.5.2.1] Array Subscripting.
   	*/
    final val CXCursor_ArraySubscriptExpr						: CXCursorKind.enum = 113
  
    /** \brief A builtin binary operation expression such as "x + y" or
   	* "x <= y".
   	*/
    final val CXCursor_BinaryOperator								: CXCursorKind.enum = 114
  
    /** \brief Compound assignment such as "+=".
   	*/
    final val CXCursor_CompoundAssignOperator				: CXCursorKind.enum = 115
  
    /** \brief The ?: ternary operator.
   	*/
    final val CXCursor_ConditionalOperator						: CXCursorKind.enum = 116
  
    /** \brief An explicit cast in C (C99 6.5.4) or a C-style cast in C++
   	* (C++ [expr.cast]), which uses the syntax (Type)expr.
   	*
   	* For example: (int)f.
   	*/
    final val CXCursor_CStyleCastExpr								: CXCursorKind.enum = 117
  
    /** \brief [C99 6.5.2.5]
   	*/
    final val CXCursor_CompoundLiteralExpr						: CXCursorKind.enum = 118
  
    /** \brief Describes an C or C++ initializer list.
   	*/
    final val CXCursor_InitListExpr									: CXCursorKind.enum = 119
  
    /** \brief The GNU address of label extension, representing &&label.
   	*/
    final val CXCursor_AddrLabelExpr									: CXCursorKind.enum = 120
  
    /** \brief This is the GNU Statement Expression extension: ({int X=4; X;})
   	*/
    final val CXCursor_StmtExpr											: CXCursorKind.enum = 121
  
    /** \brief Represents a C11 generic selection.
   	*/
    final val CXCursor_GenericSelectionExpr					: CXCursorKind.enum = 122
  
    /** \brief Implements the GNU __null extension, which is a name for a null
   	* pointer constant that has integral type (e.g., int or long) and is the same
   	* size and alignment as a pointer.
   	*
   	* The __null extension is typically only used by system headers, which define
   	* NULL as __null in C++ rather than using 0 (which is an integer that may not
   	* match the size of a pointer).
   	*/
    final val CXCursor_GNUNullExpr										: CXCursorKind.enum = 123
  
    /** \brief C++'s static_cast<> expression.
   	*/
    final val CXCursor_CXXStaticCastExpr							: CXCursorKind.enum = 124
  
    /** \brief C++'s dynamic_cast<> expression.
   	*/
    final val CXCursor_CXXDynamicCastExpr						: CXCursorKind.enum = 125
  
    /** \brief C++'s reinterpret_cast<> expression.
   	*/
    final val CXCursor_CXXReinterpretCastExpr				: CXCursorKind.enum = 126
  
    /** \brief C++'s const_cast<> expression.
   	*/
    final val CXCursor_CXXConstCastExpr							: CXCursorKind.enum = 127
  
    /** \brief Represents an explicit C++ type conversion that uses "functional"
   	* notion (C++ [expr.type.conv]).
   	*
   	* Example:
   	* \code
   	*		x = int(0.5);
   	* \endcode
   	*/
    final val CXCursor_CXXFunctionalCastExpr					: CXCursorKind.enum = 128
  
    /** \brief A C++ typeid expression (C++ [expr.typeid]).
   	*/
    final val CXCursor_CXXTypeidExpr									: CXCursorKind.enum = 129
  
    /** \brief [C++ 2.13.5] C++ Boolean Literal.
   	*/
    final val CXCursor_CXXBoolLiteralExpr						: CXCursorKind.enum = 130
  
    /** \brief [C++0x 2.14.7] C++ Pointer Literal.
   	*/
    final val CXCursor_CXXNullPtrLiteralExpr					: CXCursorKind.enum = 131
  
    /** \brief Represents the "this" expression in C++
   	*/
    final val CXCursor_CXXThisExpr										: CXCursorKind.enum = 132
  
    /** \brief [C++ 15] C++ Throw Expression.
   	*
   	* This handles 'throw' and 'throw' assignment-expression. When
   	* assignment-expression isn't present, Op will be null.
   	*/
    final val CXCursor_CXXThrowExpr									: CXCursorKind.enum = 133
  
    /** \brief A new expression for memory allocation and constructor calls, e.g:
   	* "new CXXNewExpr(foo)".
   	*/
    final val CXCursor_CXXNewExpr										: CXCursorKind.enum = 134
  
    /** \brief A delete expression for memory deallocation and destructor calls,
   	* e.g. "delete[] pArray".
   	*/
    final val CXCursor_CXXDeleteExpr									: CXCursorKind.enum = 135
  
    /** \brief A unary expression. (noexcept, sizeof, or other traits)
   	*/
    final val CXCursor_UnaryExpr											: CXCursorKind.enum = 136
  
    /** \brief An Objective-C string literal i.e. @"foo".
   	*/
    final val CXCursor_ObjCStringLiteral							: CXCursorKind.enum = 137
  
    /** \brief An Objective-C \@encode expression.
   	*/
    final val CXCursor_ObjCEncodeExpr								: CXCursorKind.enum = 138
  
    /** \brief An Objective-C \@selector expression.
   	*/
    final val CXCursor_ObjCSelectorExpr							: CXCursorKind.enum = 139
  
    /** \brief An Objective-C \@protocol expression.
   	*/
    final val CXCursor_ObjCProtocolExpr							: CXCursorKind.enum = 140
  
    /** \brief An Objective-C "bridged" cast expression, which casts between
   	* Objective-C pointers and C pointers, transferring ownership in the process.
   	*
   	* \code
   	*		NSString *str = (__bridge_transfer NSString *)CFCreateString();
   	* \endcode
   	*/
    final val CXCursor_ObjCBridgedCastExpr						: CXCursorKind.enum = 141
  
    /** \brief Represents a C++0x pack expansion that produces a sequence of
   	* expressions.
   	*
   	* A pack expansion expression contains a pattern (which itself is an
   	* expression) followed by an ellipsis. For example:
   	*
   	* \code
   	* template<typename F, typename ...Types>
   	* void forward(F f, Types &&...args) {
   	*	 f(static_cast<Types&&>(args)...);
   	* }
   	* \endcode
   	*/
    final val CXCursor_PackExpansionExpr							: CXCursorKind.enum = 142
  
    /** \brief Represents an expression that computes the length of a parameter
   	* pack.
   	*
   	* \code
   	* template<typename ...Types>
   	* struct count {
   	*		static const unsigned value = sizeof...(Types);
   	* };
   	* \endcode
   	*/
    final val CXCursor_SizeOfPackExpr								: CXCursorKind.enum = 143
  
    /* \brief Represents a C++ lambda expression that produces a local function
   	* object.
   	*
   	* \code
   	* void abssort(float *x, unsigned N) {
   	*		std::sort(x, x + N,
   	*							[](float a, float b) {
   	*								return std::abs(a) < std::abs(b);
   	*							});
   	* }
   	* \endcode
   	*/
    final val CXCursor_LambdaExpr										: CXCursorKind.enum = 144
    
    /** \brief Objective-c Boolean Literal.
   	*/
    final val CXCursor_ObjCBoolLiteralExpr						: CXCursorKind.enum = 145
  
    /** \brief Represents the "self" expression in an Objective-C method.
   	*/
    final val CXCursor_ObjCSelfExpr									: CXCursorKind.enum = 146
  
    /** \brief OpenMP 4.0 [2.4, Array Section].
   	*/
    final val CXCursor_OMPArraySectionExpr						: CXCursorKind.enum = 147
  
    final val CXCursor_LastExpr											: CXCursorKind.enum = CXCursor_OMPArraySectionExpr
  
    /* Statements */
    final val CXCursor_FirstStmt											: CXCursorKind.enum = 200
    /**
   	* \brief A statement whose specific kind is not exposed via this
   	* interface.
   	*
   	* Unexposed statements have the same operations as any other kind of
   	* statement; one can extract their location information, spelling,
   	* children, etc. However, the specific kind of the statement is not
   	* reported.
   	*/
    final val CXCursor_UnexposedStmt									: CXCursorKind.enum = 200
    
    /** \brief A labelled statement in a function. 
   	*
   	* This cursor kind is used to describe the "start_over:" label statement in 
   	* the following example:
   	*
   	* \code
   	*		start_over:
   	*			++counter;
   	* \endcode
   	*
   	*/
    final val CXCursor_LabelStmt											: CXCursorKind.enum = 201
  
    /** \brief A group of statements like { stmt stmt }.
   	*
   	* This cursor kind is used to describe compound statements, e.g. function
   	* bodies.
   	*/
    final val CXCursor_CompoundStmt									: CXCursorKind.enum = 202
  
    /** \brief A case statement.
   	*/
    final val CXCursor_CaseStmt											: CXCursorKind.enum = 203
  
    /** \brief A default statement.
   	*/
    final val CXCursor_DefaultStmt										: CXCursorKind.enum = 204
  
    /** \brief An if statement
   	*/
    final val CXCursor_IfStmt												: CXCursorKind.enum = 205
  
    /** \brief A switch statement.
   	*/
    final val CXCursor_SwitchStmt										: CXCursorKind.enum = 206
  
    /** \brief A while statement.
   	*/
    final val CXCursor_WhileStmt											: CXCursorKind.enum = 207
  
    /** \brief A do statement.
   	*/
    final val CXCursor_DoStmt												: CXCursorKind.enum = 208
  
    /** \brief A for statement.
   	*/
    final val CXCursor_ForStmt												: CXCursorKind.enum = 209
  
    /** \brief A goto statement.
   	*/
    final val CXCursor_GotoStmt											: CXCursorKind.enum = 210
  
    /** \brief An indirect goto statement.
   	*/
    final val CXCursor_IndirectGotoStmt							: CXCursorKind.enum = 211
  
    /** \brief A continue statement.
   	*/
    final val CXCursor_ContinueStmt									: CXCursorKind.enum = 212
  
    /** \brief A break statement.
   	*/
    final val CXCursor_BreakStmt											: CXCursorKind.enum = 213
  
    /** \brief A return statement.
   	*/
    final val CXCursor_ReturnStmt										: CXCursorKind.enum = 214
  
    /** \brief A GCC inline assembly statement extension.
   	*/
    final val CXCursor_GCCAsmStmt										: CXCursorKind.enum = 215
    final val CXCursor_AsmStmt												: CXCursorKind.enum = CXCursor_GCCAsmStmt
  
    /** \brief Objective-C's overall \@try-\@catch-\@finally statement.
   	*/
    final val CXCursor_ObjCAtTryStmt									: CXCursorKind.enum = 216
  
    /** \brief Objective-C's \@catch statement.
   	*/
    final val CXCursor_ObjCAtCatchStmt								: CXCursorKind.enum = 217
  
    /** \brief Objective-C's \@finally statement.
   	*/
    final val CXCursor_ObjCAtFinallyStmt							: CXCursorKind.enum = 218
  
    /** \brief Objective-C's \@throw statement.
   	*/
    final val CXCursor_ObjCAtThrowStmt								: CXCursorKind.enum = 219
  
    /** \brief Objective-C's \@synchronized statement.
   	*/
    final val CXCursor_ObjCAtSynchronizedStmt				: CXCursorKind.enum = 220
  
    /** \brief Objective-C's autorelease pool statement.
   	*/
    final val CXCursor_ObjCAutoreleasePoolStmt				: CXCursorKind.enum = 221
  
    /** \brief Objective-C's collection statement.
   	*/
    final val CXCursor_ObjCForCollectionStmt					: CXCursorKind.enum = 222
  
    /** \brief C++'s catch statement.
   	*/
    final val CXCursor_CXXCatchStmt									: CXCursorKind.enum = 223
  
    /** \brief C++'s try statement.
   	*/
    final val CXCursor_CXXTryStmt										: CXCursorKind.enum = 224
  
    /** \brief C++'s for (* : *) statement.
   	*/
    final val CXCursor_CXXForRangeStmt								: CXCursorKind.enum = 225
  
    /** \brief Windows Structured Exception Handling's try statement.
   	*/
    final val CXCursor_SEHTryStmt										: CXCursorKind.enum = 226
  
    /** \brief Windows Structured Exception Handling's except statement.
   	*/
    final val CXCursor_SEHExceptStmt									: CXCursorKind.enum = 227
  
    /** \brief Windows Structured Exception Handling's finally statement.
   	*/
    final val CXCursor_SEHFinallyStmt								: CXCursorKind.enum = 228
  
    /** \brief A MS inline assembly statement extension.
   	*/
    final val CXCursor_MSAsmStmt											: CXCursorKind.enum = 229
  
    /** \brief The null statement ";": C99 6.8.3p3.
   	*
   	* This cursor kind is used to describe the null statement.
   	*/
    final val CXCursor_NullStmt											: CXCursorKind.enum = 230
  
    /** \brief Adaptor class for mixing declarations with statements and
   	* expressions.
   	*/
    final val CXCursor_DeclStmt											: CXCursorKind.enum = 231
  
    /** \brief OpenMP parallel directive.
   	*/
    final val CXCursor_OMPParallelDirective					: CXCursorKind.enum = 232
  
    /** \brief OpenMP SIMD directive.
   	*/
    final val CXCursor_OMPSimdDirective							: CXCursorKind.enum = 233
  
    /** \brief OpenMP for directive.
   	*/
    final val CXCursor_OMPForDirective								: CXCursorKind.enum = 234
  
    /** \brief OpenMP sections directive.
   	*/
    final val CXCursor_OMPSectionsDirective					: CXCursorKind.enum = 235
  
    /** \brief OpenMP section directive.
   	*/
    final val CXCursor_OMPSectionDirective						: CXCursorKind.enum = 236
  
    /** \brief OpenMP single directive.
   	*/
    final val CXCursor_OMPSingleDirective						: CXCursorKind.enum = 237
  
    /** \brief OpenMP parallel for directive.
   	*/
    final val CXCursor_OMPParallelForDirective				: CXCursorKind.enum = 238
  
    /** \brief OpenMP parallel sections directive.
   	*/
    final val CXCursor_OMPParallelSectionsDirective	: CXCursorKind.enum = 239
  
    /** \brief OpenMP task directive.
   	*/
    final val CXCursor_OMPTaskDirective							: CXCursorKind.enum = 240
  
    /** \brief OpenMP master directive.
   	*/
    final val CXCursor_OMPMasterDirective						: CXCursorKind.enum = 241
  
    /** \brief OpenMP critical directive.
   	*/
    final val CXCursor_OMPCriticalDirective					: CXCursorKind.enum = 242
  
    /** \brief OpenMP taskyield directive.
   	*/
    final val CXCursor_OMPTaskyieldDirective					: CXCursorKind.enum = 243
  
    /** \brief OpenMP barrier directive.
   	*/
    final val CXCursor_OMPBarrierDirective						: CXCursorKind.enum = 244
  
    /** \brief OpenMP taskwait directive.
   	*/
    final val CXCursor_OMPTaskwaitDirective					: CXCursorKind.enum = 245
  
    /** \brief OpenMP flush directive.
   	*/
    final val CXCursor_OMPFlushDirective							: CXCursorKind.enum = 246
  
    /** \brief Windows Structured Exception Handling's leave statement.
   	*/
    final val CXCursor_SEHLeaveStmt									: CXCursorKind.enum = 247
  
    /** \brief OpenMP ordered directive.
   	*/
    final val CXCursor_OMPOrderedDirective						: CXCursorKind.enum = 248
  
    /** \brief OpenMP atomic directive.
   	*/
    final val CXCursor_OMPAtomicDirective						: CXCursorKind.enum = 249
  
    /** \brief OpenMP for SIMD directive.
   	*/
    final val CXCursor_OMPForSimdDirective						: CXCursorKind.enum = 250
  
    /** \brief OpenMP parallel for SIMD directive.
   	*/
    final val CXCursor_OMPParallelForSimdDirective		: CXCursorKind.enum = 251
  
    /** \brief OpenMP target directive.
   	*/
    final val CXCursor_OMPTargetDirective						: CXCursorKind.enum = 252
  
    /** \brief OpenMP teams directive.
   	*/
    final val CXCursor_OMPTeamsDirective							: CXCursorKind.enum = 253
  
    /** \brief OpenMP taskgroup directive.
   	*/
    final val CXCursor_OMPTaskgroupDirective					: CXCursorKind.enum = 254
  
    /** \brief OpenMP cancellation point directive.
   	*/
    final val CXCursor_OMPCancellationPointDirective : CXCursorKind.enum = 255
  
    /** \brief OpenMP cancel directive.
   	*/
    final val CXCursor_OMPCancelDirective						: CXCursorKind.enum = 256
  
    /** \brief OpenMP target data directive.
   	*/
    final val CXCursor_OMPTargetDataDirective				: CXCursorKind.enum = 257
  
    /** \brief OpenMP taskloop directive.
   	*/
    final val CXCursor_OMPTaskLoopDirective					: CXCursorKind.enum = 258
  
    /** \brief OpenMP taskloop simd directive.
   	*/
    final val CXCursor_OMPTaskLoopSimdDirective			: CXCursorKind.enum = 259
  
    /** \brief OpenMP distribute directive.
   	*/
    final val CXCursor_OMPDistributeDirective				: CXCursorKind.enum = 260
  
    /** \brief OpenMP target enter data directive.
   	*/
    final val CXCursor_OMPTargetEnterDataDirective		: CXCursorKind.enum = 261
  
    /** \brief OpenMP target exit data directive.
   	*/
    final val CXCursor_OMPTargetExitDataDirective		: CXCursorKind.enum = 262
  
    /** \brief OpenMP target parallel directive.
   	*/
    final val CXCursor_OMPTargetParallelDirective		: CXCursorKind.enum = 263
  
    /** \brief OpenMP target parallel for directive.
   	*/
    final val CXCursor_OMPTargetParallelForDirective : CXCursorKind.enum = 264
  
    /** \brief OpenMP target update directive.
   	*/
    final val CXCursor_OMPTargetUpdateDirective			: CXCursorKind.enum = 265
  
    /** \brief OpenMP distribute parallel for directive.
   	*/
    final val CXCursor_OMPDistributeParallelForDirective : CXCursorKind.enum = 266
  
    final val CXCursor_LastStmt								: CXCursorKind.enum = CXCursor_OMPDistributeParallelForDirective
  
    /**
   	* \brief Cursor that represents the translation unit itself.
   	*
   	* The translation unit cursor exists primarily to act as the root
   	* cursor for traversing the contents of a translation unit.
   	*/
    final val CXCursor_TranslationUnit								: CXCursorKind.enum = 300
  
    /* Attributes */
    final val CXCursor_FirstAttr											: CXCursorKind.enum = 400
    /**
   	* \brief An attribute whose specific kind is not exposed via this
   	* interface.
   	*/
    final val CXCursor_UnexposedAttr									: CXCursorKind.enum = 400
  
    final val CXCursor_IBActionAttr									: CXCursorKind.enum = 401
    final val CXCursor_IBOutletAttr									: CXCursorKind.enum = 402
    final val CXCursor_IBOutletCollectionAttr				: CXCursorKind.enum = 403
    final val CXCursor_CXXFinalAttr									: CXCursorKind.enum = 404
    final val CXCursor_CXXOverrideAttr								: CXCursorKind.enum = 405
    final val CXCursor_AnnotateAttr									: CXCursorKind.enum = 406
    final val CXCursor_AsmLabelAttr									: CXCursorKind.enum = 407
    final val CXCursor_PackedAttr										: CXCursorKind.enum = 408
    final val CXCursor_PureAttr											: CXCursorKind.enum = 409
    final val CXCursor_ConstAttr											: CXCursorKind.enum = 410
    final val CXCursor_NoDuplicateAttr								: CXCursorKind.enum = 411
    final val CXCursor_CUDAConstantAttr							: CXCursorKind.enum = 412
    final val CXCursor_CUDADeviceAttr								: CXCursorKind.enum = 413
    final val CXCursor_CUDAGlobalAttr								: CXCursorKind.enum = 414
    final val CXCursor_CUDAHostAttr									: CXCursorKind.enum = 415
    final val CXCursor_CUDASharedAttr								: CXCursorKind.enum = 416
    final val CXCursor_VisibilityAttr								: CXCursorKind.enum = 417
    final val CXCursor_DLLExport											: CXCursorKind.enum = 418
    final val CXCursor_DLLImport											: CXCursorKind.enum = 419
    final val CXCursor_LastAttr											: CXCursorKind.enum = CXCursor_DLLImport
  
    /* Preprocessing */
    final val CXCursor_PreprocessingDirective				: CXCursorKind.enum = 500
    final val CXCursor_MacroDefinition								: CXCursorKind.enum = 501
    final val CXCursor_MacroExpansion								: CXCursorKind.enum = 502
    final val CXCursor_MacroInstantiation						: CXCursorKind.enum = CXCursor_MacroExpansion
    final val CXCursor_InclusionDirective						: CXCursorKind.enum = 503
    final val CXCursor_FirstPreprocessing						: CXCursorKind.enum = CXCursor_PreprocessingDirective
    final val CXCursor_LastPreprocessing							: CXCursorKind.enum = CXCursor_InclusionDirective
  
    /* Extra Declarations */
    /**
   	* \brief A module import declaration.
   	*/
    final val CXCursor_ModuleImportDecl							: CXCursorKind.enum = 600
    final val CXCursor_TypeAliasTemplateDecl					: CXCursorKind.enum = 601
    /**
   	* \brief A static_assert or _Static_assert node
   	*/
    final val CXCursor_StaticAssert									: CXCursorKind.enum = 602
    final val CXCursor_FirstExtraDecl								: CXCursorKind.enum = CXCursor_ModuleImportDecl
    final val CXCursor_LastExtraDecl									: CXCursorKind.enum = CXCursor_StaticAssert
  
    /**
   	* \brief A code completion overload candidate.
   	*/
    final val CXCursor_OverloadCandidate							: CXCursorKind.enum = 700
  }

}
