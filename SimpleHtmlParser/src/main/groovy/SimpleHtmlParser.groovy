class SimpleHtmlParser {

	def html
	def tagType

	def SimpleHtmlParser(List data){
		html = data
	}
	
	def SimpleHtmlParser(String data){
		this(data.readLines())
	}

	List parse(String startTag) {
		def resultStack = []
		def process = false
		def counter = 0
		def StringBuilder sb = new StringBuilder()
		
		setTagType(startTag)
		html.each{ line ->

			// store start
			if( !process && line.indexOf(startTag) >= 0){ process = true }
			if(process){
				counter += countStartTag(line) - countEndTag(line)
				sb.append("${line}\n")

				// 開始タグに対応する終了タグを検知
				if ( counter == 0 ){
					process = false
					resultStack.add(sb.toString())
					sb.setLength(0)
				}
			}
		}
		resultStack
	}

	List parseToGPathResult(String startTag){
		def result = parse(startTag)
		result.collect({new XmlSlurper().parseText(it)})
	}
	
	List parseToGPathResult(String startTag, Closure closure){
		def slurpers = parseToGPathResult(startTag)
		slurpers.each{ slurper ->
			closure.call(slurper)
		}
	}
	
	// set first tag from target tag
	// for example:
	// <div class="a"> ---> div
	// <div> ---> div
	// <div /> ---> div
	def setTagType(String targetTag){
		def match = (targetTag =~ /<([a-zA-Z]+)([^>]*)>.*/)
		if (match.matches()){
			tagType = match[0][1]
		}
		tagType
	}

	def countStartTag(String line){
		line.findAll(~/<${tagType}/).size()
	}

	def countEndTag(String line){
		line.findAll(~/<\/${tagType}>/).size()
	}
}
