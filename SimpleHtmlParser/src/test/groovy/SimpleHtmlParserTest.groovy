import groovy.util.GroovyTestCase

class SimpleHtmlParserTest extends GroovyTestCase{
	
	def html = '''
            <div class="dust">
            </br >
            <span class="target"><p>data1</p></span>
            <span class="target"><p>data2</p></span>
            <br />
            <span class="target"><p>data3</p></span>
            </div>
        '''

	def testTag = '''<span class="target">'''
	
	void testParse() {
		def simpleHtmlParser = new SimpleHtmlParser(html)
		def parseResult = simpleHtmlParser.parse(testTag)
		assert "span" == simpleHtmlParser.tagType
		assert 3 == parseResult.size()
		assert '''<span class="target"><p>data1</p></span>''' == parseResult.get(0).trim()
	}
	
	void testParseToGPathResult(){
		def simpleHtmlParser = new SimpleHtmlParser(html)
		def parseResult = simpleHtmlParser.parseToGPathResult(testTag)
		def dev1 = parseResult.get(0)
		def dev2 = parseResult.get(1)
		def dev3 = parseResult.get(2)
		assert dev1.@class == """target"""
				assert dev1.p.text() == """data1"""
		assert dev2.p.text() == """data2"""
				assert dev3.p.text() == """data3"""
	}
	
	void testParseToGpathResultWithClosure(){
		def simpleHtmlParser = new SimpleHtmlParser(html)
		def parseResult = simpleHtmlParser.parseToGPathResult(testTag)
		StringBuilder sb = new StringBuilder()
		simpleHtmlParser.parseToGPathResult(testTag){
			sb.append(it.p.text())
		}
		assert "data1data2data3" == sb.toString()
	}
	
	void testSetTagType() {
		def simpleHtmlParser = new SimpleHtmlParser(html)
		assert "div" == simpleHtmlParser.setTagType("<div>asdf</div>")
		assert "div" == simpleHtmlParser.setTagType("<div class='hoge'></div>")
		assert "div" == simpleHtmlParser.setTagType("<div /></div>")
	}
}
