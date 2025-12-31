const fs = require('fs');
const path = require('path');

const rootDir = path.join(__dirname, 'app', 'controllers');
const outputFile = path.join(__dirname, 'API_DOCS.md');

function getAllFiles(dirPath, arrayOfFiles) {
  const files = fs.readdirSync(dirPath);

  arrayOfFiles = arrayOfFiles || [];

  files.forEach(function(file) {
    if (fs.statSync(dirPath + "/" + file).isDirectory()) {
      arrayOfFiles = getAllFiles(dirPath + "/" + file, arrayOfFiles);
    } else {
      if (file.endsWith('.java')) {
        arrayOfFiles.push(path.join(dirPath, "/", file));
      }
    }
  });

  return arrayOfFiles;
}

function parseComment(comment) {
  const lines = comment.split('\n').map(l => l.replace(/^\s*\*\s?/, '').trim());
  const apiData = {};
  
  let currentTag = null;
  let currentContent = [];

  lines.forEach(line => {
    if (line.startsWith('@')) {
      // Process previous tag if exists
      if (currentTag) {
        if (!apiData[currentTag]) apiData[currentTag] = [];
        apiData[currentTag].push(currentContent.join('\n'));
      }
      
      const match = line.match(/^@(\w+)\s*(.*)/);
      if (match) {
        currentTag = match[1];
        currentContent = [match[2]];
      }
    } else {
      if (currentTag) {
        currentContent.push(line);
      }
    }
  });
  
  // Push last tag
  if (currentTag) {
    if (!apiData[currentTag]) apiData[currentTag] = [];
    apiData[currentTag].push(currentContent.join('\n'));
  }

  return apiData;
}

function generateMarkdown(apis) {
  const groups = {};

  apis.forEach(api => {
    const group = api.apiGroup ? api.apiGroup[0] : 'General';
    if (!groups[group]) groups[group] = [];
    groups[group].push(api);
  });

  let md = '# API Documentation\n\n';

  for (const groupName in groups) {
    md += `## ${groupName}\n\n`;
    
    groups[groupName].forEach(api => {
      if (api.api) {
        // Parse @api {method} path title
        const apiMatch = api.api[0].match(/\{(.*?)\}\s+(.*?)\s+(.*)/);
        const method = apiMatch ? apiMatch[1] : 'GET';
        const url = apiMatch ? apiMatch[2] : api.api[0];
        const title = apiMatch ? apiMatch[3] : '';

        md += `### ${title || url}\n\n`;
        md += `\`${method.toUpperCase()}\` **${url}**\n\n`;
        
        if (api.apiName) {
            md += `**Name:** \`${api.apiName[0]}\`\n\n`;
        }

        if (api.apiParam) {
          md += `#### Parameters\n\n`;
          md += `| Type | Name | Description |\n`;
          md += `| --- | --- | --- |\n`;
          api.apiParam.forEach(param => {
            // @apiParam {type} name description
            const pMatch = param.match(/\{(.*?)\}\s+(\S+)\s+(.*)/);
            if (pMatch) {
              md += `| ${pMatch[1]} | ${pMatch[2]} | ${pMatch[3]} |\n`;
            } else {
              md += `| - | - | ${param} |\n`;
            }
          });
          md += `\n`;
        }

        if (api.apiSuccess) {
            md += `#### Success Response\n\n`;
            md += `| Type | Name | Description |\n`;
            md += `| --- | --- | --- |\n`;
            api.apiSuccess.forEach(param => {
              // @apiSuccess {type} name description
              // Handle (Group) syntax if present, e.g. (Success 200)
              let cleanParam = param;
              const groupMatch = param.match(/^\((.*?)\)\s*(.*)/);
              if (groupMatch) {
                  cleanParam = groupMatch[2];
              }
              
              const pMatch = cleanParam.match(/\{(.*?)\}\s+(\S+)\s+(.*)/);
              if (pMatch) {
                md += `| ${pMatch[1]} | ${pMatch[2]} | ${pMatch[3]} |\n`;
              } else {
                md += `| - | - | ${cleanParam} |\n`;
              }
            });
            md += `\n`;
        }

        if (api.apiParamExample) {
            md += `#### Request Example\n\n`;
            api.apiParamExample.forEach(ex => {
                 md += `\`\`\`json\n${ex}\n\`\`\`\n\n`;
            });
        }

        if (api.apiSuccessExample) {
            md += `#### Response Example\n\n`;
            api.apiSuccessExample.forEach(ex => {
                 md += `\`\`\`json\n${ex}\n\`\`\`\n\n`;
            });
        }
        
        md += `---\n\n`;
      }
    });
  }

  return md;
}

const files = getAllFiles(rootDir);
const allApis = [];

files.forEach(file => {
  const content = fs.readFileSync(file, 'utf-8');
  // Regex to match /** ... */ blocks containing @api
  const commentRegex = /\/\*\*([\s\S]*?)\*\//g;
  let match;
  
  while ((match = commentRegex.exec(content)) !== null) {
    const comment = match[1];
    if (comment.includes('@api')) {
      const parsed = parseComment(comment);
      if (parsed.api) {
        allApis.push(parsed);
      }
    }
  }
});

const markdownOutput = generateMarkdown(allApis);
fs.writeFileSync(outputFile, markdownOutput);
console.log(`Generated documentation for ${allApis.length} endpoints.`);
