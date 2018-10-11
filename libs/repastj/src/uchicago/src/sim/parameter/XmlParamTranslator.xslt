<!--
 Copyright (c) 2004, Repast Organization for Architecture and Design (ROAD)
 All rights reserved.

 Redistribution and use in source and binary forms, with 
 or without modification, are permitted provided that the following 
 conditions are met:

	 Redistributions of source code must retain the above copyright notice,
	 this list of conditions and the following disclaimer.

	 Redistributions in binary form must reproduce the above copyright notice,
	 this list of conditions and the following disclaimer in the documentation
	 and/or other materials provided with the distribution.

 Neither the name of the ROAD nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
-->
<!--
This file is used to translate XML parameter files to their equivalent "normal"
parameter file.

author: Jerry Vos
version: $Revision: 1.2 $ $Date: 2005/03/31 22:41:33 $
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output
    method="text"
    omit-xml-declaration="yes"/>


<!-- The top level run block -->
<xsl:template match="/Repast:Params/Repast:ParamBlock" xmlns:Repast="http://www.src.uchicago.edu">
	<xsl:text>
	runs: </xsl:text> <xsl:value-of select="@runs"/>
	
	<xsl:text>
	</xsl:text>
	
	<xsl:apply-templates select="Repast:Param"/>
</xsl:template>


<!-- All other run blocks block -->
<xsl:template match="Repast:ParamBlock" xmlns:Repast="http://www.src.uchicago.edu">
	<xsl:text>
	{
	runs: </xsl:text> <xsl:value-of select="@runs"/>
	
	<xsl:text>
	</xsl:text>
	
	<xsl:apply-templates select="Repast:Param"/>
	
	<xsl:text>
	}
	</xsl:text>
</xsl:template>


<!-- Incrementing parameters -->
<xsl:template match="Repast:Param[@type='incr']" xmlns:Repast="http://www.src.uchicago.edu">

	<xsl:value-of select="@name"/> 

	<!-- This tests to see if an "io" parameter is present -->	
	<xsl:choose>
		<xsl:when test="@io">
			<xsl:text> ( </xsl:text> <xsl:value-of select="@io"/> <xsl:text> ) {
			</xsl:text>
		</xsl:when>
		
		<xsl:otherwise>
			<xsl:text> {
			</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	
	<xsl:text>start: </xsl:text> <xsl:value-of select="@start"/>
	
	<xsl:text>
	end: </xsl:text> <xsl:value-of select="@end"/>
	<xsl:text>
	incr: </xsl:text> <xsl:value-of select="@incr"/>
	
	<xsl:apply-templates/>
	
	<xsl:text>
	}
	</xsl:text>
</xsl:template>



<!-- Constant parameters (same as set:) -->
<xsl:template match="Repast:Param[@type='const']" xmlns:Repast="http://www.src.uchicago.edu">
	<xsl:value-of select="@name"/>

	<!-- This tests to see if an "io" parameter is present -->	
	<xsl:choose>
		<xsl:when test="@io">
			<xsl:text> ( </xsl:text> <xsl:value-of select="@io"/> <xsl:text> ) {
			</xsl:text>
		</xsl:when>
		
		<xsl:otherwise>
			<xsl:text> {
			</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	
	<xsl:text>set: </xsl:text> <xsl:value-of select="@value"/>
	
	<xsl:text>
	</xsl:text>
	
	<xsl:text>
	}
	</xsl:text>
</xsl:template>


<!-- List parameters -->
<xsl:template match="Repast:Param[@type='list' or @type='string_list' or @type='boolean_list']" xmlns:Repast="http://www.src.uchicago.edu">
	<xsl:value-of select="@name"/>

	<!-- This tests to see if an "io" parameter is present -->	
	<xsl:choose>
		<xsl:when test="@io">
			<xsl:text> ( </xsl:text> <xsl:value-of select="@io"/> <xsl:text> ) {
			</xsl:text>
		</xsl:when>
		
		<xsl:otherwise>
			<xsl:text> {
			</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	
	<xsl:text>set_</xsl:text><xsl:value-of select="@type"/><xsl:text>: </xsl:text> <xsl:value-of select="@value"/>
	
	<xsl:text>
	</xsl:text>
	
	<xsl:apply-templates/>
	
	<xsl:text>
	}
	</xsl:text>
</xsl:template>


<!-- Set parameters. Note: you can't nest a param block (run block) in a set -->
<xsl:template match="Repast:Param[@type='set' or @type='set_string' or @type='set_boolean']" xmlns:Repast="http://www.src.uchicago.edu">
	<xsl:value-of select="@name"/> 

	<!-- This tests to see if an "io" parameter is present -->	
	<xsl:choose>
		<xsl:when test="@io">
			<xsl:text> ( </xsl:text> <xsl:value-of select="@io"/> <xsl:text> ) {
			</xsl:text>
		</xsl:when>
		
		<xsl:otherwise>
			<xsl:text> {
			</xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	
	<xsl:value-of select="@type"/><xsl:text>: </xsl:text> <xsl:value-of select="@value"/>
	
	<xsl:text>
	</xsl:text>
	
	<xsl:text>
	}
	</xsl:text>
</xsl:template>


</xsl:stylesheet>
